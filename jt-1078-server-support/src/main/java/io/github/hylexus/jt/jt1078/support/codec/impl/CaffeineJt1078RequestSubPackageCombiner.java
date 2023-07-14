package io.github.hylexus.jt.jt1078.support.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubPackageIdentifier;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078RequestSubPackageCombiner;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class CaffeineJt1078RequestSubPackageCombiner implements Jt1078RequestSubPackageCombiner {
    public static final String LOG_PREFIX = "<SubPackage>";

    protected final Cache<String, List<ByteBuf>> cache;
    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    public CaffeineJt1078RequestSubPackageCombiner(StorageConfig subPackageStorageConfig) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(subPackageStorageConfig.maximumSize)
                .expireAfterWrite(subPackageStorageConfig.getTtl())
                .removalListener((RemovalListener<String, List<ByteBuf>>) (key, value, cause) -> {
                    if (value == null) {
                        return;
                    }
                    final boolean isExpired = cause == RemovalCause.EXPIRED
                            || cause == RemovalCause.SIZE
                            || cause == RemovalCause.COLLECTED;
                    if (isExpired) {
                        value.forEach(byteBuf -> {
                            JtCommonUtils.release(byteBuf);
                            log.warn("{} {} has been released. reason = {}", LOG_PREFIX, key, cause);
                        });
                    }

                })
                .build();
    }

    @Override
    public Optional<Jt1078Request> tryCombine(Jt1078Request request) {

        if (request.header().subPackageIdentifier() == Jt1078SubPackageIdentifier.ATOMIC) {
            return Jt1078RequestSubPackageCombiner.NO_OPS.tryCombine(request);
        }

        return Optional.ofNullable(merge(request));
    }

    protected Jt1078Request merge(Jt1078Request originalRequest) {
        try {
            final Jt1078SubPackageIdentifier identifier = originalRequest.header().subPackageIdentifier();

            final String key = key(originalRequest);
            if (identifier == Jt1078SubPackageIdentifier.FIRST_SUB_PACKAGE) {
                final List<ByteBuf> list = this.cache.get(key, k -> new ArrayList<>());
                Objects.requireNonNull(list).add(originalRequest.body().copy());
                return null;
            }

            if (identifier == Jt1078SubPackageIdentifier.MIDDLE_SUB_PACKAGE) {
                final List<ByteBuf> list = this.cache.get(key, k -> new ArrayList<>());
                if (list == null) {
                    return null;
                }
                list.add(originalRequest.body().copy());
                return null;
            }

            if (identifier == Jt1078SubPackageIdentifier.LAST_SUB_PACKAGE) {
                final List<ByteBuf> list = this.cache.getIfPresent(key);
                if (list == null) {
                    return null;
                }
                int len = 0;
                final CompositeByteBuf newBody = allocator.compositeBuffer();
                try {
                    for (final ByteBuf buf : list) {
                        len += buf.readableBytes();
                        newBody.addComponent(true, buf);
                    }
                    newBody.addComponent(true, originalRequest.body().copy());
                    len += originalRequest.msgBodyLength();
                } catch (Throwable e) {
                    JtCommonUtils.release(newBody);
                    throw e;
                }

                final Jt1078Request newRequest = originalRequest.mutate()
                        .rawByteBuf(null, false)
                        .body(newBody, false)
                        .header(newHeader(originalRequest, len))
                        .build();
                list.clear();
                this.cache.invalidate(key);
                return newRequest;
            }

            log.error("Unknown identifier: " + identifier);
            return null;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    protected Jt1078RequestHeader newHeader(Jt1078Request request, int len) {
        return request.header().mutate()
                .msgBodyLength(len)
                .dataType(request.dataTypeValue())
                .subPackageIdentifier(Jt1078SubPackageIdentifier.ATOMIC)
                .isCombined(true)
                .build();
    }

    protected String key(Jt1078Request request) {
        return request.sim() + "_" + request.channelNumber();
    }

    @Data
    public static class StorageConfig {
        private long maximumSize = 1024;
        private Duration ttl = Duration.ofSeconds(45);
    }
}
