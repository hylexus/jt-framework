package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.servlet;

import io.github.hylexus.jt.web.blocking.JtResponseBodyAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JtDashboardServletCommonConfiguration {
    @Bean
    public JtResponseBodyAdvice jtResponseBodyAdvice() {
        return new JtResponseBodyAdvice();
    }
}
