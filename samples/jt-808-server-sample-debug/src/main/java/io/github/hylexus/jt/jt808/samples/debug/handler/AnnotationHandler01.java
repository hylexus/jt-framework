package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.DemoAuthMsgV2011;
import io.github.hylexus.jt.jt808.samples.debug.entity.resp.DemoServerCommonReplyRespMsg;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandlerMapping;
import org.springframework.stereotype.Component;

@Component
@Jt808RequestMsgHandler
public class AnnotationHandler01 {

    @Jt808RequestMsgHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2011)
    public Object processAuthMsg(Jt808Request request, Jt808Session session, DemoAuthMsgV2011 authMsgV2011) {
//        final Jt808ByteBuf jt808ByteBuf = new Jt808ByteBuf(ByteBufAllocator.DEFAULT.buffer())
//                .writeWord(request.flowId())
//                .writeWord(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId());
//        jt808ByteBuf.writeByte(0);

        return new DemoServerCommonReplyRespMsg().setResult((byte) 1).setFlowId(111).setMsgId(0x0200);
    }
}
