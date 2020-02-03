package io.github.hylexus.jt808.boot.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.boot.props.Jt808NettyTcpServerProps;
import io.github.hylexus.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt808.boot.props.entity.scan.Jt808EntityScanProps;
import io.github.hylexus.jt808.boot.props.handler.scan.Jt808HandlerScanProps;
import io.github.hylexus.jt808.boot.props.processor.MsgProcessorThreadPoolProps;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.BuiltinMsgTypeParser;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.converter.impl.AuthRequestMsgBodyConverter;
import io.github.hylexus.jt808.converter.impl.resp.DelegateRespMsgBodyConverter;
import io.github.hylexus.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt808.dispatcher.impl.LocalEventBusDispatcher;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.handler.impl.AuthMsgHandler;
import io.github.hylexus.jt808.handler.impl.HeartBeatMsgHandler;
import io.github.hylexus.jt808.handler.impl.NoReplyMsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl.DelegateHandlerMethodArgumentResolvers;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.queue.impl.LocalEventBus;
import io.github.hylexus.jt808.queue.listener.LocalEventBusListener;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.entity.scan.Jt808EntityScanner;
import io.github.hylexus.jt808.support.handler.scan.Jt808MsgHandlerScanner;
import io.github.hylexus.jt808.support.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.support.netty.Jt808NettyChildHandlerInitializer;
import io.github.hylexus.jt808.support.netty.Jt808NettyTcpServer;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.config.JtProtocolConstant.*;

/**
 * @author hylexus
 * Created At 2019-08-26 9:14 下午
 */
@Slf4j
//@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE - 100)
@EnableConfigurationProperties({Jt808ServerProps.class})
public class Jt808ServerAutoConfigure {

    public static final AnsiColor SERVER_BANNER_COLOR = AnsiColor.BRIGHT_BLUE;
    public static final AnsiColor BUILTIN_COMPONENT_COLOR = AnsiColor.BRIGHT_CYAN;
    public static final AnsiColor CUSTOM_COMPONENT_COLOR = AnsiColor.GREEN;
    public static final AnsiColor DEPRECATED_COMPONENT_COLOR = AnsiColor.RED;
    public static final AnsiColor UNKNOWN_COMPONENT_TYPE_COLOR = AnsiColor.BRIGHT_RED;

    @Autowired
    private Jt808ServerProps serverProps;

    @Bean
    @ConditionalOnMissingBean(Jt808ServerConfigure.class)
    public Jt808ServerConfigure jt808NettyTcpServerConfigure() {
        return new Jt808ServerConfigure.BuiltinNoOpsConfigure();
    }

