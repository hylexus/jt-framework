package io.github.hylexus.jt.dashboard.client.registration.impl.client;

import io.github.hylexus.jt.dashboard.client.registration.Jt1078Application;
import io.github.hylexus.jt.dashboard.client.registration.Jt808Application;
import io.github.hylexus.jt.dashboard.client.registration.JtApplication;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationClient;
import io.github.hylexus.jt.core.model.value.Resp;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public abstract class ReactiveJtApplicationClient implements JtApplicationClient {
    private final WebClient webClient;
    private final String apiPrefix;

    public ReactiveJtApplicationClient(WebClient webClient, String apiPrefix) {
        this.webClient = webClient;
        this.apiPrefix = apiPrefix;
    }

    @Override
    public String register(String dashboardUrl, JtApplication application) {

        if (application instanceof Jt808Application) {
            application.setType("JT_808");
        } else if (application instanceof Jt1078Application) {
            application.setType("JT_1078");
        }

        final Resp<Map<String, Object>> resp = this.webClient.post()
                .uri(dashboardUrl + apiPrefix)
                .bodyValue(application)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Resp<Map<String, Object>>>() {
                })
                .block();
        if (Resp.isSuccess(resp)) {
            return resp.getData().get("id").toString();
        }
        return null;
    }

    @Override
    public void deregister(String dashboardUrl, String id) {
        this.webClient.delete()
                .uri(dashboardUrl + apiPrefix + "/" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
