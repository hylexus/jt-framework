package io.github.hylexus.jt.jt1078.support.netty;

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

/**
 * @author hylexus
 */
@Slf4j
@Setter
@Getter
public class Jt1078NettyTcpServer extends AbstractRunner {

    private final Jt1078ServerNettyConfigure nettyConfigure;
    private final JtServerNettyConfigure.ConfigurationProvider configurationProvider;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    public Jt1078NettyTcpServer(String name, Jt1078ServerNettyConfigure nettyConfigure, JtServerNettyConfigure.ConfigurationProvider configurationProvider) {
        super(name);
        this.nettyConfigure = nettyConfigure;
        this.configurationProvider = configurationProvider;
    }

    private void bind() throws Exception {
        final ServerBootstrap serverBootstrap = this.nettyConfigure
                .configureServerBootstrap(this.configurationProvider, new ServerBootstrap())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        Jt1078NettyTcpServer.this.nettyConfigure.configureSocketChannel(configurationProvider, ch);
                    }
                });

        this.bossGroup = serverBootstrap.config().group();
        this.workerGroup = serverBootstrap.config().childGroup();

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
