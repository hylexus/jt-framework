package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionEventListener;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
public class BuiltinJt1078SessionCloseListener implements Jt1078SessionEventListener {
    private final Jt1078Publisher publisher;

    public BuiltinJt1078SessionCloseListener(Jt1078Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onSessionClose(@Nullable Jt1078Session session, Jt1078SessionCloseReason closeReason) {
        this.unsubscribe(session, closeReason);
    }

    private void unsubscribe(Jt1078Session session, Jt1078SessionCloseReason closeReason) {
        if (session != null) {
            log.info("Unsubscribe for sim [{}], because [{}]", session.sim(), closeReason);
            publisher.unsubscribeWithSimAndChannelNumber(session.sim(), session.channelNumber(), new Jt1078SessionDestroyException("session closed"));
        }
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}
