package io.github.hylexus.jt.dashboard.server.proxy;

import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/1433ca5b8343247075ff775d558c9a82341e2ac6/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/web/client/InstanceExchangeFilterFunction.java#L33">de.codecentric.boot.admin.server.web.client.InstanceExchangeFilterFunction</a>
 */
@FunctionalInterface
public interface DashboardInstanceExchangeFilterFunction {

    Mono<ClientResponse> filter(JtInstance instance, ClientRequest request, ExchangeFunction next);

}
