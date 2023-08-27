package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808;

import io.github.hylexus.jt.dashboard.client.actuator.jt808.BuiltinJt1078CommandController;
import io.github.hylexus.jt.dashboard.client.actuator.jt808.BuiltinJt808TerminalManagerController;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import org.springframework.context.annotation.Bean;

public class BuiltinJt808ControllerConfiguration {
    @Bean
    public BuiltinJt808TerminalManagerController builtinJt808TerminalManagerController(Jt808SessionManager sessionManager) {
        return new BuiltinJt808TerminalManagerController(sessionManager);
    }

    @Bean
    public BuiltinJt1078CommandController builtinJt1078CommandController(Jt808CommandSender commandSender, Jt808SessionManager sessionManager) {
        return new BuiltinJt1078CommandController(commandSender, sessionManager);
    }
}
