package io.github.hylexus.jt808.server.config;

import io.github.hylexus.jt808.server.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.server.netty.NettyChildHandlerInitializer;
import io.github.hylexus.jt808.server.netty.NettyTcpServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 * Created At 2019-08-19 22:44
 */
@Configuration
public class Jt808ServerConfig {

    @Bean
    public NettyTcpServer jt808NettyTcpServer() {
        NettyTcpServer server = new NettyTcpServer("808-tcp-server", new NettyChildHandlerInitializer(new Jt808ChannelHandlerAdapter()));
        server.setPort(1234);
        server.setBossThreadCount(2);
        server.setWorkThreadCount(4);
        return server;
    }
}
