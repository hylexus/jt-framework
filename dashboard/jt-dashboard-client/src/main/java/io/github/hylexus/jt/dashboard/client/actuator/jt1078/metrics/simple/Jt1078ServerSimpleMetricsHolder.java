package io.github.hylexus.jt.dashboard.client.actuator.jt1078.metrics.simple;

import io.github.hylexus.jt.jt1078.spec.*;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
public class Jt1078ServerSimpleMetricsHolder {

    private final SessionInfo sessions = new SessionInfo();

    @Getter
    @Setter
    public static class SessionInfo {
        private long max = 0;
        private long current = 0;

        public void max(long value) {
            this.max = value;
        }

        public void current(long value) {
            this.current = value;
        }
    }

    public static class SessionJt1078ServerInfoCollector implements Jt1078SessionEventListener, Jt1078SessionManagerAware {
        private Jt1078SessionManager sessionManager;
        private final Jt1078ServerSimpleMetricsHolder collector;
        private long maxSessionCount = 0;

        public SessionJt1078ServerInfoCollector(Jt1078ServerSimpleMetricsHolder collector) {
            this.collector = collector;
        }

        @Override
        public void onSessionAdd(@Nullable Jt1078Session session) {
            final long count = sessionManager.count();
            this.collector.getSessions().max(this.maxSessionCount = Math.max(maxSessionCount, count));
            this.collector.getSessions().current(count);
        }

        @Override
        public void onSessionClose(@Nullable Jt1078Session session, Jt1078SessionCloseReason closeReason) {
            final long count = sessionManager.count();
            this.collector.getSessions().current(count);
        }

        @Override
        public void setJt1078SessionManager(Jt1078SessionManager sessionManager) {
            this.sessionManager = sessionManager;
        }

    }

}
