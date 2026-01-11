package io.github.hylexus.jt.jt808.support.annotation.msg.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/**
 * Created At 2019-09-18 8:47 下午
 *
 * @author hylexus
 * @see io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl.Jt808RequestBodyHandlerMethodArgumentResolver
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestBody {

    /**
     * @since 3.0.0
     */
    @ApiStatus.AvailableSince("3.0.0")
    DrivenBy drivenBy() default @DrivenBy(DrivenBy.Type.DEFAULT);

}