    @Bean(BEAN_NAME_JT808_BYTES_ENCODER)
    @ConditionalOnMissingBean(BytesEncoder.class)
    public BytesEncoder bytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder();
    }

    @Autowired
    private Jt808ServerConfigure configure;

    @Bean
    public MsgHandlerMapping msgHandlerMapping(BytesEncoder bytesEncoder) {
        MsgHandlerMapping mapping = new MsgHandlerMapping(bytesEncoder);
        configure.configureMsgHandlerMapping(mapping);
        // Default handlers for debug
        mapping.registerHandler(new AuthMsgHandler(configure.supplyAuthCodeValidator()))
                .registerHandler(new HeartBeatMsgHandler())
                .registerHandler(new NoReplyMsgHandler())
        ;
        return mapping;
    }

    @Bean
    public MsgConverterMapping msgConverterMapping() {
        MsgConverterMapping mapping = new MsgConverterMapping();
        configure.configureMsgConverterMapping(mapping);
        // Default converters for debug
        mapping.registerConverter(BuiltinJt808MsgType.CLIENT_AUTH, new AuthRequestMsgBodyConverter());
        return mapping;
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808.entity-scan", name = "enabled", havingValue = "true")
    public Jt808EntityScanner jt808EntityScanner(MsgConverterMapping msgConverterMapping) {
        Jt808EntityScanProps entityScan = serverProps.getEntityScan();

        if (entityScan.isEnableBuiltinEntity()) {
            entityScan.getBasePackages().add("io.github.hylexus.jt808.msg.req");
        }
        return new Jt808EntityScanner(entityScan.getBasePackages(), configure.supplyMsgTypeParser(), msgConverterMapping);
    }

    @Bean
    public HandlerMethodArgumentResolver handlerMethodArgumentResolver() {
        // 如果有必要 --> 再提供自定义注册
        return new DelegateHandlerMethodArgumentResolvers();
    }

    @Bean
    public ResponseMsgBodyConverter responseMsgBodyConverter(MsgTypeParser msgTypeParser) {
        // 如果有必要 --> 再提供自定义注册
        return new DelegateRespMsgBodyConverter(msgTypeParser);
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808.handler-scan", name = "enabled", havingValue = "true")
    public Jt808MsgHandlerScanner jt808MsgHandlerScanner(
            MsgHandlerMapping msgHandlerMapping, HandlerMethodArgumentResolver argumentResolver,
            ResponseMsgBodyConverter responseMsgBodyConverter) {

        Jt808HandlerScanProps handlerScan = serverProps.getHandlerScan();

        return new Jt808MsgHandlerScanner(
                handlerScan.getBasePackages(), configure.supplyMsgTypeParser(),
                msgHandlerMapping, argumentResolver, responseMsgBodyConverter
        );
    }

    @Bean(BEAN_NAME_JT808_REQ_MSG_QUEUE)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_QUEUE)
    public RequestMsgQueue requestMsgQueue() {
        MsgProcessorThreadPoolProps poolProps = serverProps.getMsgProcessor().getThreadPool();
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(poolProps.getThreadNameFormat())
                .setDaemon(true)
                .build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                poolProps.getCorePoolSize(),
                poolProps.getMaximumPoolSize(),
                poolProps.getKeepAliveTime().getSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(poolProps.getBlockingQueueSize()),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        return new LocalEventBus(executor);
    }

    @Bean
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_QUEUE_LISTENER)
    public RequestMsgQueueListener msgQueueListener(MsgHandlerMapping msgHandlerMapping, RequestMsgQueue requestMsgQueue) {
        return new LocalEventBusListener(msgHandlerMapping, (LocalEventBus) requestMsgQueue);
    }

    @Bean
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_DISPATCHER)
    public RequestMsgDispatcher requestMsgDispatcher(MsgConverterMapping msgConverterMapping, RequestMsgQueue requestMsgQueue) {
        return new LocalEventBusDispatcher(msgConverterMapping, requestMsgQueue);
    }

    @Bean
    @ConditionalOnMissingBean(Jt808ChannelHandlerAdapter.class)
    public Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter(RequestMsgDispatcher requestMsgDispatcher, BytesEncoder bytesEncoder) {
        return new Jt808ChannelHandlerAdapter(requestMsgDispatcher, configure.supplyMsgTypeParser(), bytesEncoder);
    }

    @Bean
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_NETTY_TCP_SERVER)
    public Jt808NettyTcpServer jt808NettyTcpServer(Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        Jt808NettyTcpServer server = new Jt808NettyTcpServer(
                "808-tcp-server",
                configure,
                new Jt808NettyChildHandlerInitializer(configure, jt808ChannelHandlerAdapter)
        );

        Jt808NettyTcpServerProps nettyProps = serverProps.getServer();
        server.setPort(nettyProps.getPort());
        server.setBossThreadCount(nettyProps.getBossThreadCount());
        server.setWorkThreadCount(nettyProps.getWorkerThreadCount());
        return server;
    }

    @Bean(name = BEAN_NAME_JT808_AUTH_CODE_VALIDATOR)
    @ConditionalOnMissingBean(AuthCodeValidator.class)
    public AuthCodeValidator supplyAuthCodeValidator() {
        return new AuthCodeValidator.BuiltinAuthCodeValidatorForDebugging();
    }

    @Bean(name = BEAN_NAME_JT808_REQ_MSG_TYPE_PARSER)
    @ConditionalOnMissingBean(MsgTypeParser.class)
    public MsgTypeParser supplyMsgTypeParser() {
        return new BuiltinMsgTypeParser();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808", name = "print-component-statistics", havingValue = "true")
    public Jt808ServerComponentStatistics jt808ServerComponentStatistics(
            MsgTypeParser msgTypeParser,
            MsgConverterMapping msgConverterMapping,
            MsgHandlerMapping msgHandlerMapping) {

        return new Jt808ServerComponentStatistics(msgTypeParser, msgConverterMapping, msgHandlerMapping);
    }

}
