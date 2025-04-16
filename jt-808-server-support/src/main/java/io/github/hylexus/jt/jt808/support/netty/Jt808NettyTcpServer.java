package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.github.hylexus.jt.utils.AbstractRunner;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class Jt808NettyTcpServer extends AbstractRunner {

    private final Jt808ServerNettyConfigure serverConfigure;
    private final JtServerNettyConfigure.ConfigurationProvider configurationProvider;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    public Jt808NettyTcpServer(
            String name, Jt808ServerNettyConfigure serverConfigure,
            JtServerNettyConfigure.ConfigurationProvider configurationProvider) {
        super(name);
        this.serverConfigure = serverConfigure;
        this.configurationProvider = configurationProvider;
    }

    private void bind() throws Exception {
        final ServerBootstrap serverBootstrap = this.serverConfigure
                .configureServerBootstrap(this.configurationProvider, new ServerBootstrap())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        Jt808NettyTcpServer.this.serverConfigure.configureSocketChannel(configurationProvider, ch);
                    }
                });

        this.bossGroup = serverBootstrap.config().group();
        this.workerGroup = serverBootstrap.config().childGroup();

        final ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

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
