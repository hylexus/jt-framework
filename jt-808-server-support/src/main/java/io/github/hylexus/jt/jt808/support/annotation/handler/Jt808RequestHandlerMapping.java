package io.github.hylexus.jt.jt808.support.annotation.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter.Jt808RequestHandlerMappingHandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.Jt808RequestHandlerMappingHandlerMapping;

import java.lang.annotation.*;

/**
 * @author hylexus
 * @see Jt808RequestHandlerMappingHandlerMapping
 * @see Jt808RequestHandlerMappingHandlerAdapter
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestHandlerMapping {

    /**
     * @return 当前处理器方法能处理的消息类型
     * @see Jt808Request#msgType()
     */
    int[] msgType();

    /**
     * @return 当前处理器能处理的消息对应的版本
     * @see Jt808Request#version()
     */
    Jt808ProtocolVersion[] versions() default {Jt808ProtocolVersion.AUTO_DETECTION};

    String desc() default "";

}
