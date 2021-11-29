package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.response.impl.DefaultJt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808ReqMsgHandler;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author hylexus
 */
@Component
public class SimpleLocationInfoUploadMsgHandler implements Jt808ReqMsgHandler<DefaultJt808Response> {
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Set.of(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Set.of(Jt808ProtocolVersion.VERSION_2019);
    }

    @Override
    public DefaultJt808Response handleMsg(Jt808Request request, Jt808Session session) {

        final Jt808ByteBuf jt808ByteBuf = new Jt808ByteBuf(ByteBufAllocator.DEFAULT.buffer())
                .writeWord(request.flowId())
                .writeWord(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId());
        jt808ByteBuf.writeByte(0);
        return new DefaultJt808Response(BuiltinJt808MsgType.SERVER_COMMON_REPLY, jt808ByteBuf, session);
    }
}
