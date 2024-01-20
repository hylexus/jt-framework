package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.netty.Jt808DispatchChannelHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * <h2>1. 作用</h2>
 * <ol>
 *     <li>监听当前请求处理流程中的每个关键步骤</li>
 *     <li>也具有常规意义上的 {@code Filter} 的功能: 可以在请求符合某些业务逻辑的情况下终止当前请求的处理
 *     (比如当前请求的终端IP在黑名单中时，你可以选择直接终止当前请的处理)</li>
 * </ol>
 * <p>
 *
 * <h2>2. 注意事项</h2>
 * <p>
 * 一般情况下:<br/>
 *
 * <p color="#F90043">
 * 该接口的实现类的每个方法都应该返回 {@code true}。<br/>
 * 返回 {@code false} 意味着你想终止当前请求的处理流程。
 * </p>
 * <br/>
 *
 * <h2>3. 流程</h2>
 * <img src="doc-files/jt-808-request-lifecycle-listener.png"/>
 * <p>
 * <br/>
 *
 * <h2>4. 使用示例</h2>
 * <p>
 * 这里是一个使用 Prometheus 统计请求次数的示例：
 * <br/>
 *
 * <pre>{@code
 * @Component
 * public class PrometheusMetricsExporter implements Jt808RequestLifecycleListener {
 *
 *     private final MeterRegistry meterRegistry;
 *
 *     public PrometheusMetricsExporter(MeterRegistry meterRegistry) {
 *         this.meterRegistry = meterRegistry;
 *     }
 *
 *     @Override
 *     public boolean beforeDispatch(Jt808Request request) {
 *         Counter.builder("jt808_request_total_count")
 *                 .description("A counter for JT/T 808 request processed by current server")
 *                 .tags(
 *                         "terminal_id", request.terminalId(),
 *                         "version", request.version().name().toLowerCase()
 *                 )
 *                 .register(this.meterRegistry)
 *                 .increment();
 *
 *         return true;
 *     }
 * }}</pre>
 *
 * @author hylexus
 * @see Jt808DispatchChannelHandlerAdapter#channelRead(ChannelHandlerContext, Object)
 * @see Jt808RequestMsgDispatcher#doDispatch(Jt808Request)
 * @see Jt808DispatcherHandler#handleRequest(Jt808ServerExchange)
 * @see Jt808HandlerResultHandler#handleResult(Jt808ServerExchange, Jt808HandlerResult)
 * @see io.github.hylexus.jt.jt808.spec.impl.Jt808RequestLifecycleListeners
 * @since 2.0.2
 */
public interface Jt808RequestLifecycleListener extends OrderedComponent {

    /**
     * @param request 尚未解码的原始报文
     * @param channel 当前终端对应的 {@link Channel}
     * @see Jt808DispatchChannelHandlerAdapter#channelRead(ChannelHandlerContext, Object)
     */
    default boolean beforeDecode(ByteBuf request, Channel channel) {
        return true;
    }

    /**
     * @since 2.1.4
     */
    default boolean beforeDecode(ByteBuf request, Channel channel, Jt808Session.Role role) {
        return beforeDecode(request, channel);
    }

    /**
     * @param request 初步解码后的请求(请求体尚未解码)
     * @see Jt808RequestMsgDispatcher#doDispatch(Jt808Request)
     */
    default boolean beforeDispatch(Jt808Request request) {
        return true;
    }

    /**
     * @param exchange 请求上下文
     * @see Jt808DispatcherHandler#handleRequest(Jt808ServerExchange)
     */
    default boolean beforeDispatch(Jt808ServerExchange exchange) {
        return true;
    }

    /**
     * @param exchange 请求上下文
     * @param handler  处理当前请求的处理器。
     *                 目前内置的处理器有两种:
     *                 <ol>
     *                 <li>被 {@link io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler @Jt808RequestHandler}</li> 注解标记的处理器方法
     *                 <li>{@link io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler SimpleJt808RequestHandler}</li> 实例
     *                 </ol>
     * @see Jt808DispatcherHandler#handleRequest(io.github.hylexus.jt.jt808.spec.Jt808ServerExchange)
     */
    default boolean beforeHandle(Jt808ServerExchange exchange, Object handler) {
        return true;
    }

    /**
     * @param exchange      请求上下文
     * @param handlerResult 请求处理器的处理结果
     * @see Jt808HandlerResultHandler#handleResult(Jt808ServerExchange, Jt808HandlerResult)
     */
    default boolean beforeEncode(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult) {
        return true;
    }

    /**
     * @param exchange      请求上下文
     * @param handlerResult 请求处理器的处理结果
     * @param response      回复给客户端的报文
     * @see Jt808HandlerResultHandler#handleResult(Jt808ServerExchange, Jt808HandlerResult)
     */
    default boolean beforeResponse(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult, ByteBuf response) {
        return true;
    }
}
