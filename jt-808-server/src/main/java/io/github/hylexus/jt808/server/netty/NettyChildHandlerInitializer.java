package io.github.hylexus.jt808.server.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import static io.github.hylexus.jt.config.JtProtocolConstant.MAX_PACKAGE_LENGTH;
import static io.github.hylexus.jt.config.JtProtocolConstant.PACKAGE_DELIMITER;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
public class NettyChildHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter;

    public NettyChildHandlerInitializer(Jt808ChannelHandlerAdapter msgDispatcher) {
        this.jt808ChannelHandlerAdapter = msgDispatcher;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(
                new DelimiterBasedFrameDecoder(MAX_PACKAGE_LENGTH, Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})));
        ch.pipeline().addLast(jt808ChannelHandlerAdapter);
    }
}
