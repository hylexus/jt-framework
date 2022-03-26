package io.github.hylexus.jt808.samples.customized.config;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGeneratorFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.*;
import io.github.hylexus.jt.jt808.support.codec.impl.*;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.netty.Jt808DispatchChannelHandlerAdapter;
import io.github.hylexus.jt.jt808.support.netty.Jt808ServerNettyConfigure;
import io.github.hylexus.jt.jt808.support.netty.Jt808TerminalHeatBeatHandler;
import io.github.hylexus.jt808.samples.customized.session.MyJt808SessionEventListener01;
import io.github.hylexus.jt808.samples.customized.session.MyJt808SessionEventListener02;
import io.github.hylexus.jt808.samples.customized.session.MySessionManager;
import io.github.hylexus.jt808.samples.customized.subpackage.MyRequestSubPackageStorage;
import io.github.hylexus.jt808.samples.customized.subpackage.MyResponseSubPackageStorage;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;

/**
 * @author hylexus
 */
@Configuration
public class MyJt808Config {
    // [[ 必须配置 ]] -- 提供自定义的 Jt808MsgTypeParser
    @Bean
    public Jt808MsgTypeParser jt808MsgTypeParser() {
        // 优先使用自定义类型解析
        return msgId -> MyMsgType.CLIENT_AUTH.parseFromInt(msgId)
                // 使用内置类型解析
                .or(() -> BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgId));
    }

    // [[ 非必须配置 ]] -- 替换内置响应消息分包暂存器
    @Bean
    public Jt808ResponseSubPackageStorage myJt808ResponseSubPackageStorage() {
        return new MyResponseSubPackageStorage(new CaffeineJt808ResponseSubPackageStorage.StorageConfig());
    }

    // [[ 非必须配置 ]] -- 替换内置请求消息分包暂存器
    @Bean
    public Jt808RequestSubPackageStorage myJt808RequestSubPackageStorage() {
        return new MyRequestSubPackageStorage(ByteBufAllocator.DEFAULT, new CaffeineJt808RequestSubPackageStorage.StorageConfig());
    }

    // [[ 非必须配置 ]] -- 替换流水号生成策略
    @Bean
    public Jt808FlowIdGeneratorFactory jt808FlowIdGeneratorFactory() {
        return new Jt808FlowIdGeneratorFactory.DefaultJt808FlowIdGeneratorFactory();
    }

    // [[ 非必须配置 ]] -- Session事件监听器 (可以有多个)
    @Bean
    public Jt808SessionEventListener listener1() {
        return new MyJt808SessionEventListener01();
    }

    // [[ 非必须配置 ]] -- Session事件监听器 (可以有多个)
    @Bean
    public Jt808SessionEventListener listener2() {
        return new MyJt808SessionEventListener02();
    }

    // [[ 非必须配置 ]] -- 替换内置的 Jt808MsgEncoder
    @Bean
    public Jt808MsgEncoder jt808MsgEncoder(
            Jt808MsgBytesProcessor processor, Jt808ResponseSubPackageEventListener listener,
            Jt808ResponseSubPackageStorage subPackageStorage) {
        return new DefaultJt808MsgEncoder(ByteBufAllocator.DEFAULT, processor, listener, subPackageStorage);
    }

    // [[ 非必须配置 ]] -- 替换内置的 Jt808MsgDecoder
    @Bean
    public Jt808MsgDecoder jt808MsgDecoder(
            Jt808MsgTypeParser jt808MsgTypeParser,
            Jt808MsgBytesProcessor bytesProcessor,
            Jt808ProtocolVersionDetectorRegistry registry) {
        return new DefaultJt808MsgDecoder(jt808MsgTypeParser, bytesProcessor, registry);
    }

    // [[ 非必须配置 ]] -- 替换内置的 Jt808SessionManager
    @Bean
    public Jt808SessionManager jt808SessionManager(ObjectProvider<Jt808SessionEventListener> listeners, Jt808FlowIdGeneratorFactory factory) {
        final Jt808SessionManager sessionManager = MySessionManager.getInstance(factory);
        listeners.stream().sorted(Comparator.comparing(OrderedComponent::getOrder))
                .forEach(sessionManager::addListener);
        return sessionManager;
    }

    // [[ 非必须配置 ]] -- 替换内置的 Netty 配置类
    @Bean
    public Jt808ServerNettyConfigure jt808ServerNettyConfigure(
            EventExecutorGroup eventExecutorGroup,
            Jt808ServerProps serverProps,
            Jt808TerminalHeatBeatHandler heatBeatHandler,
            Jt808DispatchChannelHandlerAdapter channelHandlerAdapter) {

        final var idleStateHandler = serverProps.getServer().getIdleStateHandler();
        final var serverBootstrapProps = new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.BuiltInServerBootstrapProps(
                serverProps.getProtocol().getMaxFrameLength(),
                new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.IdleStateHandlerProps(
                        idleStateHandler.isEnabled(),
                        idleStateHandler.getReaderIdleTime(),
                        idleStateHandler.getWriterIdleTime(),
                        idleStateHandler.getAllIdleTime()
                )
        );
        return new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure(serverBootstrapProps, heatBeatHandler, channelHandlerAdapter, eventExecutorGroup);
    }

    // [[ 非必须配置 ]] -- 替换内置的转义等逻辑
    @Bean
    public Jt808MsgBytesProcessor jt808MsgBytesProcessor() {
        return new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT);
    }
}
