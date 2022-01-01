package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;

import javax.annotation.Nullable;

/**
 * 该接口是从<a
 * href="https://github.com/spring-projects/spring-framework/blob/v5.3.14/spring-webmvc/src/main/java/org/springframework/web/servlet/HandlerInterceptor.java">
 * SpringMvc-HandlerInterceptor</a> 借鉴(抄袭)过来的。
 *
 * @author hylexus
 */
public interface Jt808HandlerInterceptor extends OrderedComponent {

    /**
     * 调用处理器方法之前回调
     *
     * @param exchange 当前请求上下文
     * @param handler  处理当前消息的处理器,内置处理器有两种:
     *                 <ol>
     *                      <li>{@link SimpleJt808RequestHandler SimpleJt808RequestHandler} 的实现类</li>
     *                      <li>被 {@link Jt808RequestHandlerMapping @Jt808RequestHandlerMapping} 标记的方法</li>
     *                 </ol>
     * @return true: 会正常调用处理器方法处理消息; false: 直接返回,不再调用处理器方法
     */
    default boolean preHandle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        return true;
    }

    /**
     * 处理器执行结束之后回调
     *
     * @param exchange      当前请求上下文
     * @param handler       处理当前消息的处理器,内置处理器有两种:
     *                      <ol>
     *                           <li>{@link SimpleJt808RequestHandler SimpleJt808RequestHandler} 的实现类</li>
     *                           <li>被 {@link Jt808RequestHandlerMapping @Jt808RequestHandlerMapping} 标记的方法</li>
     *                      </ol>
     * @param handlerResult 处理器处理结果
     */
    default void postHandle(Jt808ServerExchange exchange, Object handler, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
    }

    /**
     * @param exchange  当前请求上下文
     * @param handler   处理当前消息的处理器,内置处理器有两种:
     *                  <ol>
     *                       <li>{@link SimpleJt808RequestHandler SimpleJt808RequestHandler} 的实现类</li>
     *                       <li>被 {@link Jt808RequestHandlerMapping @Jt808RequestHandlerMapping} 标记的方法</li>
     *                  </ol>
     * @param exception 处理器处理消息时候出现的异常(@Nullable)
     */
    default void afterCompletion(Jt808ServerExchange exchange, Object handler, @Nullable Throwable exception) throws Throwable {
    }

}
