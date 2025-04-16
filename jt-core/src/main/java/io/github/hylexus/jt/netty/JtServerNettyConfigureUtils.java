package io.github.hylexus.jt.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class JtServerNettyConfigureUtils {

    public static Class<? extends ServerChannel> configureChannel(
            ServerBootstrap serverBootstrap,
            boolean preferEpoll,
            int bossThreadCount, int workerThreadCount) {

        final Class<? extends ServerChannel> channelClass;
        if (preferEpoll && Epoll.isAvailable()) {
            channelClass = EpollServerSocketChannel.class;
            serverBootstrap.group(new EpollEventLoopGroup(bossThreadCount), new EpollEventLoopGroup(workerThreadCount))
                    .channel(channelClass);
        } else {
            channelClass = NioServerSocketChannel.class;
            serverBootstrap.group(new NioEventLoopGroup(bossThreadCount), new NioEventLoopGroup(workerThreadCount))
                    .channel(channelClass);
        }
        return channelClass;
    }

}
