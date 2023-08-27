package io.github.hylexus.jt.dashboard.client.registration.impl.client;

import org.springframework.web.reactive.function.client.WebClient;

public class Jt808ReactiveJtApplicationClient extends ReactiveJtApplicationClient {
    public Jt808ReactiveJtApplicationClient(WebClient webClient) {
        super(webClient, "/api/dashboard/instances/808");
    }

}
