package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Slf4j
public class RedisJt808ResponseSubPackageStorage implements Jt808ResponseSubPackageStorage {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ResponseSubPackageStorageProps storageProps;

    public RedisJt808ResponseSubPackageStorage(ResponseSubPackageStorageProps storageProps, RedisTemplate<String, Object> redisTemplate) {
        this.storageProps = storageProps;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveSubPackage(Jt808Response.Jt808ResponseSubPackage subPackage) {
        log.debug("<<ResponseSubPackage>> terminalId={}, flowId={}, {}/{}", subPackage.terminalId(), subPackage.flowId(), subPackage.totalSubPackageCount(),
                subPackage.currentPackageNo());
        final String hexString = HexStringUtils.byteBufToString(subPackage.msg());
        final var cacheItem = new RedisJt808ResponseSubPackageCacheItem(hexString, subPackage.createdAt());
        final String key = generateKey(subPackage.terminalId(), subPackage.firstFlowIdOfSubPackageGroup());
        //this.redisTemplate.opsForHash().put(key, generateHashKey(subPackage.currentPackageNo()), cacheItem);
        final BoundHashOperations<String, Object, Object> boundHashOps = this.redisTemplate.boundHashOps(key);
        boundHashOps.put(generateHashKey(subPackage.currentPackageNo()), cacheItem);
        boundHashOps.expire(storageProps.getTtl().toSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public Collection<ByteBuf> getSubPackageMsg(String terminalId, int firstFlowIdOfSubPackage, Collection<Integer> packageIds) {
        final String key = this.generateKey(terminalId, firstFlowIdOfSubPackage);
        final List<String> hashKeys = packageIds.stream().map(this::generateHashKey).collect(Collectors.toList());

        // <terminalId,<firstFlowIdOfSomeSubPackage, List<msg>>>
        final HashOperations<String, String, RedisJt808ResponseSubPackageCacheItem> hashOperations = this.redisTemplate.opsForHash();

        final List<RedisJt808ResponseSubPackageCacheItem> list = hashOperations.multiGet(key, hashKeys);

        return list.stream()
                .map(it -> ByteBufAllocator.DEFAULT.buffer()
                        .writeBytes(HexStringUtils.hexString2Bytes(it.getHexString()))
                )
                .collect(Collectors.toList());
    }

    @Nonnull
    private String generateKey(String terminalId, int firstFlowIdOfSubPackageGroup) {
        return terminalId + "::" + firstFlowIdOfSubPackageGroup;
    }

    @Nonnull
    private String generateHashKey(int currentPackageNo) {
        return String.valueOf(currentPackageNo);
    }

    @Data
    public static class ResponseSubPackageStorageProps {
        private Duration ttl = Duration.ofMinutes(3);
    }
}
