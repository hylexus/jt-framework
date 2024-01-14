package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.utils.AbstractRunner;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

@Slf4j
@Setter
@Getter
public class Jt808NettyTcpAttachmentServer extends AbstractRunner {

    private final Jt808AttachmentServerNettyConfigure serverConfigure;
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private Integer port;
    private Integer workThreadCount;
    private Integer bossThreadCount;

    public Jt808NettyTcpAttachmentServer(String name, Jt808AttachmentServerNettyConfigure serverConfigure) {
        super(name);
        this.serverConfigure = serverConfigure;
    }

    private void bind() throws Exception {
        this.bossGroup = new NioEventLoopGroup(bossThreadCount);
        this.workerGroup = new NioEventLoopGroup(workThreadCount);
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(@NonNull SocketChannel ch) {
                        serverConfigure.configureSocketChannel(ch);
                    }
                });

        serverConfigure.configureServerBootstrap(serverBootstrap);

        log.info("----> netty tcp attachment server started, port = {}", this.port);
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