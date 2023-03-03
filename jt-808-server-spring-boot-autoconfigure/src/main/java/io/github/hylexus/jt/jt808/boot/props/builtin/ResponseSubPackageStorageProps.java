package io.github.hylexus.jt.jt808.boot.props.builtin;

import io.github.hylexus.jt.jt808.support.codec.impl.CaffeineJt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.RedisJt808ResponseSubPackageStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class ResponseSubPackageStorageProps {

    private Type type = Type.CAFFEINE;

    @NestedConfigurationProperty
    private CaffeineJt808ResponseSubPackageStorage.StorageConfig caffeine = new CaffeineJt808ResponseSubPackageStorage.StorageConfig();

    @NestedConfigurationProperty
    private RedisJt808ResponseSubPackageStorage.StorageConfig redis = new RedisJt808ResponseSubPackageStorage.StorageConfig();

    public enum Type {
        /**
         * 不保留分包历史信息(无法应答 {@link io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType#CLIENT_RETRANSMISSION 0x0005} 消息)
         */
        NONE,
        /**
         * 基于 caffeine 的响应分包消息暂存器
         */
        CAFFEINE,
        /**
         * 基于 redis 的响应分包消息暂存器
         */
        REDIS
    }

}
