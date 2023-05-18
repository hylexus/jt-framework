package io.github.hylexus.jt.jt808.boot.props.feature.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Jt808RequestFilterProps {

    /**
     * 是否启用 {@link io.github.hylexus.jt.jt808.spec.Jt808RequestFilter} 的功能
     *
     * @since 2.1.1
     */
    private boolean enabled = false;

}
