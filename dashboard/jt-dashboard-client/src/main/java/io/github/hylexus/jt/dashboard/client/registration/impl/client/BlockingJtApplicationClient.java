package io.github.hylexus.jt.dashboard.client.registration.impl.client;

import io.github.hylexus.jt.dashboard.client.registration.Jt1078Application;
import io.github.hylexus.jt.dashboard.client.registration.Jt808Application;
import io.github.hylexus.jt.dashboard.client.registration.JtApplication;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationClient;
import io.github.hylexus.jt.core.model.value.Resp;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class BlockingJtApplicationClient implements JtApplicationClient {
    private final RestTemplate restTemplate;
    private final String apiPrefix;

    public BlockingJtApplicationClient(RestTemplate restTemplate, String apiPrefix) {
        this.restTemplate = restTemplate;
        this.apiPrefix = apiPrefix;
    }

    @Override
    public String register(String dashboardUrl, JtApplication application) {
        if (application instanceof Jt808Application) {
            application.setType("JT_808");
        } else if (application instanceof Jt1078Application) {
            application.setType("JT_1078");
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<JtApplication> request = new HttpEntity<>(application, headers);
        final ResponseEntity<Resp<Map<String, Object>>> response = restTemplate.exchange(
                dashboardUrl + "/" + apiPrefix,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Resp<Map<String, Object>>>() {
                }
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            final Resp<Map<String, Object>> body = response.getBody();
            if (Resp.isSuccess(body)) {
                return body.getData().get("id").toString();
            }
        }

        return null;
    }

    @Override
    public void deregister(String dashboardUrl, String id) {
        this.restTemplate.delete(dashboardUrl + apiPrefix + "/" + id);
    }
}
