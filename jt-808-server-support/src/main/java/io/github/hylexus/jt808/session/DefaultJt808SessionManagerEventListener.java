package io.github.hylexus.jt808.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * Created At 2020-06-22 20:41
 *
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class DefaultJt808SessionManagerEventListener implements Jt808SessionManagerEventListener {

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
