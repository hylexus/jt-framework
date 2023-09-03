package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.reactive;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.impl.factory.Jt1078ApplicationFactory;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationRegistrator;
import io.github.hylexus.jt.dashboard.client.registration.impl.client.Jt1078ReactiveJtApplicationClient;
import io.github.hylexus.jt.dashboard.client.registration.impl.registrator.DefaultJtApplicationRegistrator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Import({
        BuiltinVideoStreamSubscriberConfiguration.class,
})
public class ReactiveJtDashboard1078Configuration {

    @Bean
    public JtApplicationRegistrator reactiveJt1078ApplicationRegistrator(JtApplicationProps jtApplicationProps) {
        return new DefaultJtApplicationRegistrator(
                new Jt1078ApplicationFactory(jtApplicationProps),
                new Jt1078ReactiveJtApplicationClient(WebClient.builder().build()),
                jtApplicationProps.getJt1078().getServerUrl(),
                jtApplicationProps.getJt1078().isRegisterOnce()
        );
    }
}
