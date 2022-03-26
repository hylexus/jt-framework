package io.github.hylexus.jt.jt808.support.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
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
    protected final Cache<String, Map<Integer, Jt808Request>> cache;

    protected final ByteBufAllocator allocator;
    private Jt808RequestMsgDispatcher requestMsgDispatcher;

    public void setRequestMsgDispatcher(Jt808RequestMsgDispatcher requestMsgDispatcher) {
        this.requestMsgDispatcher = requestMsgDispatcher;
    }

    public CaffeineJt808RequestSubPackageStorage(
            ByteBufAllocator allocator,
            StorageConfig subPackageStorageConfig) {

        this.allocator = allocator;
        //this.requestMsgDispatcher = requestMsgDispatcher;
        this.cache = Caffeine.newBuilder()
                .maximumSize(subPackageStorageConfig.maximumSize)
                .expireAfterWrite(subPackageStorageConfig.getTtl())
                //.expireAfterAccess(subPackageStorageConfig.getTtl())
                .removalListener((RemovalListener<String, Map<Integer, Jt808Request>>) (key, value, cause) -> {
                    if (value == null) {
                        return;
                    }
                    final boolean isExpired = cause == RemovalCause.EXPIRED
                                              || cause == RemovalCause.SIZE
                                              || cause == RemovalCause.COLLECTED;
                    if (isExpired) {
                        value.forEach((currentPackageNo, subPackage) -> {
                            subPackage.release();
                            log.debug("{} {} has been released. reason = {}", LOG_PREFIX, subPackage, cause);
                        });
                    }

                })
                .build();
    }

    @Override
    public void saveSubPackage(Jt808Request request) {
        final String key = this.buildSubPackageCacheKey(request);
        final Map<Integer, Jt808Request> map = this.cache.get(key, k -> new ConcurrentHashMap<>());
        final Jt808RequestHeader.Jt808SubPackageProps jt808SubPackageProps = request.header().subPackage();
        requireNonNull(map).put(jt808SubPackageProps.currentPackageNo(), request.copy());

        this.getAllSubPackages(key, request).ifPresent(mergedBody -> {
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

    protected Jt808Request buildRequest(Jt808Request request, ByteBuf mergedBody) {
        final Jt808RequestHeader.Jt808MsgBodyProps newMsgBodyProps = request.header().msgBodyProps()
                .mutate()
                .msgBodyLength(mergedBody.readableBytes())
                .hasSubPackage(false)
                .build();

        final Jt808RequestHeader newHeader = request.header().mutate()
                .msgBodyProps(newMsgBodyProps)
                .subPackageProps(null)
                .build();

        return request.mutate()
                .header(newHeader)
                .body(mergedBody, false)
                .rawByteBuf(null, false)
                .calculatedCheckSum((byte) 0)
                .originalCheckSum((byte) 0)
                .build();
    }

    // TODO 分包补传(0x8003)
    // TODO 分包消息 ACK
    public Optional<ByteBuf> getAllSubPackages(String key, Jt808Request request) {
        final Map<Integer, Jt808Request> map = this.cache.get(key, k -> new ConcurrentHashMap<>());
        if (requireNonNull(map).size() < request.header().subPackage().totalSubPackageCount()) {
            return Optional.empty();
        }

        final CompositeByteBuf compositeByteBuf = this.allocator.compositeBuffer(map.size());
        map.values().stream()
                .sorted(Comparator.comparing(e -> e.header().subPackage().currentPackageNo()))
                .forEach(subPackage -> {
                    compositeByteBuf.addComponents(true, subPackage.body());
                    JtProtocolUtils.release(subPackage.rawByteBuf());
                });

        this.cache.invalidate(key);
        return Optional.of(compositeByteBuf);
    }

    protected String buildSubPackageCacheKey(Jt808Request request) {
        return String.format("%s_%d_%d", request.terminalId(), request.msgType().getMsgId(), request.header().subPackage().totalSubPackageCount());
    }

    @Data
    public static class StorageConfig {
        private long maximumSize = 1024;
        private Duration ttl = Duration.ofSeconds(45);
    }
}
