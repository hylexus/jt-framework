package io.github.hylexus.jt.jt1078.support.netty;

import io.github.hylexus.jt.utils.AbstractRunner;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j
@Setter
@Getter
public class Jt1078NettyTcpServer extends AbstractRunner {

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private Integer port;
    private Integer workThreadCount;
    private Integer bossThreadCount;

    private final Jt1078ServerNettyConfigure nettyConfigure;
    private final Jt1078NettyChildHandlerInitializer handlerInitializer;

    public Jt1078NettyTcpServer(String name, Jt1078ServerNettyConfigure nettyConfigure, Jt1078NettyChildHandlerInitializer handlerInitializer) {
        super(name);
        this.nettyConfigure = nettyConfigure;
        this.handlerInitializer = handlerInitializer;
    }

    private void bind() throws Exception {
        this.bossGroup = new NioEventLoopGroup(bossThreadCount);
        this.workerGroup = new NioEventLoopGroup(workThreadCount);
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(this.handlerInitializer);

        this.nettyConfigure.configureServerBootstrap(serverBootstrap);

        log.info("----> netty tcp server started, port = {}", this.port);
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

        channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void doProcessBlocked() throws Exception {
        this.bind();
    }

    @Override
    public void onDestroy() throws Exception {
        this.stopServer();
    }

    public synchronized void stopServer() throws Exception {

        Future<?> future = this.workerGroup.shutdownGracefully().await();
        if (!future.isSuccess()) {
            log.error("<---- netty workerGroup cannot be stopped", future.cause());
        }

        future = this.bossGroup.shutdownGracefully().await();
        if (!future.isSuccess()) {
            log.error("<---- netty bossGroup cannot be stopped", future.cause());
        }
    }

}