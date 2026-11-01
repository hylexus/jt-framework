/*
 * Copyright 2014-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Adapted for jt-framework by hylexus: JT application models and Dashboard registration.
// Upstream: https://github.com/codecentric/spring-boot-admin/blob/83db63e82e916357d36b1e6b4d552e1b6506ecc9/spring-boot-admin-client/src/main/java/de/codecentric/boot/admin/client/registration/BlockingRegistrationClient.java

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
