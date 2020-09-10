package io.github.hylexus.jt808.boot.config;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt808.boot.props.converter.scan.Jt808ConverterScanProps;
import io.github.hylexus.jt808.boot.props.entity.scan.Jt808EntityScanProps;
import io.github.hylexus.jt808.boot.props.exception.handler.scan.Jt808ExceptionHandlerScanProps;
import io.github.hylexus.jt808.boot.props.handler.scan.Jt808HandlerScanProps;
import io.github.hylexus.jt808.boot.props.msg.processor.MsgProcessorThreadPoolProps;
import io.github.hylexus.jt808.boot.props.server.Jt808NettyTcpServerProps;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.codec.Decoder;
import io.github.hylexus.jt808.codec.Encoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.converter.impl.*;
import io.github.hylexus.jt808.converter.impl.resp.DelegateRespMsgBodyConverter;
import io.github.hylexus.jt808.dispatcher.CommandSender;
import io.github.hylexus.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt808.dispatcher.impl.DefaultCommandSender;
import io.github.hylexus.jt808.dispatcher.impl.LocalEventBusDispatcher;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.ext.TerminalValidator;
import io.github.hylexus.jt808.handler.impl.BuiltInNoReplyMsgHandler;
import io.github.hylexus.jt808.handler.impl.BuiltinAuthMsgHandler;
import io.github.hylexus.jt808.handler.impl.BuiltinHeartBeatMsgHandler;
import io.github.hylexus.jt808.handler.impl.exception.DelegateExceptionHandler;
import io.github.hylexus.jt808.handler.impl.reflection.CustomReflectionBasedRequestMsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl.DelegateHandlerMethodArgumentResolvers;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.queue.impl.LocalEventBus;
import io.github.hylexus.jt808.queue.listener.LocalEventBusListener;
import io.github.hylexus.jt808.session.DefaultJt808SessionManagerEventListener;
import io.github.hylexus.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt808.session.Jt808SessionManagerEventListener;
import io.github.hylexus.jt808.session.SessionManager;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.OrderedComponent;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import io.github.hylexus.jt808.support.converter.scan.Jt808MsgConverterScanner;
import io.github.hylexus.jt808.support.entity.scan.Jt808EntityScanner;
import io.github.hylexus.jt808.support.exception.scan.Jt808ExceptionHandlerScanner;
import io.github.hylexus.jt808.support.handler.scan.Jt808MsgHandlerScanner;
import io.github.hylexus.jt808.support.netty.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.config.JtProtocolConstant.*;

/**
 * Created At 2020-07-04 18:25
 *
 * @author hylexus
 */
@Slf4j
@EnableConfigurationProperties({Jt808ServerProps.class})
public abstract class Jt808ServerConfigurationSupport {

    @Autowired
    private Jt808ServerProps serverProps;

    public Jt808ServerConfigurationSupport() {
        log.info("<<< Jt808ServerConfigSupport init ... [{}] >>>", this.getClass());
    }

