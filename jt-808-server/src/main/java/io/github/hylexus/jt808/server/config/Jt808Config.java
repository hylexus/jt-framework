package io.github.hylexus.jt808.server.config;

import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.server.handler.LocationInfoUploadMsgHandler;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.netty.Jt808NettyTcpServerConfigure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.hylexus.jt.config.JtProtocolConstant.BEAN_NAME_JT808_REQ_MSG_TYPE_PARSER;

/**
 * @author hylexus
 * Created At 2019-09-22 3:43 下午
 */
@Configuration("jt808NettyTcpServerConfigure")
public class Jt808Config extends Jt808NettyTcpServerConfigure {

    @Override
    public void configureMsgConverterMapping(MsgConverterMapping mapping) {
        super.configureMsgConverterMapping(mapping);
    }

    @Override
    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
        super.configureMsgHandlerMapping(mapping);
        mapping.registerHandler(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationInfoUploadMsgHandler());
    }

    @Bean(name = BEAN_NAME_JT808_REQ_MSG_TYPE_PARSER)
    public MsgTypeParser msgTypeParser() {
        return new Jt808MsgTypeParser();
    }
}
