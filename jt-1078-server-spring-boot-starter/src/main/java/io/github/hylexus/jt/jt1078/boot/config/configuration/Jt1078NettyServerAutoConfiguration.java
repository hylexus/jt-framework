package io.github.hylexus.jt.jt1078.boot.config.configuration;

import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultJt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherInternal;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.codec.impl.DefaultJt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestMsgDispatcher;
import io.github.hylexus.jt.jt1078.support.dispatcher.impl.DefaultJt1078RequestMsgDispatcher;
import io.github.hylexus.jt.jt1078.support.dispatcher.impl.DefaultPublisherBasedJt1078RequestHandler;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078NettyChildHandlerInitializer;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078NettyTcpServer;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078ServerNettyConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

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
    public Jt1078PublisherInternal internalJt1078Publisher() {
        return new DefaultJt1078Publisher();
    }

    @Bean
    public Jt1078RequestHandler defaultSubscriberBasedJt1078RequestHandler(Jt1078PublisherInternal jt1078PublisherManager) {
        return new DefaultPublisherBasedJt1078RequestHandler(jt1078PublisherManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078RequestMsgDispatcher jt1078RequestMsgDispatcher(Jt1078SessionManager sessionManager, List<Jt1078RequestHandler> handlers) {
        return new DefaultJt1078RequestMsgDispatcher(sessionManager, handlers);
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

    @Bean(initMethod = "doStart", destroyMethod = "doStop")
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
