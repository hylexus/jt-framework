package io.github.hylexus.jt808.samples.customized.session;

import io.github.hylexus.jt808.session.ISessionCloseReason;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.session.Jt808SessionManagerEventListener;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * Created At 2020-06-24 15:11
 *
 * @author hylexus
 */
@Slf4j
public class MyJt808SessionManagerEventListener implements Jt808SessionManagerEventListener {
    @Override
    public void onSessionAdd(@Nullable Jt808Session session) {
        if (session == null) {
            return;
        }
        log.info("[SessionAdd] terminalId = {}, sessionId = {}", session.getTerminalId(), session.getId());
    }

    @Override
    public void onSessionRemove(@Nullable Jt808Session session) {
        if (session == null) {
            return;
        }
        log.info("[SessionRemove] terminalId = {}, sessionId = {}", session.getTerminalId(), session.getId());
    }

    @Override
    public void onSessionClose(@Nullable Jt808Session session, ISessionCloseReason closeReason) {
        if (session == null) {
            return;
        }
        log.info("[SessionClose] terminalId = {}, sessionId = {}, reason = {}", session.getTerminalId(), session.getId(), closeReason);
    }
}
