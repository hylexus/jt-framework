package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.core.OrderedComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author hylexus
 */
public interface Jt1078RequestLifecycleListener extends OrderedComponent {

    /**
     * @param request 尚未解码的原始报文
     * @param channel 当前终端对应的 {@link Channel}
     * @see io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler#channelRead(ChannelHandlerContext, Object)
     */
    default boolean beforeDecode(ByteBuf request, Channel channel) {
        return true;
    }

    /**
     * @param request 初步解码后的请求(分包合并之前)
     * @see io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler#channelRead(ChannelHandlerContext, Object)
     */
    default boolean beforeDispatch(Jt1078Request request, Channel channel) {
        return true;
    }

    /**
     * @see io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler#handle(Jt1078Request)
     */
    default boolean beforeHandle(Jt1078Request request) {
        return true;
    }

}
