package io.github.hylexus.jt.dashboard.client.actuator.jt808;

import io.github.hylexus.jt.dashboard.common.model.dto.jt808.TerminalListDto;
import io.github.hylexus.jt.dashboard.common.model.vo.PageableVo;
import io.github.hylexus.jt.dashboard.common.model.vo.TerminalVo;
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
@RequestMapping("/jt808/terminal")
public class BuiltinJt808TerminalManagerController {

    private final Jt808SessionManager sessionManager;

    public BuiltinJt808TerminalManagerController(Jt808SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @GetMapping("/list")
    @ResponseBody
    public PageableVo<TerminalVo> terminalList(TerminalListDto dto) {
        final Predicate<Jt808Session> filter = buildFilter(dto);
        final long count = this.sessionManager.count(filter);
        final List<TerminalVo> list = this.sessionManager.list(dto.getPage(), dto.getPageSize(), filter, Comparator.comparing(Jt808Session::terminalId), this::toTerminalVo);
        return PageableVo.of(count, list);
    }

    private Predicate<Jt808Session> buildFilter(TerminalListDto dto) {
        Predicate<Jt808Session> filter = session -> true;

        if (StringUtils.isNotEmpty(dto.getTerminalId())) {
            filter = filter.and(session -> session.terminalId().contains(dto.getTerminalId()));
        }

        if (StringUtils.isNotEmpty(dto.getVersion()) && !"all".equalsIgnoreCase(dto.getVersion())) {
            filter = filter.and(session -> session.protocolVersion().getShortDesc().equals(dto.getVersion()));
        }

        return filter;
    }

    private TerminalVo toTerminalVo(Jt808Session session) {
        final String location = session.getAttribute("latest_geo_key");
        return new TerminalVo().setTerminalId(session.terminalId())
                .setVersion(session.protocolVersion().getShortDesc())
                .setLastCommunicationTime(new Date(session.lastCommunicateTimestamp()))
                .setLatestGeo(location);
    }
}
