package io.github.hylexus.jt808.server.config;

import io.github.hylexus.jt.jt808.converter.BuiltinMsgTypeParser;
import io.github.hylexus.jt.jt808.converter.impl.AuthMsgConverter;
import io.github.hylexus.jt.jt808.dispatcher.LocalDispatcher;
import io.github.hylexus.jt.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt.jt808.support.MsgConverterMapping;
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
    public MsgConverterMapping msgConverterMapping() {
        MsgConverterMapping mapping = new MsgConverterMapping();
        mapping.addConverter(BuiltinMsgType.REQ_CLIENT_AUTH, new AuthMsgConverter());
        return mapping;
    }

    @Bean
    public RequestMsgDispatcher requestMsgDispatcher() {
        return new LocalDispatcher(msgConverterMapping());
    }

    @Bean
    public Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter() {
        return new Jt808ChannelHandlerAdapter(requestMsgDispatcher(), new BuiltinMsgTypeParser());
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
