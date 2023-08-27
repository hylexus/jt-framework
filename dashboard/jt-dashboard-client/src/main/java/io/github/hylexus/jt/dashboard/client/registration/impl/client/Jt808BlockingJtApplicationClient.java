package io.github.hylexus.jt.dashboard.client.registration.impl.client;

import org.springframework.web.client.RestTemplate;

public class Jt808BlockingJtApplicationClient extends BlockingJtApplicationClient {
    public Jt808BlockingJtApplicationClient(RestTemplate restTemplate) {
        super(restTemplate, "/api/dashboard/instances/808");
    }

}
