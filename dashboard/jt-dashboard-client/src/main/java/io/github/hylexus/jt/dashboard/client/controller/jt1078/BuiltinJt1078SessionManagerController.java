package io.github.hylexus.jt.dashboard.client.controller.jt1078;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.hylexus.jt.core.model.dto.SimplePageableDto;
import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.core.model.vo.PageableVo;
import io.github.hylexus.jt.dashboard.client.controller.model.dto.DashboardSessionListDto;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.github.hylexus.jt.dashboard.common.consts.DashboardJt1078SessionCloseReason.CLOSED_BY_DASHBOARD_HTTP_API;

@Slf4j
@RestController
@RequestMapping("/api/dashboard-client/jt1078")
public class BuiltinJt1078SessionManagerController {
    private final Jt1078SubscriberManager subscriberManager;
    private final Jt1078SessionManager sessionManager;

    public BuiltinJt1078SessionManagerController(Jt1078SubscriberManager subscriberManager, Jt1078SessionManager sessionManager) {
        this.subscriberManager = subscriberManager;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/sessions")
    // public Resp<PageableVo<Jt1078SessionVo>> sessionList(DashboardSessionListDto pageable) {
    public Resp<Object> sessionList(DashboardSessionListDto pageable) {

        Predicate<Jt1078Session> filter = session -> true;
        if (StringUtils.isNotEmpty(pageable.getSim())) {
            filter = filter.and(session -> session.sim().contains(pageable.getSim()));
        }

        if (pageable.getChannelNumber() > 0) {
            filter = filter.and(session -> session.channelNumber() == pageable.getChannelNumber());
        }

        final long total = this.sessionManager.count(filter);
        final Set<String> sessionIds = new HashSet<>();
        final List<Jt1078SessionVo> list = this.sessionManager.list()
                .filter(filter)
                .sorted(Comparator.comparing(Jt1078Session::sessionId))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .peek(it -> sessionIds.add(it.sessionId()))
                .map(it -> new Jt1078SessionVo(it.sessionId(), it.sim(), it.channelNumber(), new Date(it.lastCommunicateTimestamp())))
                .collect(Collectors.toList());

        if (list.isEmpty() || !pageable.isWithSubscribers()) {
            return Resp.success(PageableVo.of(total, list));
        }

        final Map<String, List<Jt1078SubscriberDescriptor>> groupBySessionId = this.subscriberManager.list()
                .filter(it -> sessionIds.contains(this.sessionManager.generateSessionId(it.getSim(), it.getChannel())))
                .collect(Collectors.groupingBy(it -> this.sessionManager.generateSessionId(it.getSim(), it.getChannel())));

        for (final Jt1078SessionVo vo : list) {
            vo.setSubscribers(groupBySessionId.getOrDefault(vo.getId(), Collections.emptyList()));
        }
        return Resp.success(PageableVo.of(total, list));
    }

    @DeleteMapping("/sessions/{sessionId}")
    public Resp<Object> closeSessions(@PathVariable("sessionId") String sessionId) {
        this.sessionManager.removeBySessionIdAndThenClose(sessionId, CLOSED_BY_DASHBOARD_HTTP_API);
        return Resp.success(sessionId);
    }

    @GetMapping("/subscribers")
    // public PageableVo<Jt1078SubscriberDescriptor> subscribers(SimplePageableDto pageable) {
    public Resp<Object> subscribers(SimplePageableDto pageable) {
        final long total = this.subscriberManager.count(it -> true);
        final List<Jt1078SubscriberDescriptor> list = this.subscriberManager.list()
                .sorted(Comparator.comparing(Jt1078SubscriberDescriptor::getId))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return Resp.success(PageableVo.of(total, list));
    }

    @DeleteMapping("/subscribers/{subscriberId}")
    public Resp<Object> closeSubscribers(@PathVariable("subscriberId") String subscriberId) {
        this.subscriberManager.closeSubscriber(subscriberId);
        return Resp.success(subscriberId);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Jt1078SessionVo {
        private String id;
        private String sim;
        private short channel;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastCommunicateTime;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Jt1078SubscriberDescriptor> subscribers;

        public Jt1078SessionVo(String id, String sim, short channel, Date lastCommunicateTime) {
            this.id = id;
            this.sim = sim;
            this.channel = channel;
            this.lastCommunicateTime = lastCommunicateTime;
        }
    }
}
