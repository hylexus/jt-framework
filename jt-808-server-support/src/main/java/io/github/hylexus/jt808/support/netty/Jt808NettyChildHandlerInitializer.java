package io.github.hylexus.jt808.support.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
public class Jt808NettyChildHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private final Jt808ServerNettyConfigure configure;

    public Jt808NettyChildHandlerInitializer(Jt808ServerNettyConfigure configure) {
        this.configure = configure;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        configure.configureSocketChannel(ch);
    }
}
