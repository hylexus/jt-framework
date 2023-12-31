package io.github.hylexus.jt.dashboard.client.controller.jt808;

import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.core.model.vo.PageableVo;
import io.github.hylexus.jt.dashboard.client.controller.model.vo.DashboardTerminalVo;
import io.github.hylexus.jt.dashboard.common.model.dto.jt808.DashboardTerminalListDto;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Controller
@RequestMapping("/api/dashboard-client/jt808")
public class BuiltinJt808TerminalManagerController {

    private final Jt808SessionManager sessionManager;

    public BuiltinJt808TerminalManagerController(Jt808SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @GetMapping("/sessions")
    @ResponseBody
    public Resp<PageableVo<DashboardTerminalVo>> terminalList(DashboardTerminalListDto dto) {
        final Predicate<Jt808Session> filter = buildFilter(dto);
        final long count = this.sessionManager.count(filter);
        final List<DashboardTerminalVo> list = this.sessionManager.list(dto.getPage(), dto.getPageSize(), filter, Comparator.comparing(Jt808Session::terminalId), this::toTerminalVo);
        return Resp.success(PageableVo.of(count, list));
    }

    private Predicate<Jt808Session> buildFilter(DashboardTerminalListDto dto) {
        Predicate<Jt808Session> filter = session -> true;

        if (StringUtils.isNotEmpty(dto.getTerminalId())) {
            filter = filter.and(session -> session.terminalId().contains(dto.getTerminalId()));
        }

        if (StringUtils.isNotEmpty(dto.getVersion()) && !"all".equalsIgnoreCase(dto.getVersion())) {
            filter = filter.and(session -> session.protocolVersion().getShortDesc().equals(dto.getVersion()));
        }

        return filter;
    }

    private DashboardTerminalVo toTerminalVo(Jt808Session session) {
        return new DashboardTerminalVo()
                .setTerminalId(session.terminalId())
                .setVersion(session.protocolVersion().getShortDesc())
                .setLastCommunicationTime(new Date(session.lastCommunicateTimestamp()))
                .setCreatedAt(new Date(session.createdAt()));
    }
}
