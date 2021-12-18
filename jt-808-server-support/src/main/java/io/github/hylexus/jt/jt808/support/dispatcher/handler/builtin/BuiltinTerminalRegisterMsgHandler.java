package io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin;

import io.github.hylexus.jt.core.BuiltinComponent;
import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0100V2011;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0100V2019;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg8100;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2011;
import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;

/**
 * @author hylexus
 */
@Slf4j
@Jt808RequestHandler
public class BuiltinTerminalRegisterMsgHandler implements ReplaceableComponent, BuiltinComponent {

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2011)
    public BuiltinMsg8100 processTerminalRegisterV2011(BuiltinMsg0100V2011 msgBody, Jt808Session session) {
        log.info("TerminalRegister V2011 : {}", msgBody);
        return new BuiltinMsg8100()
                .setFlowId(session.getCurrentFlowId())
                .setAuthCode("AuthCode-admin-2011")
                .setResult((byte) 0);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public Jt808Response processTerminalRegisterV2019(Jt808RequestEntity<BuiltinMsg0100V2019> entity) {
        log.info("TerminalRegister V2019 : {}", entity.body());

        return Jt808Response.newBuilder()
                .flowId(entity.session().getCurrentFlowId())
                .body(writer -> writer
                        // 应答流水号
                        .writeWord(entity.session().getCurrentFlowId())
                        // 结果
                        .writeByte(0)
                        // 鉴权码
                        .writeString("AuthCode-admin-2019")
                ).build();
    }

}
