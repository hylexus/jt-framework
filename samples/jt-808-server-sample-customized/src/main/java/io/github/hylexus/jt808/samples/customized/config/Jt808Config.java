package io.github.hylexus.jt808.samples.customized.config;

import io.github.hylexus.jt.exception.MsgEscapeException;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.samples.customized.converter.LocationUploadMsgBodyConverter2;
import io.github.hylexus.jt808.samples.customized.handler.LocationInfoUploadMsgHandler;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import io.github.hylexus.jt808.support.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 * Created At 2019-09-22 3:43 下午
 */
@Slf4j
@Configuration
public class Jt808Config extends Jt808ServerConfigure {

    @Override
    public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
        super.configureServerBootstrap(serverBootstrap);
    }

    @Override
    public void configureSocketChannel(SocketChannel ch, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        super.configureSocketChannel(ch, jt808ChannelHandlerAdapter);
    }

    @Override
    public void configureMsgConverterMapping(RequestMsgBodyConverterMapping mapping) {
        super.configureMsgConverterMapping(mapping);
        mapping.registerConverter(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationUploadMsgBodyConverter2());
    }

    @Autowired
    private LocationInfoUploadMsgHandler locationInfoUploadMsgHandler;

    @Override
    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
        super.configureMsgHandlerMapping(mapping);
        mapping.registerHandler(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, locationInfoUploadMsgHandler);
    }

    @Override
    public BytesEncoder supplyBytesEncoder() {
        return new BytesEncoder() {

            private final BytesEncoder bytesEncoder = new BytesEncoder.DefaultBytesEncoder();

            @Override
            public byte[] doEscapeForReceive(byte[] bytes, int start, int end) throws MsgEscapeException {
                return bytesEncoder.doEscapeForReceive(bytes, start, end);
            }

            @Override
            public byte[] doEscapeForSend(byte[] bytes, int start, int end) throws MsgEscapeException {
                return bytesEncoder.doEscapeForSend(bytes, start, end);
            }
        };
    }

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

    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return new Jt808MsgTypeParser();
    }

}
