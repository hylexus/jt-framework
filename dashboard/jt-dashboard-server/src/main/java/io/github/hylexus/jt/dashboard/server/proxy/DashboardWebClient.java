package io.github.hylexus.jt.dashboard.server.proxy;

import io.github.hylexus.jt.dashboard.server.common.execption.JtInstanceNotFoundException;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/1433ca5b8343247075ff775d558c9a82341e2ac6/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/web/client/InstanceWebClient.java#L31">de.codecentric.boot.admin.server.web.client.InstanceWebClient</a>
 */
@Slf4j
public class DashboardWebClient {

    public static final String ATTR_KEY_JT_SERVER_INSTANCE = "jtServerInstance";

    private final WebClient webClient;

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(WebClient.Builder builder) {
        return new Builder(builder);
    }

    private DashboardWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClient mutateForInstance(JtInstance instance) {
        return this.mutateForInstance(Mono.just(instance));
    }

    public WebClient mutateForInstance(Mono<JtInstance> instanceMono) {
        return this.webClient.mutate()
                .filters(filters -> filters.add(0, setInstance(instanceMono)))
                .build();
    }

    private static ExchangeFilterFunction setInstance(Mono<JtInstance> instance) {
        return (request, next) -> instance
                .map((ins) -> ClientRequest.from(request).attribute(ATTR_KEY_JT_SERVER_INSTANCE, ins).build())
                .switchIfEmpty(Mono.error(() -> new JtInstanceNotFoundException("Can not found JtServerInstance")))
                .flatMap(next::exchange);
    }

    private static ExchangeFilterFunction toExchangeFilterFunction(DashboardInstanceExchangeFilterFunction filter) {
        return (request, next) -> request.attribute(ATTR_KEY_JT_SERVER_INSTANCE)
                .filter(JtInstance.class::isInstance)
                .map(JtInstance.class::cast)
                .map((instance) -> filter.filter(instance, request, next))
                .orElseGet(() -> next.exchange(request));
    }

    public static class Builder {

        private List<DashboardInstanceExchangeFilterFunction> filters = new ArrayList<>();

        private WebClient.Builder webClientBuilder;

        public Builder() {
            this(WebClient.builder());
        }

        public Builder(WebClient.Builder webClientBuilder) {
            this.webClientBuilder = webClientBuilder;
        }

        protected Builder(Builder other) {
            this.filters = new ArrayList<>(other.filters);
            this.webClientBuilder = other.webClientBuilder.clone();
        }

        public Builder filter(DashboardInstanceExchangeFilterFunction filter) {
            this.filters.add(filter);
            return this;
        }

        public Builder filters(Consumer<List<DashboardInstanceExchangeFilterFunction>> filtersConsumer) {
            filtersConsumer.accept(this.filters);
            return this;
        }

        public Builder webClient(WebClient.Builder builder) {
            this.webClientBuilder = builder;
            return this;
        }

        public DashboardWebClient build() {
            this.filters.stream()
                    .map(DashboardWebClient::toExchangeFilterFunction)
                    .forEach(this.webClientBuilder::filter);
            return new DashboardWebClient(this.webClientBuilder.build());
        }

        @Override
        public Builder clone() {
            return new Builder(this);
        }
    }
}
