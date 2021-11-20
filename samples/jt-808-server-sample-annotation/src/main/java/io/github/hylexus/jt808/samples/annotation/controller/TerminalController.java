package io.github.hylexus.jt808.samples.annotation.controller;

import io.github.hylexus.jt808.session.Jt808SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created At 2020-06-20 20:17
 *
 * @author hylexus
 */
@RestController
@RequestMapping("/annotation")
public class TerminalController {

    @Autowired
    private Jt808SessionManager jt808SessionManager;

    @GetMapping("/session-list")
    public Object sessionList(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {

        return jt808SessionManager.list(page, pageSize, session -> {
            if (StringUtils.isEmpty(keyword)) {
                return true;
            }
            final String lowerCase = keyword.toLowerCase();
            return session.getTerminalId().toLowerCase().contains(lowerCase)
                   || session.getSessionId().toLowerCase().contains(lowerCase);
        });

    }
}
