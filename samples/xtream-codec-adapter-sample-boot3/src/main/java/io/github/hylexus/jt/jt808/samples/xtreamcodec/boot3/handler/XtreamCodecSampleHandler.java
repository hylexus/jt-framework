package io.github.hylexus.jt.jt808.samples.xtreamcodec.boot3.handler;

import io.github.hylexus.jt.jt808.samples.xtreamcodec.boot3.message.XtreamMessage0001;
import io.github.hylexus.jt.jt808.samples.xtreamcodec.boot3.message.XtreamMessage0100AllInOne;
import io.github.hylexus.jt.jt808.samples.xtreamcodec.boot3.message.XtreamMessage0800;
import io.github.hylexus.jt.jt808.samples.xtreamcodec.boot3.message.XtreamMessage8100;
import io.github.hylexus.jt.jt808.spec.CommandWaitingPool;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.*;

/**
 * @author hylexus
 */
@Component
@Jt808RequestHandler
public class XtreamCodecSampleHandler {
    private static final Logger log = LoggerFactory.getLogger(XtreamCodecSampleHandler.class);

    // 7e000100050139123443210000007b020001007e
    // 7e0001400501000000000139123443290000007b020001497e
    @Jt808RequestHandlerMapping(msgType = 0x0001, versions = AUTO_DETECTION)
    public void processMsg0001(Jt808RequestEntity<XtreamMessage0001> request) {
        final XtreamMessage0001 body = request.body();
        final String terminalId = request.header().terminalId();
        log.info("终端通用应答: {}", body);
        // 2. 生成同样的Key
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, body.serverFlowId());
        // 将结果放入 CommandWaitingPool
        CommandWaitingPool.getInstance().putIfNecessary(commandKey, body);
    }

    // 7e0100405601000000000139123443290000000b0002696431323334353637383974797065313233343536373839303132333435363738393031323334353669643132333435363738393031323334353637383930313233343536373801b8ca4a2d313233343539517e
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public XtreamMessage8100 clientRegisterV2019(Jt808RequestEntity<XtreamMessage0100AllInOne> request) {
        final XtreamMessage0100AllInOne body = request.body();
        log.info("client register v2019 : {}", body);
        return new XtreamMessage8100().setClientFlowId(request.flowId()).setResult((byte) 0).setAuthCode("AuthCode2019----");
    }

    // 7e0100002f0139123443230000000b0002696433323174797065313233343536373839303132333435366964313233343501b8ca4a2d3132333435395a7e
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2013)
    public XtreamMessage8100 clientRegisterV2013(Jt808Request request, XtreamMessage0100AllInOne body) {
        log.info("client register v2013 : {}", body);
        return new XtreamMessage8100().setClientFlowId(request.flowId()).setResult((byte) 0).setAuthCode("AuthCode2013----");
    }

    // 7e08004008010000000001391234432900000000006f0000077b267e
    @Jt808RequestHandlerMapping(msgType = 0x0800, versions = AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0800(Jt808RequestEntity<XtreamMessage0800> request) {
        final XtreamMessage0800 body = request.body();

        log.info("多媒体事件信息上传: {}", body);
        return BuiltinServerCommonReplyMsg.success(request.msgId(), request.flowId());
    }
}
