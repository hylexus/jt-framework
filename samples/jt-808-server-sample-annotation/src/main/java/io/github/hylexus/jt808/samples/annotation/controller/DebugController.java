package io.github.hylexus.jt808.samples.annotation.controller;

import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt808.samples.annotation.handler.LocationMsgHandler;
import io.github.hylexus.jt808.samples.annotation.model.vo.TerminalVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DebugController {

    private final Jt808SessionManager sessionManager;

    public DebugController(Jt808SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @GetMapping("/debug/terminals")
    @ResponseBody
    public List<TerminalVo> terminalList() {
        return this.sessionManager.list().map(this::toTerminalVo).collect(Collectors.toList());
    }

    private TerminalVo toTerminalVo(Jt808Session session) {
        final String location = session.getAttribute(LocationMsgHandler.LATEST_GEO_KEY);
        return new TerminalVo().setTerminalId(session.terminalId())
                .setVersion(session.protocolVersion().getShortDesc())
                .setLastCommunicationTime(new Date(session.lastCommunicateTimestamp()))
                .setLatestGeo(location);
    }
}
