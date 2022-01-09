package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Slf4j
public class BuiltinRedisJt808ResponseSubPackageStorage implements Jt808ResponseSubPackageStorage {

    private final RedisTemplate<String, RedisJt808ResponseSubPackageCacheItem> redisTemplate;

    public BuiltinRedisJt808ResponseSubPackageStorage(RedisTemplate<String, RedisJt808ResponseSubPackageCacheItem> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveSubPackage(Jt808Response.Jt808ResponseSubPackage subPackage) {
        log.debug("<<ResponseSubPackage>> terminalId={}, flowId={}, {}/{}", subPackage.terminalId(), subPackage.flowId(), subPackage.totalSubPackageCount(),
                subPackage.currentPackageNo());
        final String hexString = HexStringUtils.byteBufToString(subPackage.msg());
        final var cacheItem = new RedisJt808ResponseSubPackageCacheItem(hexString, subPackage.createdAt());
        this.redisTemplate.opsForHash().put(generateKey(subPackage.terminalId(), subPackage.firstFlowIdOfSubPackageGroup()), generateHashKey(subPackage.currentPackageNo()), cacheItem);
    }

    @Override
    public Collection<ByteBuf> getSubPackageMsg(String terminalId, int firstFlowIdOfSubPackage, Collection<Integer> packageIds) {
        final String key = this.generateKey(terminalId, firstFlowIdOfSubPackage);
        final List<String> hashKeys = packageIds.stream().map(this::generateHashKey).collect(Collectors.toList());
        //final List<Map<String,Object>> objects = this.redisTemplate.opsForHash().multiGet(key, hashKeys);
        final HashOperations<String, String, RedisJt808ResponseSubPackageCacheItem> hashOperations = this.redisTemplate.opsForHash();
        final List<RedisJt808ResponseSubPackageCacheItem> list = hashOperations.multiGet(key, hashKeys);
        System.out.println(list);
        // <terminalId,<firstFlowIdOfSomeSubPackage, List<msg>>>
        //List<RedisJt808ResponseSubPackageCacheItem> list=this.redisTemplate.opsForHash().multiGet("", Set.of(packageIds));
        //return requireNonNull(this.cache.get(terminalId, terminal -> new HashMap<>()))
        //        .getOrDefault(firstFlowIdOfSubPackage, new ArrayList<>())
        //        .stream()
        //        .filter(it -> packageIds.contains(it.currentPackageNo()))
        //        .sorted(Comparator.comparing(Jt808Response.Jt808ResponseSubPackage::currentPackageNo))
        //        .map(Jt808Response.Jt808ResponseSubPackage::msg);
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
}
