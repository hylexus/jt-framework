package io.github.hylexus.jt.dashboard.client.controller.jt1078;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.hylexus.jt.core.model.dto.SimplePageableDto;
import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.core.model.vo.PageableVo;
import io.github.hylexus.jt.dashboard.client.controller.model.dto.DashboardCloseSessionDto;
import io.github.hylexus.jt.dashboard.client.controller.model.dto.DashboardCloseSubscriberDto;
import io.github.hylexus.jt.dashboard.client.controller.model.dto.DashboardSessionListDto;
import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.github.hylexus.jt.dashboard.common.consts.DashboardJt1078SessionCloseReason.CLOSED_BY_DASHBOARD_HTTP_API;

@Slf4j
@RestController
@RequestMapping("/api/dashboard-client/jt1078")
public class BuiltinJt1078SessionManagerController {
    private final Jt1078PublisherManager publisherManager;
    private final Jt1078SessionManager sessionManager;

    public BuiltinJt1078SessionManagerController(Jt1078PublisherManager publisherManager, Jt1078SessionManager sessionManager) {
        this.publisherManager = publisherManager;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/sessions")
    // public Resp<PageableVo<Jt1078SessionVo>> sessionList(DashboardSessionListDto pageable) {
    public Resp<Object> sessionList(DashboardSessionListDto pageable) {
        final long total = this.sessionManager.count();
        final Set<String> sessionIds = new HashSet<>();
        final List<Jt1078SessionVo> list = this.sessionManager.list()
                .sorted(Comparator.comparing(Jt1078Session::sim))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .peek(it -> sessionIds.add(it.sessionId()))
                .map(it -> new Jt1078SessionVo(it.sessionId(), it.sim(), it.channelNumber(), new Date(it.lastCommunicateTimestamp())))
                .collect(Collectors.toList());

        if (list.isEmpty() || !pageable.isWithSubscribers()) {
            return Resp.success(PageableVo.of(total, list));
        }

        final Map<String, List<Jt1078SubscriberDescriptor>> groupBySessionId = this.publisherManager.list()
                .filter(it -> sessionIds.contains(this.sessionManager.generateSessionId(it.getSim(), it.getChannel())))
                .collect(Collectors.groupingBy(it -> this.sessionManager.generateSessionId(it.getSim(), it.getChannel())));

        for (final Jt1078SessionVo vo : list) {
            vo.setSubscribers(groupBySessionId.getOrDefault(vo.getId(), Collections.emptyList()));
        }
        return Resp.success(PageableVo.of(total, list));
    }

    @DeleteMapping("/sessions")
    public Resp<Object> closeSessions(@RequestBody DashboardCloseSessionDto dto) {
        if (CollectionUtils.isEmpty(dto.getSessionIdList()) && CollectionUtils.isEmpty(dto.getSimList())) {
            return Resp.parameterError("'sessionIdList' or 'simList' is missing");
        }

        if (!CollectionUtils.isEmpty(dto.getSessionIdList())) {
            for (final String sessionId : dto.getSessionIdList()) {
                this.sessionManager.removeBySessionIdAndThenClose(sessionId, CLOSED_BY_DASHBOARD_HTTP_API);
                // 和当前 session 关联的 Subscriber 会自动关闭
            }
        }

        if (!CollectionUtils.isEmpty(dto.getSimList())) {
            for (final String sim : dto.getSimList()) {
                this.sessionManager.removeBySimAndThenClose(sim, CLOSED_BY_DASHBOARD_HTTP_API);
            }
        }
        return Resp.success(Collections.emptyMap());
    }

    @GetMapping("/subscribers")
    // public PageableVo<Jt1078SubscriberDescriptor> subscribers(SimplePageableDto pageable) {
    public Resp<Object> subscribers(SimplePageableDto pageable) {
        final long total = this.publisherManager.count(it -> true);
        final List<Jt1078SubscriberDescriptor> list = this.publisherManager.list()
                .sorted(Comparator.comparing(Jt1078SubscriberDescriptor::getId))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return Resp.success(PageableVo.of(total, list));
    }

    @DeleteMapping("/subscribers")
    public Resp<Object> closeSubscribers(@RequestBody DashboardCloseSubscriberDto dto) {
        if (CollectionUtils.isEmpty(dto.getIds())) {
            return Resp.parameterError("ids is null or empty");
        }
        for (final String id : dto.getIds()) {
            this.publisherManager.closeSubscriber(id);
        }

        return Resp.success(dto.getIds());
    }

    @GetMapping("/subscribers/{sim}")
    // public PageableVo<Jt1078SubscriberDescriptor> subscribersOfSession(SimplePageableDto pageable, @PathVariable("sim") String sim) {
    public Resp<Object> subscribersOfSession(SimplePageableDto pageable, @PathVariable("sim") String sim) {
        final Predicate<Jt1078SubscriberDescriptor> filter = it -> it.getSim().equals(sim);
        final long total = this.publisherManager.count(filter);

        final List<Jt1078SubscriberDescriptor> list = this.publisherManager.list()
                .filter(filter)
                .sorted(Comparator.comparing(Jt1078SubscriberDescriptor::getId))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return Resp.success(PageableVo.of(total, list));
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
