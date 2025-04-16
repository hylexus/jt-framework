package io.github.hylexus.jt808.samples.customized.config;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.config.BuiltinJt808ServerNettyConfigure;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.DefaultJt808FlowIdGeneratorV2;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGeneratorFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.*;
import io.github.hylexus.jt.jt808.support.codec.impl.*;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.Optional;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;

/**
 * @author hylexus
 */
@Configuration
public class MyJt808Config {
    // [[ 必须配置 ]] -- 提供自定义的 Jt808MsgTypeParser
    @Bean
    public Jt808MsgTypeParser jt808MsgTypeParser() {
        // 优先使用自定义类型解析
        // return msgId -> MyMsgType.CLIENT_AUTH.parseFromInt(msgId)
        //         // 使用内置类型解析
        //         .or(() -> BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgId));

        // 兼容 JDK8
        return msgId -> {
            // 优先使用自定义类型解析
            final Optional<MsgType> msgType = MyMsgType.CLIENT_AUTH.parseFromInt(msgId);
            if (msgType.isPresent()) {
                return msgType;
            }
            // 使用内置类型解析
            return BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgId);
        };
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
        return DefaultJt808FlowIdGeneratorV2::new;
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
        return new DefaultJt808MsgEncoder(ByteBufAllocator.DEFAULT, processor, listener, subPackageStorage, Jt808MsgEncryptionHandler.NO_OPS);
    }

    // [[ 非必须配置 ]] -- 替换内置的 Jt808MsgDecoder
    @Bean
    public Jt808MsgDecoder jt808MsgDecoder(
            Jt808MsgTypeParser jt808MsgTypeParser,
            Jt808MsgBytesProcessor bytesProcessor,
            Jt808ProtocolVersionDetectorRegistry registry) {
        return new DefaultJt808MsgDecoder(jt808MsgTypeParser, bytesProcessor, registry, Jt808MsgEncryptionHandler.NO_OPS);
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
            @Qualifier(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP) EventExecutorGroup eventExecutorGroup,
            Jt808TerminalHeatBeatHandler heatBeatHandler,
            Jt808DispatchChannelHandlerAdapter channelHandlerAdapter,
            Jt808ServerProps serverProps) {

        return new BuiltinJt808ServerNettyConfigure(serverProps, eventExecutorGroup, channelHandlerAdapter, heatBeatHandler);
    }

    // [[ 非必须配置 ]] -- 替换内置的转义等逻辑
    @Bean
    public Jt808MsgBytesProcessor jt808MsgBytesProcessor() {
        return new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT);
    }
}
