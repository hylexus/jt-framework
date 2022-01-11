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
public class RequestSubPackageStorageProps extends Switchable {

    private Type type = Type.CAFFEINE;

    @NestedConfigurationProperty
    private CaffeineJt808RequestSubPackageStorage.RequestSubPackageStorageConfig caffeine =
            new CaffeineJt808RequestSubPackageStorage.RequestSubPackageStorageConfig();

    public enum Type {
        CAFFEINE
    }

}
