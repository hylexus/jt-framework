package io.github.hylexus.jt.jt808.support.annotation.handler;

import io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter.Jt808RequestHandlerMappingHandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.Jt808RequestHandlerMappingHandlerMapping;

import java.lang.annotation.*;

/**
 * @author hylexus
 * @see Jt808RequestHandlerMappingHandlerMapping
 * @see Jt808RequestHandlerMappingHandlerAdapter
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestHandler {
}
