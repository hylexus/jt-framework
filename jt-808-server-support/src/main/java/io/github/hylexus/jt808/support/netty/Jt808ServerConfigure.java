package io.github.hylexus.jt808.support.netty;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.BuiltinMsgTypeParser;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.config.JtProtocolConstant.*;

/**
 * @author hylexus
 * Created At 2019-08-27 16:53
 */

@Slf4j
public class Jt808ServerConfigure {

    public void configureMsgConverterMapping(RequestMsgBodyConverterMapping mapping) {
    }

    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
    }

    public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
        serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 2048)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public void configureSocketChannel(SocketChannel ch, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        ch.pipeline().addLast(NETTY_HANDLER_NAME_808_IDLE_STATE, new IdleStateHandler(20, 20, 20, TimeUnit.MINUTES));
        ch.pipeline().addLast(NETTY_HANDLER_NAME_808_HEART_BEAT, new HeatBeatHandler());
        ch.pipeline().addLast(
                NETTY_HANDLER_NAME_808_FRAME,
                new DelimiterBasedFrameDecoder(
                        MAX_PACKAGE_LENGTH,
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})
                )
        );
        ch.pipeline().addLast(NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER, jt808ChannelHandlerAdapter);
    }

    @Bean(name = BEAN_NAME_JT808_BYTES_ENCODER)
    public BytesEncoder supplyBytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder();
    }

    @Bean(name = BEAN_NAME_JT808_AUTH_CODE_VALIDATOR)
    public AuthCodeValidator supplyAuthCodeValidator() {
        return new AuthCodeValidator.BuiltinAuthCodeValidatorForDebugging();
    }

    @Bean(name = BEAN_NAME_JT808_REQ_MSG_TYPE_PARSER)
    public MsgTypeParser supplyMsgTypeParser() {
        return new BuiltinMsgTypeParser();
    }


    //    private void warning(Class<?> actualCls, Class<?> superClass) {
    //        String content = ">|< -- Using [{}], please consider to provide your own implementation of [{}]";
    //        log.warn(tips(DEPRECATED_COMPONENT_COLOR, content), actualCls.getSimpleName(), superClass.getSimpleName());
    //    }
    //
    //    private String tips(AnsiColor color, String content) {
    //        return AnsiOutput.toString(color, content, AnsiColor.DEFAULT);
    //    }

    @DebugOnly
    public static final class BuiltinNoOpsConfigure extends Jt808ServerConfigure {
    }

}
