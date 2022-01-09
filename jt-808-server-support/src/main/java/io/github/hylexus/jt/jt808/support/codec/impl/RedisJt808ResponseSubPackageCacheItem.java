package io.github.hylexus.jt.jt808.support.codec.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author hylexus
 */
@Data
@NoArgsConstructor
public class RedisJt808ResponseSubPackageCacheItem {
    private String hexString;
    private LocalDateTime createdAt;

    public RedisJt808ResponseSubPackageCacheItem(String hexString, LocalDateTime createdAt) {
        this.hexString = hexString;
        this.createdAt = createdAt;
    }
}
