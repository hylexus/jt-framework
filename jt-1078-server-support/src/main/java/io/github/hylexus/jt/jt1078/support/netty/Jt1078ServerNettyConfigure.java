package io.github.hylexus.jt.jt1078.support.netty;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;

import java.util.function.Consumer;

/**
 * @author hylexus
 */
public interface Jt1078ServerNettyConfigure extends JtServerNettyConfigure {

    @BuiltinComponent
    class DefaultJt1078ServerNettyConfigure implements Jt1078ServerNettyConfigure {

        private final Consumer<ServerBootstrap> serverBootstrapConfigure;
        private final Consumer<SocketChannel> socketChannelConfigure;

        public DefaultJt1078ServerNettyConfigure(Consumer<ServerBootstrap> serverBootstrapConfigure, Consumer<SocketChannel> socketChannelConfigure) {
            this.serverBootstrapConfigure = serverBootstrapConfigure;
            this.socketChannelConfigure = socketChannelConfigure;
        }

        @Override
        public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
            this.serverBootstrapConfigure.accept(serverBootstrap);
        }

        @Override
        public void configureSocketChannel(SocketChannel ch) {
            this.socketChannelConfigure.accept(ch);
        }
    }
}
