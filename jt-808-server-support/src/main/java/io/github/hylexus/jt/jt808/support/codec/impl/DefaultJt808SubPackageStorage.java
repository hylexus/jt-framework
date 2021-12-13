package io.github.hylexus.jt.jt808.support.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808SubPackageRequest;
import io.github.hylexus.jt.jt808.support.codec.Jt808SubPackageStorage;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt808SubPackageStorage implements Jt808SubPackageStorage {

    public static final String LOG_PREFIX = "<SubPackage>";
    private final List<SubPackageEventListener> listenerList = new ArrayList<>();
    protected final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    protected final Cache<String, List<Jt808SubPackageRequest.Jt808SubPackage>> cache;

    public DefaultJt808SubPackageStorage(long maxCapacity, Duration subPackageTtl) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxCapacity)
                .expireAfterWrite(subPackageTtl)
                .removalListener((RemovalListener<String, List<Jt808SubPackageRequest.Jt808SubPackage>>) (key, value, cause) -> {
                    if (value == null) {
                        return;
                    }

                    final boolean isExpired = cause == RemovalCause.EXPIRED
                                              || cause == RemovalCause.SIZE
                                              || cause == RemovalCause.COLLECTED;
                    for (Jt808SubPackageRequest.Jt808SubPackage subPackage : value) {
                        try {
                            if (isExpired) {
                                this.invokeListener(EventType.PACKAGE_EXPIRED, subPackage);
                            }
                        } finally {
                            JtProtocolUtils.release(subPackage.body());
                            log.debug("{} {} has been released.", LOG_PREFIX, subPackage);
                        }
                    }

                })
                .build();
    }

    @Override
    public void storeSubPackage(Jt808SubPackageRequest request) {
        final String key = this.buildSubPackageCacheKey(request);
        final List<Jt808SubPackageRequest.Jt808SubPackage> list = this.cache.get(key, k -> new ArrayList<>());
        requireNonNull(list).add(request.subPackage());
        this.invokeListener(EventType.PACKAGE_ARRIVED, request.subPackage());
    }

    @Override
    public Optional<ByteBuf> getAllSubPackages(Jt808SubPackageRequest request) {
        final String key = this.buildSubPackageCacheKey(request);
        final List<Jt808SubPackageRequest.Jt808SubPackage> list = this.cache.get(key, k -> new ArrayList<>());
        if (requireNonNull(list).size() < request.subPackage().totalSubPackageCount() - 1) {
            return Optional.empty();
        }

        this.invokeListener(EventType.PACKAGE_ARRIVED, request.subPackage());

        list.add(request.subPackage());

        final CompositeByteBuf compositeByteBuf = this.allocator.compositeBuffer(list.size());
        list.stream()
                .sorted(Comparator.comparing(Jt808SubPackageRequest.Jt808SubPackage::currentPackageNo))
                .peek(subPackage -> invokeListener(EventType.PACKAGE_CONSUMED, subPackage))
                .forEach(jt808SubPackage -> compositeByteBuf.addComponents(true, jt808SubPackage.body().retain()));

        this.cache.invalidate(key);
        return Optional.of(compositeByteBuf);
    }

    private void invokeListener(EventType eventType, Jt808SubPackageRequest.Jt808SubPackage subPackage) {
        for (SubPackageEventListener listener : this.listenerList) {
            try {
                listener.onEvent(new Event(eventType, subPackage));
            } catch (Exception e) {
                log.error("An error occurred while callback SubPackageStorageListener.", e);
            }
        }
    }

    @Override
    public synchronized void addListener(SubPackageEventListener listener) {
        this.listenerList.add(listener);
        this.listenerList.sort(Comparator.comparing(OrderedComponent::getOrder));
    }

    protected String buildSubPackageCacheKey(Jt808Request request) {
        return String.format("%s_%d", request.terminalId(), request.msgType().getMsgId());
    }

}
