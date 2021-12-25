package io.github.hylexus.jt808.samples.customized.session;

import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;
import io.github.hylexus.jt.jt808.spec.session.SessionCloseReason;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * @author hylexus
 */
@Slf4j
public class MyJt808SessionEventListener01 implements Jt808SessionEventListener {

    @Override
    public int getOrder() {
        return 200;
    }

    @Override
    public void onSessionAdd(@Nullable Jt808Session session) {
        if (session == null) {
            return;
        }
        log.info("[SessionAdd] terminalId = {}, sessionId = {}", session.terminalId(), session.id());
    }

    @Override
    public void onSessionRemove(@Nullable Jt808Session session) {
        if (session == null) {
            return;
        }
        log.info("[SessionRemove] terminalId = {}, sessionId = {}", session.terminalId(), session.id());
    }

    @Override
    public void onSessionClose(@Nullable Jt808Session session, SessionCloseReason closeReason) {
        if (session == null) {
            return;
        }
        log.info("[SessionClose] terminalId = {}, sessionId = {}, reason = {}", session.terminalId(), session.id(), closeReason);
    }

}
