package io.github.hylexus.jt.dashboard.client.registration.impl.client;

import org.springframework.web.client.RestTemplate;

public class Jt1078BlockingJtApplicationClient extends BlockingJtApplicationClient {

    public Jt1078BlockingJtApplicationClient(RestTemplate restTemplate) {
        super(restTemplate, "/api/dashboard/instances/1078");
    }

}
