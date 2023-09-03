package io.github.hylexus.jt.dashboard.server.proxy.impl;

import io.github.hylexus.jt.dashboard.server.proxy.DashboardInstanceExchangeFilterFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
public class BuiltinDashboardInstanceExchangeFilterFunctions {

    public static DashboardInstanceExchangeFilterFunction rewritePath() {
        return (instance, request, next) -> {
            if (request.url().isAbsolute()) {
                return next.exchange(request);
            }

            final URI newUri = UriComponentsBuilder.fromHttpUrl(instance.getRegistration().getBaseUrl())
                    .path(request.url().getPath())
                    .query(request.url().getQuery())
                    .build(true).toUri();
            log.debug("Rewrite newUri {} for instance {}", newUri, instance);
            return next.exchange(ClientRequest.from(request).url(newUri).build());
        };
    }
}
