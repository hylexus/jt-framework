package io.github.hylexus.jt.jt808.support.annotation.msg;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/**
 * @author hylexus
 * @since 3.0.0
 */
@ApiStatus.AvailableSince("3.0.0")
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DrivenBy {

    Type value() default Type.DEFAULT;

    enum Type {
        /**
         * jt-framework 默认的编解码器
         */
        DEFAULT,
        /**
         * 基于 xtream-codec-core 提供的编解码器
         *
         * @see <a href="https://hylexus.github.io/xtream-codec/guide/core/annotation-driven/builtin-annotations.html">https://hylexus.github.io/xtream-codec/guide/core/annotation-driven/builtin-annotations.html</a>
         * @see <a href="https://github.com/hylexus/xtream-codec">https://github.com/hylexus/xtream-codec</a>
         */
        XTREAM_CODEC,
    }

}