    public void configureMsgConverterMapping(RequestMsgBodyConverterMapping mapping) {
    }

    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
    }

    @Bean(name = BEAN_NAME_JT808_REQ_MSG_TYPE_PARSER)
    @ConditionalOnMissingBean(MsgTypeParser.class)
    public abstract MsgTypeParser supplyMsgTypeParser();

    @Bean(name = BEAN_NAME_JT808_AUTH_CODE_VALIDATOR)
    public AuthCodeValidator supplyAuthCodeValidator() {
        return new AuthCodeValidator.BuiltinAuthCodeValidatorForDebugging();
    }

    @Bean(BEAN_NAME_JT808_BYTES_ENCODER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_BYTES_ENCODER)
    public BytesEncoder supplyBytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder();
    }

    @Bean(BEAN_NAME_JT808_SESSION_MANAGER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_SESSION_MANAGER)
    public Jt808SessionManager supplyJt808SessionManager() {
        return SessionManager.getInstance();
    }

    @Bean(BEAN_NAME_JT808_SESSION_MANAGER_EVENT_LISTENER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_SESSION_MANAGER_EVENT_LISTENER)
    public Jt808SessionManagerEventListener supplyJt808SessionManagerEventListener() {
        return new DefaultJt808SessionManagerEventListener();
    }

    @Bean
    public Jt808SessionManagerEventListenerSetter listenerSetter(Jt808SessionManager jt808SessionManager, Jt808SessionManagerEventListener listener) {
        return new Jt808SessionManagerEventListenerSetter(jt808SessionManager, listener);
    }

    @Bean(NETTY_HANDLER_NAME_808_DECODE)
    @ConditionalOnMissingBean(name = NETTY_HANDLER_NAME_808_DECODE)
    public Jt808DecodeHandler decodeHandler(BytesEncoder bytesEncoder, Decoder decoder) {
        return new Jt808DecodeHandler(serverProps.getProtocol().getVersion(), bytesEncoder, decoder);
    }

    @Bean(BEAN_NAME_JT808_TERMINAL_VALIDATOR)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_TERMINAL_VALIDATOR)
    public TerminalValidator terminalValidator() {
        return new TerminalValidator.BuiltinTerminalValidatorForDebugging();
    }

    @Bean(NETTY_HANDLER_NAME_808_TERMINAL_VALIDATOR)
    @ConditionalOnMissingBean(name = NETTY_HANDLER_NAME_808_TERMINAL_VALIDATOR)
    public TerminalValidatorHandler terminalValidatorHandler(TerminalValidator terminalValidator) {
        return new TerminalValidatorHandler(terminalValidator);
    }

    @Bean(NETTY_HANDLER_NAME_808_HEART_BEAT)
    @ConditionalOnMissingBean(name = NETTY_HANDLER_NAME_808_HEART_BEAT)
    public HeatBeatHandler heatBeatHandler(Jt808SessionManager jt808SessionManager) {
        return new HeatBeatHandler(jt808SessionManager);
    }

    @Bean
    public Decoder decoder(BytesEncoder bytesEncoder) {
        return new Decoder(bytesEncoder);
    }

    @Bean
    public Encoder encoder(BytesEncoder bytesEncoder) {
        return new Encoder(bytesEncoder);
    }

    @Bean
    public RequestMsgBodyConverterMapping msgConverterMapping() {
        RequestMsgBodyConverterMapping mapping = new RequestMsgBodyConverterMapping();
        this.configureMsgConverterMapping(mapping);

        // Default converters for debug
        if (serverProps.getEntityScan().isRegisterBuiltinRequestMsgConverters()) {
            mapping.registerConverter(BuiltinJt808MsgType.CLIENT_AUTH, new BuiltinAuthRequestMsgBodyConverter())
                    .registerConverter(BuiltinJt808MsgType.CLIENT_COMMON_REPLY, new BuiltinTerminalCommonReplyRequestMsgBodyConverter())
                    .registerConverter(BuiltinJt808MsgType.CLIENT_HEART_BEAT, new BuiltinEmptyRequestMsgBodyConverter())
            ;
        }
        return mapping;
    }

    @Bean
    public MsgHandlerMapping msgHandlerMapping(BytesEncoder bytesEncoder, AuthCodeValidator authCodeValidator) {
        MsgHandlerMapping mapping = new MsgHandlerMapping(bytesEncoder);
        this.configureMsgHandlerMapping(mapping);

        // Default handlers for debug
        if (serverProps.getHandlerScan().isRegisterBuiltinMsgHandlers()) {
            mapping.registerHandler(new BuiltinAuthMsgHandler(authCodeValidator))
                    .registerHandler(new BuiltinHeartBeatMsgHandler())
                    .registerHandler(new BuiltInNoReplyMsgHandler())
            ;
        }
        return mapping;
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808.entity-scan", name = "enabled", havingValue = "true")
    public Jt808EntityScanner jt808EntityScanner(
            RequestMsgBodyConverterMapping msgConverterMapping, MsgTypeParser msgTypeParser,
            Decoder decoder) throws IOException {
        final Jt808EntityScanProps entityScan = serverProps.getEntityScan();

        final Jt808EntityScanner scanner = new Jt808EntityScanner(
                entityScan.getBasePackages(), msgTypeParser, msgConverterMapping,
                new CustomReflectionBasedRequestMsgBodyConverter(decoder)
        );

        if (entityScan.isEnableBuiltinEntity() && entityScan.isRegisterBuiltinRequestMsgConverters()) {
            scanner.doEntityScan(Sets.newHashSet("io.github.hylexus.jt808.msg.req"), new BuiltinReflectionBasedRequestMsgBodyConverter(decoder));
        }
        return scanner;
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808.handler-scan", name = "enabled", havingValue = "true")
    public Jt808MsgHandlerScanner jt808MsgHandlerScanner(
            MsgHandlerMapping msgHandlerMapping, HandlerMethodArgumentResolver argumentResolver,
            ResponseMsgBodyConverter responseMsgBodyConverter,
            DelegateExceptionHandler delegateExceptionHandler, MsgTypeParser msgTypeParser) {

        final Jt808HandlerScanProps handlerScan = serverProps.getHandlerScan();

        return new Jt808MsgHandlerScanner(
                handlerScan.getBasePackages(), msgTypeParser,
                msgHandlerMapping, new CustomReflectionBasedRequestMsgHandler(argumentResolver, responseMsgBodyConverter, delegateExceptionHandler)
        );

    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808.converter-scan", name = "enabled", havingValue = "true")
    public Jt808MsgConverterScanner jt808MsgConverterScanner(MsgTypeParser msgTypeParser,
                                                             RequestMsgBodyConverterMapping requestMsgBodyConverterMapping) {
        final Jt808ConverterScanProps converterScan = serverProps.getConverterScan();
        return new Jt808MsgConverterScanner(converterScan.getBasePackages(), msgTypeParser, requestMsgBodyConverterMapping);
    }

    @Bean
    public DelegateExceptionHandler delegateExceptionHandler() {
        return new DelegateExceptionHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808.exception-handler-scan", name = "enabled", havingValue = "true")
    public Jt808ExceptionHandlerScanner exceptionHandlerScanner(DelegateExceptionHandler exceptionHandler, HandlerMethodArgumentResolver argumentResolver)
            throws IllegalAccessException, IOException, InstantiationException {

        final Jt808ExceptionHandlerScanProps props = serverProps.getExceptionHandlerScan();
        final Jt808ExceptionHandlerScanner scanner = new Jt808ExceptionHandlerScanner(props.getBasePackages(), exceptionHandler, argumentResolver);
        if (props.isRegisterBuiltinExceptionHandlers()) {
            scanner.doScan(Sets.newHashSet("io.github.hylexus.jt808.handler.impl.exception.builtin"), OrderedComponent.BUILTIN_COMPONENT_ORDER);
        }
        return scanner;
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

    @Bean(BEAN_NAME_JT808_COMMAND_SENDER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_COMMAND_SENDER)
    public CommandSender commandSender(ResponseMsgBodyConverter responseMsgBodyConverter, Encoder encoder, Jt808SessionManager jt808SessionManager) {
        return new DefaultCommandSender(responseMsgBodyConverter, encoder, jt808SessionManager);
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
                new LinkedBlockingQueue<>(poolProps.getBlockingQueueSize()),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        return new LocalEventBus(executor);
    }

    @Bean(BEAN_NAME_JT808_REQ_MSG_QUEUE_LISTENER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_QUEUE_LISTENER)
    public RequestMsgQueueListener msgQueueListener(
            MsgHandlerMapping msgHandlerMapping, RequestMsgQueue requestMsgQueue,
            DelegateExceptionHandler exceptionHandler, Encoder encoder, ResponseMsgBodyConverter responseMsgBodyConverter,
            Jt808SessionManager jt808SessionManager) {

        return new LocalEventBusListener(
                msgHandlerMapping, (LocalEventBus) requestMsgQueue,
                exceptionHandler, responseMsgBodyConverter, encoder, jt808SessionManager
        );
    }

    @Bean(BEAN_NAME_JT808_REQ_MSG_DISPATCHER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_DISPATCHER)
    public RequestMsgDispatcher requestMsgDispatcher(RequestMsgBodyConverterMapping msgConverterMapping, RequestMsgQueue requestMsgQueue) {
        return new LocalEventBusDispatcher(msgConverterMapping, requestMsgQueue);
    }

    @Bean
    @ConditionalOnMissingBean(Jt808ChannelHandlerAdapter.class)
    public Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter(
            RequestMsgDispatcher requestMsgDispatcher, Jt808SessionManager jt808SessionManager,
            MsgTypeParser msgTypeParser) {
        return new Jt808ChannelHandlerAdapter(requestMsgDispatcher, msgTypeParser, jt808SessionManager);
    }

    @Bean
    @ConditionalOnMissingBean(Jt808ServerNettyConfigure.class)
    public Jt808ServerNettyConfigure jt808ServerNettyConfigure(HeatBeatHandler heatBeatHandler, Jt808DecodeHandler decodeHandler,
                                                               TerminalValidatorHandler terminalValidatorHandler,
                                                               Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        return new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure(heatBeatHandler, decodeHandler,
                terminalValidatorHandler, jt808ChannelHandlerAdapter);
    }

    @Bean(BEAN_NAME_JT808_NETTY_TCP_SERVER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_NETTY_TCP_SERVER)
    public Jt808NettyTcpServer jt808NettyTcpServer(Jt808ServerNettyConfigure configure) {
        Jt808NettyTcpServer server = new Jt808NettyTcpServer(
                "808-tcp-server",
                configure,
                new Jt808NettyChildHandlerInitializer(configure)
        );

        Jt808NettyTcpServerProps nettyProps = serverProps.getServer();
        server.setPort(nettyProps.getPort());
        server.setBossThreadCount(nettyProps.getBossThreadCount());
        server.setWorkThreadCount(nettyProps.getWorkerThreadCount());
        return server;
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808", name = "print-component-statistics", havingValue = "true")
    public Jt808ServerComponentStatistics jt808ServerComponentStatistics(
            MsgTypeParser msgTypeParser,
            RequestMsgBodyConverterMapping msgConverterMapping,
            MsgHandlerMapping msgHandlerMapping) {

        return new Jt808ServerComponentStatistics(msgTypeParser, msgConverterMapping, msgHandlerMapping);
    }
}
