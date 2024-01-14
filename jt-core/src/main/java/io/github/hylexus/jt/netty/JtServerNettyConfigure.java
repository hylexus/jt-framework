package io.github.hylexus.jt.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;

/**
 * @author hylexus
 */
public interface JtServerNettyConfigure {

    default void configureServerBootstrap(ServerBootstrap serverBootstrap) {
        serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 2048)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    void configureSocketChannel(SocketChannel ch);

}