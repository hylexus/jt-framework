package io.github.hylexus.jt.dashboard.server.proxy;

import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static org.springframework.http.HttpMethod.*;

/**
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/1433ca5b8343247075ff775d558c9a82341e2ac6/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/web/InstanceWebProxy.java#L58">de.codecentric.boot.admin.server.web.InstanceWebProxy</a>
 */
@Slf4j
public class DashboardWebProxy {
    private static final List<HttpMethod> METHODS_HAS_BODY = List.of(PUT, POST, PATCH);

    private final DashboardWebClient dashboardWebClient;

    public DashboardWebProxy(DashboardWebClient dashboardWebClient) {
        this.dashboardWebClient = dashboardWebClient;
    }

    public <V> Mono<V> proxy(JtInstance instance, ForwardRequest request, Function<ClientResponse, ? extends Mono<V>> responseHandler) {
        return proxy(Mono.just(instance), request, responseHandler);
    }

    public <V> Mono<V> proxy(Mono<JtInstance> instanceMono, ForwardRequest request, Function<ClientResponse, ? extends Mono<V>> responseHandler) {

        final WebClient.RequestBodySpec bodySpec = this.dashboardWebClient.mutateForInstance(instanceMono)
                .method(request.getMethod())
                .uri(request.getUri())
                .headers(headers -> headers.addAll(request.getHeaders()));

        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec;
        if (hashRequestBody(request.getMethod())) {
            headersSpec = bodySpec.body(request.getBody());
        }

        return headersSpec.exchangeToMono(responseHandler).onErrorResume((ex) -> {
            log.error(ex.getMessage(), ex);
            return Mono.error(ex);
        });
    }


    private boolean hashRequestBody(HttpMethod method) {
        return METHODS_HAS_BODY.contains(method);
    }

    @lombok.Data
    @lombok.Builder
    public static class ForwardRequest {

        private final URI uri;

        private final HttpMethod method;

        private final HttpHeaders headers;

        private final BodyInserter<?, ? super ClientHttpRequest> body;

    }
}
