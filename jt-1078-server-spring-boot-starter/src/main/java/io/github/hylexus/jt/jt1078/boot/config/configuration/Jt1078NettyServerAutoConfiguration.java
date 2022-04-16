package io.github.hylexus.jt.jt1078.boot.config.configuration;

import io.github.hylexus.jt.jt1078.support.netty.Jt1078NettyChildHandlerInitializer;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078NettyTcpServer;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078ServerNettyConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author hylexus
 */
// TODO configurable
public class Jt1078NettyServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Jt1078ServerNettyConfigure.class)
    public Jt1078ServerNettyConfigure jt808ServerNettyConfigure() {
        return new Jt1078ServerNettyConfigure.DefaultJt1078ServerNettyConfigure();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078NettyTcpServer jt808NettyTcpServer(Jt1078ServerNettyConfigure configure) {
        final Jt1078NettyTcpServer server = new Jt1078NettyTcpServer(
                "1078-tcp-server",
                configure,
                new Jt1078NettyChildHandlerInitializer(configure)
        );

        server.setPort(61078);
        server.setBossThreadCount(1);
        server.setWorkThreadCount(2);
        return server;
    }

}
