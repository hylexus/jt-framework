package io.github.hylexus.jt808.samples.annotation.controller;

import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt808.samples.annotation.model.vo.TerminalVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }

    @GetMapping("/debug/terminals")
    @ResponseBody
    public List<TerminalVo> terminalList() {
        return this.sessionManager.list().map(this::toTerminalVo).collect(Collectors.toList());
    }

    private TerminalVo toTerminalVo(Jt808Session session) {
        return new TerminalVo().setTerminalId(session.terminalId())
                .setVersion(session.protocolVersion().getShortDesc())
                .setLastCommunicationTime(new Date(session.lastCommunicateTimestamp()));
    }
}
