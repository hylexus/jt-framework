package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author hylexus
 */
@Component
public class SimpleLocationInfoUploadHandlerSimple implements SimpleJt808RequestHandler<Jt808Response> {
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Jdk8Adapter.setOf(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jdk8Adapter.setOf(Jt808ProtocolVersion.VERSION_2019);
    }

    // 7e02004086010000000001893094655200E4000000000000000101D907F2073D336C00000000000021112411480801040000002603020000300115310100250400000000140400000001
    // 1504000000FA160400000000170200001803000000EA10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B01711800000000000000000000000000000000000000000
    // 0000000567e
    @Override
    public Jt808Response handleMsg(Jt808ServerExchange exchange) {
        final Jt808Request request = exchange.request();

        // 忽略请求读取过程

        // 下面是直接返回Jt808Response类型数据
        return exchange.response()
                .msgId(BuiltinJt808MsgType.SERVER_COMMON_REPLY.getMsgId())
                .flowId(exchange.session().nextFlowId())
                // body
                .writeWord(request.flowId())
                .writeWord(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId())
                .writeByte(0);
    }
}
