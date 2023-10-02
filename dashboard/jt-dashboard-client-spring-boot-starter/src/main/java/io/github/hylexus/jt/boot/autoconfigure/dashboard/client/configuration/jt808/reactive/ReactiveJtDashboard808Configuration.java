package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808.reactive;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.impl.factory.Jt808ApplicationFactory;
import io.github.hylexus.jt.dashboard.client.controller.jt808.reactive.BuiltinReactiveJt1078CommandController;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationRegistrator;
import io.github.hylexus.jt.dashboard.client.registration.impl.client.Jt808ReactiveJtApplicationClient;
import io.github.hylexus.jt.dashboard.client.registration.impl.registrator.DefaultJtApplicationRegistrator;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveJtDashboard808Configuration {
    @Bean
    public JtApplicationRegistrator reactiveJt808ApplicationRegistrator(JtApplicationProps applicationProps) {
        return new DefaultJtApplicationRegistrator(
                new Jt808ApplicationFactory(applicationProps),
                new Jt808ReactiveJtApplicationClient(WebClient.builder().build()),
                applicationProps.getJt808().getServerUrl(),
                applicationProps.getJt808().isRegisterOnce()
        );
    }

    @Bean
    public BuiltinReactiveJt1078CommandController builtinJt1078CommandController(Jt808CommandSender commandSender, Jt808SessionManager sessionManager) {
        return new BuiltinReactiveJt1078CommandController(commandSender, sessionManager);
    }
}
