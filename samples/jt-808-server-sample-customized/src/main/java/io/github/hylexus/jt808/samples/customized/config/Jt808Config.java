package io.github.hylexus.jt808.samples.customized.config;

import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.samples.customized.converter.LocationUploadMsgBodyConverter2;
import io.github.hylexus.jt808.samples.customized.handler.LocationInfoUploadMsgHandler;
import io.github.hylexus.jt808.samples.customized.session.MyJt808SessionManagerEventListener;
import io.github.hylexus.jt808.session.Jt808SessionManagerEventListener;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import io.github.hylexus.jt808.support.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 * Created At 2019-09-22 3:43 下午
 */
@Slf4j
@Configuration
public class Jt808Config extends Jt808ServerConfigure {

    @Autowired
    private LocationInfoUploadMsgHandler locationInfoUploadMsgHandler;

    // [[非必须配置]] -- 替换内置 Jt808SessionManagerEventListener
    @Bean
    public Jt808SessionManagerEventListener listener() {
        return new MyJt808SessionManagerEventListener();
    }

    // [[必须配置]] -- 自定义消息类型解析器
    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return new Jt808MsgTypeParser();
    }

    // [非必须配置] -- 配置TCP相关参数
    @Override
    public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
        super.configureServerBootstrap(serverBootstrap);
    }

    // [非必须配置] -- 配置Netty相关参数
    @Override
    public void configureSocketChannel(SocketChannel ch, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        super.configureSocketChannel(ch, jt808ChannelHandlerAdapter);
    }

    // [非必须配置] -- 手动注册消息转换器
    @Override
    public void configureMsgConverterMapping(RequestMsgBodyConverterMapping mapping) {
        super.configureMsgConverterMapping(mapping);
        mapping.registerConverter(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationUploadMsgBodyConverter2());
    }

    // [非必须配置] -- 手动注册消息处理器
    @Override
    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
        super.configureMsgHandlerMapping(mapping);
        mapping.registerHandler(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, locationInfoUploadMsgHandler);
    }

    // [非必须配置] -- 可替换内置转义逻辑
    @Override
    public BytesEncoder supplyBytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder();
    }

    // [非必须配置] -- 配置鉴权鉴权消息处理器
    @Override
    public AuthCodeValidator supplyAuthCodeValidator() {
        return (session, requestMsgMetadata, authRequestMsgBody) -> {
            final String terminalId = session.getTerminalId();
            final String authCode = authRequestMsgBody.getAuthCode();
            // 从其他服务验证鉴权码是否正确
            log.info("AuthCode validate for terminal : {} with authCode : {}, result: {}", terminalId, authCode, true);
            return true;
        };
    }

}
