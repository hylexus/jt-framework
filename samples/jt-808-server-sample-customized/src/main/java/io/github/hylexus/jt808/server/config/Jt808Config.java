package io.github.hylexus.jt808.server.config;

import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.server.handler.LocationInfoUploadMsgHandler;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 * Created At 2019-09-22 3:43 下午
 */
@Configuration
public class Jt808Config extends Jt808ServerConfigure {

    @Override
    public void configureMsgConverterMapping(MsgConverterMapping mapping) {
        super.configureMsgConverterMapping(mapping);
    }

    @Override
    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
        super.configureMsgHandlerMapping(mapping);
        mapping.registerHandler(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationInfoUploadMsgHandler());
    }

    @Override
    public AuthCodeValidator supplyAuthCodeValidator() {
        return (session, requestMsgMetadata, authRequestMsgBody) -> true;
    }

    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return new Jt808MsgTypeParser();
    }

}
