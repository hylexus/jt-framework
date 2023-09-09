package io.github.hylexus.jt.dashboard.client.actuator.jt808.metrics.simple;

import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.session.*;
import io.github.hylexus.jt.utils.FormatUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

@Getter
@Setter
public class Jt808ServerSimpleMetricsHolder {

    private final SessionInfo sessions = new SessionInfo();
    private final RequestInfo requests = new RequestInfo();

    @Getter
    @Setter
    public static class RequestInfo {
        private final LongAdder total = new LongAdder();
        private final Map<Integer, RequestDetail> details = new HashMap<>();

        public void incrementTotal() {
            this.total.increment();
        }
    }

    @Getter
    @Setter
    public static class RequestDetail {
        private int msgId;
        private long count;
        private String desc;

        public RequestDetail(int msgId, long count, String desc) {
            this.msgId = msgId;
            this.count = count;
            this.desc = desc;
        }

        public void incrementCount() {
            this.count++;
        }

        // for jackson
        public String getMsgIdAsHexString() {
            return "0x" + FormatUtils.toHexString(getMsgId(), 4);
        }
    }

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

    @Slf4j
    public static class RequestJt808ServerInfoCollector implements Jt808RequestLifecycleListener {
        private final Jt808ServerSimpleMetricsHolder collector;
        private final Jt808MsgTypeParser msgTypeParser;

        public RequestJt808ServerInfoCollector(Jt808ServerSimpleMetricsHolder collector, Jt808MsgTypeParser msgTypeParser) {
            this.collector = collector;
            this.msgTypeParser = msgTypeParser;
        }

        @Override
        public boolean beforeDecode(ByteBuf request, Channel channel) {
            this.collector.getRequests().incrementTotal();
            return true;
        }

        static class PlaceholderMsgType implements MsgType {
            private final int msgId;

            PlaceholderMsgType(int msgId) {
                this.msgId = msgId;
            }

            @Override
            public int getMsgId() {
                return msgId;
            }

            @Override
            public Optional<MsgType> parseFromInt(int msgId) {
                return Optional.empty();
            }

            @Override
            public String getDesc() {
                return "解析异常";
            }
        }

        @Override
        public boolean beforeDispatch(Jt808Request request) {
            try {
                final int msgId = request.header().msgId();
                final MsgType msgType = this.msgTypeParser.parseMsgType(msgId)
                        .orElseGet(() -> new PlaceholderMsgType(request.header().msgId()));
                this.collector.requests.details.computeIfAbsent(msgId, k -> new RequestDetail(msgId, 0L, msgType.getDesc())).incrementCount();
            } catch (Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }
            return true;
        }
    }

    public static class SessionJt808ServerInfoCollector implements Jt808SessionEventListener, Jt808SessionManagerAware {
        private Jt808SessionManager sessionManager;
        private final Jt808ServerSimpleMetricsHolder collector;
        private long maxSessionCount = 0;

        public SessionJt808ServerInfoCollector(Jt808ServerSimpleMetricsHolder collector) {
            this.collector = collector;
        }

        @Override
        public void onSessionAdd(@Nullable Jt808Session session) {
            final long count = sessionManager.count();
            this.collector.getSessions().max(this.maxSessionCount = Math.max(maxSessionCount, count));
            this.collector.getSessions().current(count);
        }

        @Override
        public void onSessionClose(@Nullable Jt808Session session, SessionCloseReason closeReason) {
            final long count = sessionManager.count();
            this.collector.getSessions().current(count);
        }

        @Override
        public void setJt808SessionManager(Jt808SessionManager sessionManager) {
            this.sessionManager = sessionManager;
        }
    }

}
