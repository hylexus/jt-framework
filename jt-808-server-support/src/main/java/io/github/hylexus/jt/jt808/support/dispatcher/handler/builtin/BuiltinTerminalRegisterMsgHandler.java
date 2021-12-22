package io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin;

import io.github.hylexus.jt.core.BuiltinComponent;
import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0100V2011;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0100V2013;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0100V2019;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg8100;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.*;

/**
 * @author hylexus
 */
@Slf4j
@Jt808RequestHandler
public class BuiltinTerminalRegisterMsgHandler implements ReplaceableComponent, BuiltinComponent {

    // terminalId: 013912344321
    // 7E01000023013912344321007B000B0002696431323361626364656667684944313233343501B8CA4A2D313233343531317E
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2011)
    public BuiltinMsg8100 processTerminalRegisterV2011(Jt808Request request, BuiltinMsg0100V2011 msgBody) {
        log.info("TerminalRegister V2011 : {}", msgBody);
        return new BuiltinMsg8100()
                .setTerminalFlowId(request.flowId())
                .setAuthCode("AuthCode-admin-2011")
                .setResult((byte) 0);
    }

    // terminalId: 013912344323
    // 7E0100002F013912344323007B000B0002696431323374797065313233343536373838373635343332314944313233343501B8CA4A2D3132333435332D7E
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2013)
    public BuiltinMsg8100 processTerminalRegisterV2013(Jt808RequestEntity<BuiltinMsg0100V2013> entity) {
        log.info("TerminalRegister V2013 : {}", entity.body());
        return new BuiltinMsg8100()
                .setTerminalFlowId(entity.flowId())
                .setAuthCode("AuthCode-admin-2013")
                .setResult((byte) 0);
    }

    // terminalId: 013912344329
    // 7E010040560100000000013912344329007B000B00026964393837363534333231747970653030313233343536373831323334353637383837363534333231494
    // 43030303031323334353637383132333435363738383736353433323101B8CA4A2D313233343539257E
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public Jt808Response processTerminalRegisterV2019(Jt808RequestEntity<BuiltinMsg0100V2019> request) {
        log.info("TerminalRegister V2019 : {}", request.body());

        return Jt808Response.newBuilder()
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                .version(request.version())
                .terminalId(request.terminalId())
                .flowId(request.session().nextFlowId())
                .body(writer -> writer
                        // 应答流水号
                        .writeWord(request.flowId())
                        // 结果
                        .writeByte(0)
                        // 鉴权码
                        .writeString("AuthCode-admin-2019")
                ).build();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
