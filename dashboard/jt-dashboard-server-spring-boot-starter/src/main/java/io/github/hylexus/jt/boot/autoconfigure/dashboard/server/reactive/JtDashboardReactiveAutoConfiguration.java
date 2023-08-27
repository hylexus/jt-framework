package io.github.hylexus.jt.boot.autoconfigure.dashboard.server.reactive;

import io.github.hylexus.jt.dashboard.server.controller.proxy.reactive.DashboardInstanceProxyController;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import io.github.hylexus.jt.web.reactive.JtResponseWrapperResultHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@ConditionalOnWebApplication(type = REACTIVE)
public class JtDashboardReactiveAutoConfiguration {

    @Bean
    public JtResponseWrapperResultHandler jtResponseWrapperResultHandler(
            ServerCodecConfigurer serverCodecConfigurer,
            RequestedContentTypeResolver requestedContentTypeResolver,
            ResponseEntityResultHandler responseEntityResultHandler) {

        return new JtResponseWrapperResultHandler(
                serverCodecConfigurer.getWriters(),
                requestedContentTypeResolver,
                responseEntityResultHandler
        );
    }


    @Bean
    public DashboardInstanceProxyController dashboardInstanceProxyController(ProxyInstanceProvider instanceSupplier, DashboardWebClient.Builder builder) {
        return new DashboardInstanceProxyController(instanceSupplier, builder);
    }

}
