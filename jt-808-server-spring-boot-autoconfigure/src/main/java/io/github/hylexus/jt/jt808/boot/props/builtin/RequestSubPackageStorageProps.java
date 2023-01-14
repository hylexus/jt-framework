package io.github.hylexus.jt.jt808.boot.props.builtin;

import io.github.hylexus.jt.jt808.support.codec.impl.CaffeineJt808RequestSubPackageStorage;
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
public class RequestSubPackageStorageProps {

    private Type type = Type.CAFFEINE;

    @NestedConfigurationProperty
    private CaffeineJt808RequestSubPackageStorage.StorageConfig caffeine = new CaffeineJt808RequestSubPackageStorage.StorageConfig();

    public enum Type {
        /**
         * 忽略分包请求
         */
        NONE,
        /**
         * 基于 caffeine 的请求分包消息暂存器
         */
        CAFFEINE
    }

}
