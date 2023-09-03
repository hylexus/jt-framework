package io.github.hylexus.jt.dashboard.client.controller.jt1078;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.hylexus.jt.core.model.dto.SimplePageableDto;
import io.github.hylexus.jt.core.model.vo.PageableVo;
import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @RequestMapping("/sessions")
    public PageableVo<Jt1078SessionVo> sessionList(SimplePageableDto pageable) {
        final long total = this.sessionManager.count();
        final List<Jt1078SessionVo> list = this.sessionManager.list()
                .sorted(Comparator.comparing(Jt1078Session::sim))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .map(it -> new Jt1078SessionVo(it.sessionId(), it.sim(), new Date(it.lastCommunicateTimestamp())))
                .collect(Collectors.toList());
        return PageableVo.of(total, list);
    }

    @RequestMapping("/subscribers")
    public PageableVo<Jt1078SubscriberDescriptor> subscribers(SimplePageableDto pageable) {
        final long total = this.publisherManager.count(it -> true);
        final List<Jt1078SubscriberDescriptor> list = this.publisherManager.list()
                .sorted(Comparator.comparing(Jt1078SubscriberDescriptor::getId))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return PageableVo.of(total, list);
    }

    @RequestMapping("/subscribers/{sim}")
    public PageableVo<Jt1078SubscriberDescriptor> subscribersOfSession(SimplePageableDto pageable, @PathVariable("sim") String sim) {
        final Predicate<Jt1078SubscriberDescriptor> filter = it -> it.getSim().equals(sim);
        final long total = this.publisherManager.count(filter);

        final List<Jt1078SubscriberDescriptor> list = this.publisherManager.list()
                .filter(filter)
                .sorted(Comparator.comparing(Jt1078SubscriberDescriptor::getId))
                .skip((long) (pageable.getPage() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return PageableVo.of(total, list);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Jt1078SessionVo {
        private String id;
        private String sim;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastCommunicateTime;
    }
}
