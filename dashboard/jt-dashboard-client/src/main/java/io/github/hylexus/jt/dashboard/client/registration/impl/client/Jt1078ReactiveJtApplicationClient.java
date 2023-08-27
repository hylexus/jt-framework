package io.github.hylexus.jt.dashboard.client.registration.impl.client;

import org.springframework.web.reactive.function.client.WebClient;

public class Jt1078ReactiveJtApplicationClient extends ReactiveJtApplicationClient {

    public Jt1078ReactiveJtApplicationClient(WebClient webClient) {
        super(webClient, "/api/dashboard/instances/1078");
    }

}
