package io.github.hylexus.jt808.support.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
public class Jt808NettyChildHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter;
    private Jt808NettyTcpServerConfigure serverConfigure;

    public Jt808NettyChildHandlerInitializer(Jt808NettyTcpServerConfigure serverConfigure, Jt808ChannelHandlerAdapter msgDispatcher) {
        this.serverConfigure = serverConfigure;
        this.jt808ChannelHandlerAdapter = msgDispatcher;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        serverConfigure.configureSocketChannel(ch, jt808ChannelHandlerAdapter);
    }
}
