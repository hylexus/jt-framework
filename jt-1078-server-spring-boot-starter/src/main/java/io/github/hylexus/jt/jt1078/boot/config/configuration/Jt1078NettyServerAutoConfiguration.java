package io.github.hylexus.jt.jt1078.boot.config.configuration;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestMsgDispatcher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.DefaultJt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.impl.DefaultJt1078RequestMsgDispatcher;
import io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078CollectorFactory;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.codec.impl.DefaultJt1078CollectorFactory;
import io.github.hylexus.jt.jt1078.support.codec.impl.DefaultJt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler;
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
    @ConditionalOnMissingBean
    public Jt1078MsgDecoder jt1078MsgDecoder() {
        return new DefaultJt1078MsgDecoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078SessionManager jt1078SessionManager() {
        return new DefaultJt1078SessionManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078RequestMsgDispatcher jt1078RequestMsgDispatcher(Jt1078SessionManager sessionManager) {
        return new DefaultJt1078RequestMsgDispatcher(sessionManager);
    }

    @Bean
    // TODO ConditionalOnMissingBean
    public Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler(
            Jt1078MsgDecoder jt1078MsgDecoder,
            Jt1078SessionManager jt1078SessionManager,
            Jt1078RequestMsgDispatcher jt1078RequestMsgDispatcher) {
        return new Jt1078DispatcherChannelHandler(jt1078MsgDecoder, jt1078SessionManager, jt1078RequestMsgDispatcher);
    }

    @Bean
    @ConditionalOnMissingBean(Jt1078ServerNettyConfigure.class)
    public Jt1078ServerNettyConfigure jt808ServerNettyConfigure(Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler) {
        return new Jt1078ServerNettyConfigure.DefaultJt1078ServerNettyConfigure(jt1078DispatcherChannelHandler);
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

    @Bean
    public Jt1078CollectorFactory jt1078CollectorFactory() {
        return new DefaultJt1078CollectorFactory();
    }

    @Bean
    public Jt1078PublisherManager jt1078PublisherManager(Jt1078SessionManager sessionManager, Jt1078CollectorFactory collectorFactory) {
        return new DefaultJt1078PublisherManager(sessionManager, collectorFactory);
    }
}
