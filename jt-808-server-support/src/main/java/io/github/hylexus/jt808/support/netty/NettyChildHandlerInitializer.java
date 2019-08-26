package io.github.hylexus.jt808.support.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

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
        ch.pipeline().addLast(new IdleStateHandler(3, 0, 0, TimeUnit.MINUTES));
        ch.pipeline().addLast(new HeatBeatHandler());
        ch.pipeline().addLast(
                new DelimiterBasedFrameDecoder(
                        MAX_PACKAGE_LENGTH,
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})
                )
        );
        ch.pipeline().addLast(jt808ChannelHandlerAdapter);
    }
}
