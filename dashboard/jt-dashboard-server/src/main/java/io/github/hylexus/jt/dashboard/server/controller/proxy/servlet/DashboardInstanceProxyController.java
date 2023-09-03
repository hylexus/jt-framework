package io.github.hylexus.jt.dashboard.server.controller.proxy.servlet;


import io.github.hylexus.jt.dashboard.server.common.execption.JtInstanceNotFoundException;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebProxy;
import io.github.hylexus.jt.dashboard.server.proxy.HttpHeaderFilter;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;
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
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/83db63e82e916357d36b1e6b4d552e1b6506ecc9/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/web/servlet/InstancesProxyController.java#L58">de.codecentric.boot.admin.server.web.servlet.InstancesProxyController</a>
 */
@Slf4j
@Controller
public class DashboardInstanceProxyController {

    private final HttpHeaderFilter httpHeadersFilter = new HttpHeaderFilter(Set.of());
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private static final String INSTANCE_MAPPED_PATH = "/api/dashboard/proxy/{instanceId}/**";
    private final DashboardWebProxy webProxy;
    private final ProxyInstanceProvider instanceSupplier;
    private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

    public DashboardInstanceProxyController(ProxyInstanceProvider instanceSupplier, DashboardWebClient.Builder builder) {
        this.instanceSupplier = instanceSupplier;
        final DashboardWebClient dashboardWebClient = builder.build();
        this.webProxy = new DashboardWebProxy(dashboardWebClient);
    }

    @RequestMapping(path = INSTANCE_MAPPED_PATH)
    public void endpointProxy(
            @PathVariable("instanceId") String instanceId,
            HttpServletRequest servletRequest, HttpServletResponse response1) {

        final JtInstance instance = this.instanceSupplier.getInstance(instanceId)
                .orElseThrow(() -> new JtInstanceNotFoundException("No server instance found with id " + instanceId));


        AsyncContext asyncContext = servletRequest.startAsync();
        asyncContext.setTimeout(-1); // no timeout because instanceWebProxy will handle it
        // for us
        try {
            final ServletServerHttpRequest request = new ServletServerHttpRequest((HttpServletRequest) asyncContext.getRequest());
            final Flux<DataBuffer> requestBody = DataBufferUtils.readInputStream(request::getBody, this.bufferFactory, 4096);
            final DashboardWebProxy.ForwardRequest forwardRequest = this.createTargetRequest(request, instance, requestBody);

            this.webProxy
                    .proxy(instance, forwardRequest, (clientResponse) -> {
                        try (ServerHttpResponse response = new ServletServerHttpResponse((HttpServletResponse) asyncContext.getResponse())) {
                            response.setStatusCode(clientResponse.statusCode());
                            response.getHeaders().addAll(this.httpHeadersFilter.filterHeaders(clientResponse.headers().asHttpHeaders()));
                            try {
                                final OutputStream responseBody = response.getBody();
                                response.flush();
                                return clientResponse.body(BodyExtractors.toDataBuffers())
                                        .window(1)
                                        .concatMap((body) -> writeAndFlush(body, responseBody))
                                        .then();
                            } catch (IOException ex) {
                                return Mono.error(ex);
                            }
                        }
                    })
                    // We need to explicitly block so the headers are recieved and written
                    // before any async dispatch otherwise the FrameworkServlet will add
                    // wrong
                    // Allow header for OPTIONS request
                    .block();
        } finally {
            asyncContext.complete();
        }
    }

    private DashboardWebProxy.ForwardRequest createTargetRequest(ServletServerHttpRequest request, JtInstance instance, Flux<DataBuffer> body) {
        final String localPath = this.getLocalPath(request);
        final URI uri = UriComponentsBuilder.fromHttpUrl(instance.getRegistration().getBaseUrl())
                .query(request.getURI().getRawQuery())
                .path(localPath)
                .build(true).toUri();

        return DashboardWebProxy.ForwardRequest.builder()
                .uri(uri)
                .method(request.getMethod())
                .headers(this.httpHeadersFilter.filterHeaders(request.getHeaders()))
                .body(BodyInserters.fromDataBuffers(body))
                .build();
    }

    private String getLocalPath(ServletServerHttpRequest request) {
        final String pathWithinApplication = request.getServletRequest().getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        return this.pathMatcher.extractPathWithinPattern(INSTANCE_MAPPED_PATH, pathWithinApplication);
    }

    private Mono<Void> writeAndFlush(Flux<DataBuffer> body, OutputStream responseBody) {
        return DataBufferUtils.write(body, responseBody).map(DataBufferUtils::release).then(Mono.create((sink) -> {
            try {
                responseBody.flush();
                sink.success();
            } catch (IOException ex) {
                sink.error(ex);
            }
        }));
    }
}
