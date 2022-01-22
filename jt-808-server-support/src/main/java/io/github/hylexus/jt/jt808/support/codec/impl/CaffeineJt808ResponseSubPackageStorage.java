package io.github.hylexus.jt.jt808.support.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class CaffeineJt808ResponseSubPackageStorage implements Jt808ResponseSubPackageStorage {

    protected final Cache<String, Map<Integer, List<Jt808Response.Jt808ResponseSubPackage>>> cache;

    public CaffeineJt808ResponseSubPackageStorage(StorageConfig storageProps) {
        // <terminalId,<firstFlowIdOfSomeSubPackage, List<msg>>>
        this.cache = Caffeine.newBuilder()
                .maximumSize(storageProps.getMaximumSize())
                .expireAfterWrite(storageProps.getTtl())
                .expireAfterAccess(storageProps.getTtl())
                .removalListener((RemovalListener<String, Map<Integer, List<Jt808Response.Jt808ResponseSubPackage>>>) (key, value, cause) -> {
                    if (value == null) {
                        return;
                    }
                    value.forEach((flowId, response) -> response.forEach(resp -> JtProtocolUtils.release(resp.msg())));
                })
                .build();
    }

    @Override
    public void saveSubPackage(Jt808Response.Jt808ResponseSubPackage subPackage) {
        log.debug("<<ResponseSubPackage>> terminalId={}, flowId={}, {}/{}", subPackage.terminalId(), subPackage.flowId(), subPackage.totalSubPackageCount(),
                subPackage.currentPackageNo());

        // <terminalId,<firstFlowIdOfSomeSubPackage, List<msg>>>
        requireNonNull(this.cache.get(subPackage.terminalId(), terminalId -> new HashMap<>()))
                .computeIfAbsent(subPackage.firstFlowIdOfSubPackageGroup(), integer -> new ArrayList<>())
                .add(subPackage.copy());
    }

    @Override
    public Collection<ByteBuf> getSubPackageMsg(String terminalId, int firstFlowIdOfSubPackage, Collection<Integer> packageIds) {
        // <terminalId,<firstFlowIdOfSomeSubPackage, List<msg>>>
        return requireNonNull(this.cache.get(terminalId, terminal -> new HashMap<>()))
                .getOrDefault(firstFlowIdOfSubPackage, new ArrayList<>())
                .stream()
                .filter(it -> packageIds.contains(it.currentPackageNo()))
                .sorted(Comparator.comparing(Jt808Response.Jt808ResponseSubPackage::currentPackageNo))
                .map(Jt808Response.Jt808ResponseSubPackage::msg)
                .collect(Collectors.toList());
    }

    @Data
    public static class StorageConfig {
        private long maximumSize = 1024;
        private Duration ttl = Duration.ofSeconds(45);
    }
}
