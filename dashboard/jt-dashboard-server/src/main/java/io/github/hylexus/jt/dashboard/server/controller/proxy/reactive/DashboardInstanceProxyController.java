package io.github.hylexus.jt.dashboard.server.controller.proxy.reactive;


import io.github.hylexus.jt.dashboard.server.common.execption.JtInstanceNotFoundException;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebProxy;
import io.github.hylexus.jt.dashboard.server.proxy.HttpHeaderFilter;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

/**
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/83db63e82e916357d36b1e6b4d552e1b6506ecc9/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/web/reactive/InstancesProxyController.java#L51">de.codecentric.boot.admin.server.web.reactive.InstancesProxyController</a>
 */
@Slf4j
@Controller
public class DashboardInstanceProxyController {

    private final HttpHeaderFilter httpHeadersFilter = new HttpHeaderFilter(Set.of());
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private static final String INSTANCE_MAPPED_PATH = "/api/dashboard/proxy/{instanceId}/**";
    private final DashboardWebProxy webProxy;
    private final ProxyInstanceProvider instanceSupplier;

    public DashboardInstanceProxyController(ProxyInstanceProvider instanceSupplier, DashboardWebClient.Builder builder) {
        this.instanceSupplier = instanceSupplier;
        final DashboardWebClient dashboardWebClient = builder.build();
        this.webProxy = new DashboardWebProxy(dashboardWebClient);
    }

    @RequestMapping(path = INSTANCE_MAPPED_PATH)
    public Mono<Void> endpointProxy(
            @PathVariable("instanceId") String instanceId,
            ServerHttpRequest request, ServerHttpResponse response) {

        final JtInstance instance = this.instanceSupplier.getInstance(instanceId)
                .orElseThrow(() -> new JtInstanceNotFoundException("No server instance found with id " + instanceId));

        final DashboardWebProxy.ForwardRequest forwardRequest = this.createTargetRequest(request, instance);

        return this.webProxy.proxy(Mono.just(instance), forwardRequest, clientResponse -> {
            response.setStatusCode(clientResponse.statusCode());
            response.getHeaders().addAll(this.httpHeadersFilter.filterHeaders(clientResponse.headers().asHttpHeaders()));
            return response.writeAndFlushWith(clientResponse.body(BodyExtractors.toDataBuffers()).window(1));
        });
    }

    private DashboardWebProxy.ForwardRequest createTargetRequest(ServerHttpRequest request, JtInstance instance) {
        final String localPath = this.getLocalPath(request);
        final URI uri = UriComponentsBuilder.fromHttpUrl(instance.getRegistration().getBaseUrl())
                .query(request.getURI().getRawQuery())
                .path(localPath)
                .build(true).toUri();

        return DashboardWebProxy.ForwardRequest.builder()
                .uri(uri)
                .method(request.getMethod())
                .headers(this.httpHeadersFilter.filterHeaders(request.getHeaders()))
                .body(BodyInserters.fromDataBuffers(request.getBody()))
                .build();
    }

    private String getLocalPath(ServerHttpRequest request) {
        final String pathWithinApplication = request.getPath().pathWithinApplication().value();
        return this.pathMatcher.extractPathWithinPattern(DashboardInstanceProxyController.INSTANCE_MAPPED_PATH, pathWithinApplication);
    }
}
