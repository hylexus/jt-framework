package io.github.hylexus.jt.jt808.support.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.Jt808SubPackageRequest;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class CaffeineJt808RequestSubPackageStorage implements Jt808RequestSubPackageStorage {

    public static final String LOG_PREFIX = "<SubPackage>";
    // <terminalId_msgId_totalSubPackageCount,Map<currentSubPackageNo,Request>>
    protected final Cache<String, Map<Integer, Jt808SubPackageRequest.Jt808SubPackage>> cache;

    protected final ByteBufAllocator allocator;
    private final Jt808RequestMsgDispatcher requestMsgDispatcher;

    public CaffeineJt808RequestSubPackageStorage(
            ByteBufAllocator allocator,
            Jt808RequestMsgDispatcher requestMsgDispatcher,
            RequestSubPackageStorageConfig subPackageStorageConfig) {

        this.allocator = allocator;
        this.requestMsgDispatcher = requestMsgDispatcher;
        this.cache = Caffeine.newBuilder()
                .maximumSize(subPackageStorageConfig.maximumSize)
                .expireAfterWrite(subPackageStorageConfig.getTtl())
                .expireAfterAccess(subPackageStorageConfig.getTtl())
                .removalListener((RemovalListener<String, Map<Integer, Jt808SubPackageRequest.Jt808SubPackage>>) (key, value, cause) -> {
                    if (value == null) {
                        return;
                    }

                    value.forEach((currentPackageNo, subPackage) -> {
                        JtProtocolUtils.release(subPackage.body());
                        log.debug("{} {} has been released.", LOG_PREFIX, subPackage);
                    });

                })
                .build();
    }

    @Override
    public void saveSubPackage(Jt808SubPackageRequest request) {
        final String key = this.buildSubPackageCacheKey(request);
        final Map<Integer, Jt808SubPackageRequest.Jt808SubPackage> map = this.cache.get(key, k -> new ConcurrentHashMap<>());
        requireNonNull(map).put(request.subPackage().currentPackageNo(), request.subPackage());

        this.getAllSubPackages(request).ifPresent(mergedBody -> {
            final Jt808Request mergedRequest = this.buildRequest(request, mergedBody);
            if (log.isDebugEnabled()) {
                log.debug("Redispatch mergedRequest : {}", mergedRequest);
            }
            try {
                this.requestMsgDispatcher.doDispatch(mergedRequest);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected Jt808Request buildRequest(Jt808SubPackageRequest request, ByteBuf mergedBody) {
        final Jt808RequestHeader.Jt808MsgBodyProps newMsgBodyProps = request.header().msgBodyProps()
                .mutate()
                .msgBodyLength(mergedBody.readableBytes())
                .subPackageIdentifier(false)
                .build();

        final Jt808RequestHeader newHeader = request.header().mutate().msgBodyProps(newMsgBodyProps).build();

        return request.mutate()
                .header(newHeader)
                .body(mergedBody)
                .rawByteBuf(null)
                .calculatedCheckSum((byte) 0)
                .originalCheckSum((byte) 0)
                .build();
    }

    public Optional<ByteBuf> getAllSubPackages(Jt808SubPackageRequest request) {
        final String key = this.buildSubPackageCacheKey(request);
        final Map<Integer, Jt808SubPackageRequest.Jt808SubPackage> map = this.cache.get(key, k -> new ConcurrentHashMap<>());
        if (requireNonNull(map).size() < request.subPackage().totalSubPackageCount()) {
            return Optional.empty();
        }

        map.put(request.subPackage().currentPackageNo(), request.subPackage());

        final CompositeByteBuf compositeByteBuf = this.allocator.compositeBuffer(map.size());
        map.values().stream()
                .sorted(Comparator.comparing(Jt808SubPackageRequest.Jt808SubPackage::currentPackageNo))
                .forEach(subPackage -> compositeByteBuf.addComponents(true, subPackage.body().retain()));

        this.cache.invalidate(key);
        return Optional.of(compositeByteBuf);
    }

    protected String buildSubPackageCacheKey(Jt808SubPackageRequest request) {
        return String.format("%s_%d_%d", request.terminalId(), request.msgType().getMsgId(), request.subPackage().totalSubPackageCount());
    }

    @Data
    public static class RequestSubPackageStorageConfig {
        private long maximumSize = 128;
        private Duration ttl = Duration.ofSeconds(45);
    }
}
