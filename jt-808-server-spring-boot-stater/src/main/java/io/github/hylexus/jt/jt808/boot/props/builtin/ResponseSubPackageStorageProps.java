package io.github.hylexus.jt.jt808.boot.props.builtin;

import io.github.hylexus.jt.jt808.support.codec.impl.RedisJt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.CaffeineJt808ResponseSubPackageStorage;
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
public class ResponseSubPackageStorageProps extends Switchable {

    private Type type = Type.CAFFEINE;

    @NestedConfigurationProperty
    private CaffeineJt808ResponseSubPackageStorage.ResponseSubPackageStorageProps caffeine = new CaffeineJt808ResponseSubPackageStorage.ResponseSubPackageStorageProps();

    @NestedConfigurationProperty
    private RedisJt808ResponseSubPackageStorage.ResponseSubPackageStorageProps redis = new RedisJt808ResponseSubPackageStorage.ResponseSubPackageStorageProps();

    public enum Type {
        CAFFEINE, REDIS
    }
}
