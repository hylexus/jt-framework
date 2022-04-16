package io.github.hylexus.jt.jt1078.support.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import javax.annotation.Nonnull;

/**
 * @author hylexus
 */
public class Jt1078NettyChildHandlerInitializer extends ChannelInitializer<SocketChannel> {
    private final Jt1078ServerNettyConfigure configure;

    public Jt1078NettyChildHandlerInitializer(Jt1078ServerNettyConfigure configure) {
        this.configure = configure;
    }

    @Override
    protected void initChannel(@Nonnull SocketChannel socketChannel) throws Exception {
        this.configure.configureSocketChannel(socketChannel);
    }
}
