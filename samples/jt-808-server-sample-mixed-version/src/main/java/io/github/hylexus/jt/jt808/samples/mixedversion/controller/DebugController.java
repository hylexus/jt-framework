package io.github.hylexus.jt.jt808.samples.mixedversion.controller;

import io.github.hylexus.jt.jt808.samples.mixedversion.entity.vo.TerminalVo;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
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
        return new TerminalVo().setTerminalId(session.getTerminalId())
                .setVersion(session.getProtocolVersion().getShortDesc())
                .setLastCommunicationTime(new Date(session.getLastCommunicateTimestamp()));
    }
}
