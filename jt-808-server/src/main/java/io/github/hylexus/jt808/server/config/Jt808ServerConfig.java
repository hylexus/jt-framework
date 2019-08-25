package io.github.hylexus.jt808.server.config;

import io.github.hylexus.jt808.converter.BuiltinMsgTypeParser;
import io.github.hylexus.jt808.converter.impl.AuthMsgConverter;
import io.github.hylexus.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt808.dispatcher.impl.LocalEventBusDispatcher;
import io.github.hylexus.jt808.handler.impl.AuthMsgHandler;
import io.github.hylexus.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.queue.impl.LocalEventBus;
import io.github.hylexus.jt808.queue.listener.LocalEventBusListener;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.support.netty.NettyChildHandlerInitializer;
import io.github.hylexus.jt808.support.netty.NettyTcpServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2019-08-19 22:44
 */
@Configuration
public class Jt808ServerConfig {

    @Bean
    public MsgHandlerMapping msgHandlerMapping() {
        MsgHandlerMapping mapping = new MsgHandlerMapping();
        mapping.registerHandler(new AuthMsgHandler());
        return mapping;
    }

    @Bean
    public MsgConverterMapping msgConverterMapping() {
        MsgConverterMapping mapping = new MsgConverterMapping();
        mapping.registerConverter(BuiltinMsgType.REQ_CLIENT_AUTH, new AuthMsgConverter());
        return mapping;
    }

    @Bean
    public LocalEventBus requestMsgQueue() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 10, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadPoolExecutor.AbortPolicy()
        );
        return new LocalEventBus(executor);
    }

    @Bean
    public RequestMsgDispatcher asyncEventBus() {
        return new LocalEventBusDispatcher(msgConverterMapping(), requestMsgQueue());
    }

    @Bean
    public RequestMsgQueueListener msgQueueListener() {
        return new LocalEventBusListener(msgHandlerMapping(), requestMsgQueue());
    }

    @Bean
    public Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter() {
        return new Jt808ChannelHandlerAdapter(asyncEventBus(), new BuiltinMsgTypeParser());
    }

    @Bean
    public NettyTcpServer jt808NettyTcpServer() {
        NettyTcpServer server = new NettyTcpServer("808-tcp-server", new NettyChildHandlerInitializer(jt808ChannelHandlerAdapter()));
        server.setPort(1234);
        server.setBossThreadCount(2);
        server.setWorkThreadCount(4);
        return server;
    }

}
