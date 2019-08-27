package io.github.hylexus.jt808.boot.config;

import io.github.hylexus.jt808.boot.props.Jt808NettyTcpServerProps;
import io.github.hylexus.jt808.converter.BuiltinMsgTypeParser;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.impl.AuthMsgConverter;
import io.github.hylexus.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt808.dispatcher.impl.LocalEventBusDispatcher;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.handler.impl.AuthMsgHandler;
import io.github.hylexus.jt808.handler.impl.HeartBeatMsgHandler;
import io.github.hylexus.jt808.handler.impl.NoReplyMsgHandler;
import io.github.hylexus.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.queue.impl.LocalEventBus;
import io.github.hylexus.jt808.queue.listener.LocalEventBusListener;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.support.netty.Jt808NettyTcpServer;
import io.github.hylexus.jt808.support.netty.NettyChildHandlerInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2019-08-26 9:14 下午
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({Jt808NettyTcpServerProps.class})
public class Jt808ServerAutoConfigure implements InitializingBean {

    public static final AnsiColor BUILTIN_COMPONENT_COLOR = AnsiColor.BRIGHT_CYAN;
    public static final AnsiColor CUSTOM_COMPONENT_COLOR = AnsiColor.GREEN;
    public static final AnsiColor DEPRECATED_COMPONENT_COLOR = AnsiColor.RED;

    @Autowired(required = false)
    private Jt808ServerConfigure jt808ServerConfigure;

    @Autowired
    private Jt808NettyTcpServerProps jt808NettyTcpServerProps;

    @Bean
    @ConditionalOnMissingBean(AuthCodeValidator.class)
    public AuthCodeValidator authCodeValidator() {
        autoConfigLog("auto config --> AuthCodeValidator");
        warning(AuthCodeValidator.BuiltinAuthCodeValidatorForDebugging.class, AuthCodeValidator.class);
        return new AuthCodeValidator.BuiltinAuthCodeValidatorForDebugging();
    }

    @Bean
    public MsgHandlerMapping msgHandlerMapping() {
        autoConfigLog("auto config --> MsgHandlerMapping");
        MsgHandlerMapping mapping = new MsgHandlerMapping();
        jt808ServerConfigure.configureMsgHandlerMapping(mapping);
        mapping.registerHandler(new AuthMsgHandler(authCodeValidator()))
                .registerHandler(new HeartBeatMsgHandler())
                .registerHandler(new NoReplyMsgHandler())
        ;
        return mapping;
    }


    @Bean
    public MsgConverterMapping msgConverterMapping() {
        autoConfigLog("auto config --> MsgConverterMapping");
        MsgConverterMapping mapping = new MsgConverterMapping();
        this.jt808ServerConfigure.configureMsgConverterMapping(mapping);
        mapping.registerConverter(BuiltinMsgType.CLIENT_AUTH, new AuthMsgConverter());
        return mapping;
    }

    @Bean
    @ConditionalOnMissingBean(RequestMsgQueue.class)
    public RequestMsgQueue requestMsgQueue() {
        autoConfigLog("auto config --> RequestMsgQueue");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 10, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadPoolExecutor.AbortPolicy()
        );
        return new LocalEventBus(executor);
    }

    @Bean
    @ConditionalOnMissingBean(RequestMsgDispatcher.class)
    public RequestMsgDispatcher requestMsgDispatcher() {
        autoConfigLog("auto config --> RequestMsgDispatcher");
        return new LocalEventBusDispatcher(msgConverterMapping(), requestMsgQueue());
    }

    @Bean
    @ConditionalOnMissingBean(RequestMsgQueueListener.class)
    public RequestMsgQueueListener msgQueueListener() {
        autoConfigLog("auto config --> RequestMsgQueueListener");
        return new LocalEventBusListener(msgHandlerMapping(), (LocalEventBus) requestMsgQueue());
    }

    @Bean
    @ConditionalOnMissingBean(MsgTypeParser.class)
    public MsgTypeParser msgTypeParser() {
        autoConfigLog("auto config --> MsgTypeParser");
        return new BuiltinMsgTypeParser();
    }

    @Bean
    @ConditionalOnMissingBean(Jt808ChannelHandlerAdapter.class)
    public Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter() {
        autoConfigLog("auto config --> Jt808ChannelHandlerAdapter");
        return new Jt808ChannelHandlerAdapter(requestMsgDispatcher(), msgTypeParser());
    }

    @Bean
    @ConditionalOnMissingBean(name = "jt808NettyTcpServer")
    public Jt808NettyTcpServer jt808NettyTcpServer() {
        autoConfigLog("auto config --> jt808NettyTcpServer");
        Jt808NettyTcpServer server = new Jt808NettyTcpServer("808-tcp-server",
                new NettyChildHandlerInitializer(jt808ChannelHandlerAdapter()));

        server.setPort(jt808NettyTcpServerProps.getPort());
        server.setBossThreadCount(jt808NettyTcpServerProps.getBossThreadCount());
        server.setWorkThreadCount(jt808NettyTcpServerProps.getWorkerThreadCount());
        return server;
    }

    private void autoConfigLog(String content, Object... args) {
        log.info(tips(BUILTIN_COMPONENT_COLOR, content), args);
    }

    private void warning(Class<?> actualCls, Class<?> superClass) {
        String content = ">|< -- Using [{}], please consider to provide your own implementation of [{}]";
        log.warn(tips(DEPRECATED_COMPONENT_COLOR, content), actualCls.getSimpleName(), superClass.getSimpleName());
    }

    private String tips(AnsiColor color, String content) {
        return AnsiOutput.toString(color, content, AnsiColor.DEFAULT);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.jt808ServerConfigure == null) {
            // NoOps
            this.jt808ServerConfigure = new Jt808ServerConfigure.BuiltinNoOpsConfigure();
        }
    }
}
