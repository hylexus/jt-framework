package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808;

import io.github.hylexus.jt.dashboard.client.controller.jt808.BuiltinJt808TerminalManagerController;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import org.springframework.context.annotation.Bean;

public class BuiltinJt808ControllerConfiguration {
    @Bean
    public BuiltinJt808TerminalManagerController builtinJt808TerminalManagerController(Jt808SessionManager sessionManager) {
        return new BuiltinJt808TerminalManagerController(sessionManager);
    }

}
