package io.github.hylexus.jt.jt1078.boot.props.subpackage;

import io.github.hylexus.jt.jt1078.support.codec.impl.CaffeineJt1078RequestSubPackageCombiner;
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
public class RequestSubPackageCombinerProps {

    private Type type = Type.CAFFEINE;

    @NestedConfigurationProperty
    private CaffeineJt1078RequestSubPackageCombiner.StorageConfig caffeine = new CaffeineJt1078RequestSubPackageCombiner.StorageConfig();

    public enum Type {
        /**
         * 不合并分包(意味着下游收到的可能是尚未合并的子包)
         */
        NONE,
        /**
         * 基于 caffeine 的分包合并器
         */
        CAFFEINE
    }

}
