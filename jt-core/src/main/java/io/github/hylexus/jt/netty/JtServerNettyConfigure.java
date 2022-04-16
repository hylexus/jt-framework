package io.github.hylexus.jt.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;

/**
 * @author hylexus
 */
public interface JtServerNettyConfigure {

    void configureServerBootstrap(ServerBootstrap serverBootstrap);

    void configureSocketChannel(SocketChannel ch);

}