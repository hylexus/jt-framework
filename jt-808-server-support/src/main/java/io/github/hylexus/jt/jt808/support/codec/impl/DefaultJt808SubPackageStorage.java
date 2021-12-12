package io.github.hylexus.jt.jt808.support.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.SubPackageSupportedJt808Request;
import io.github.hylexus.jt.jt808.support.codec.Jt808SubPackageStorage;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt808SubPackageStorage implements Jt808SubPackageStorage {

    protected final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    protected final Cache<String, List<SubPackageSupportedJt808Request.Jt808SubPackage>> cache;

    public DefaultJt808SubPackageStorage() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .removalListener(new RemovalListener<String, List<SubPackageSupportedJt808Request.Jt808SubPackage>>() {
                    @Override
                    public void onRemoval(
                            @Nullable String key, @Nullable List<SubPackageSupportedJt808Request.Jt808SubPackage> value, @Nonnull RemovalCause cause) {

                        log.info("Removed , key={}", key);
                        if (value != null) {
                            for (SubPackageSupportedJt808Request.Jt808SubPackage subPackage : value) {
                                try {
                                    JtProtocolUtils.release(subPackage.body());
                                } catch (Throwable e) {
                                    log.error("An error occurred while release SubPackage", e);
                                }
                            }
                        }
                    }
                })
                .build();
    }

    @Override
    public boolean isAllSubPackagesArrived(SubPackageSupportedJt808Request request) {
        final String key = this.buildSubPackageCacheKey(request);
        final SubPackageSupportedJt808Request.Jt808SubPackage subPackage = request.subPackage();
        final List<SubPackageSupportedJt808Request.Jt808SubPackage> list = this.cache.getIfPresent(key);

        return list != null
               && (list.size() == (subPackage.totalSubPackageCount() - 1));
    }

    @Override
    public void storeSubPackage(SubPackageSupportedJt808Request request) {
        final String key = this.buildSubPackageCacheKey(request);
        List<SubPackageSupportedJt808Request.Jt808SubPackage> list = this.cache.getIfPresent(key);
        if (list == null) {
            list = new ArrayList<>();
            this.cache.put(key, list);
        }
        list.add(request.subPackage());
    }

    @Override
    public ByteBuf getAllSubPackages(SubPackageSupportedJt808Request request) {
        final String key = this.buildSubPackageCacheKey(request);
        final List<SubPackageSupportedJt808Request.Jt808SubPackage> list = this.cache.getIfPresent(key);
        if (list == null) {
            return null;
        }
        final CompositeByteBuf compositeByteBuf = this.allocator.compositeBuffer(list.size() + 1);
        list.stream()
                .sorted(Comparator.comparing(SubPackageSupportedJt808Request.Jt808SubPackage::currentPackageNo))
                .forEach(jt808SubPackage -> compositeByteBuf.addComponents(true, jt808SubPackage.body().copy()));
        compositeByteBuf.addComponents(true, request.subPackage().body());
        return compositeByteBuf;
    }

    protected String buildSubPackageCacheKey(Jt808Request request) {
        return String.format("%s_%d", request.terminalId(), request.msgType().getMsgId());
    }
}
