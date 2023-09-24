package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808.blocking;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.impl.factory.Jt808ApplicationFactory;
import io.github.hylexus.jt.dashboard.client.controller.jt808.blocking.BuiltinBlockingJt1078CommandController;
import io.github.hylexus.jt.dashboard.client.controller.jt808.reactive.BuiltinReactiveJt1078CommandController;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationRegistrator;
import io.github.hylexus.jt.dashboard.client.registration.impl.client.Jt808BlockingJtApplicationClient;
import io.github.hylexus.jt.dashboard.client.registration.impl.registrator.DefaultJtApplicationRegistrator;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class BlockingJtDashboard808Configuration {

    @Bean
    public JtApplicationRegistrator blockingJt808ApplicationRegistrator(JtApplicationProps jtApplicationProps) {
        return new DefaultJtApplicationRegistrator(
                new Jt808ApplicationFactory(jtApplicationProps),
                new Jt808BlockingJtApplicationClient(new RestTemplate()),
                jtApplicationProps.getJt808().getServerUrl(),
                jtApplicationProps.getJt808().isRegisterOnce()
        );
    }

    @Bean
    public BuiltinBlockingJt1078CommandController builtinJt1078CommandController(Jt808CommandSender commandSender, Jt808SessionManager sessionManager) {
        return new BuiltinBlockingJt1078CommandController(commandSender, sessionManager);
    }
}
