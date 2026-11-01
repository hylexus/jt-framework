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

// Adapted for jt-framework by hylexus; see the upstream source link below.

package io.github.hylexus.jt.dashboard.server.service;

import io.github.hylexus.jt.dashboard.common.consts.JtDashboardConstants;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/b744d98a50bb31366ef14cb5017a3f4894100e6a/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/services/StatusUpdater.java#L52C15-L52C15">de.codecentric.boot.admin.server.services.StatusUpdater</a>
 */
@Slf4j
public class Jt1078ServerSimpleMetricsUpdater {
    private final Jt1078InstanceRegistry instanceRegistry;
    private final DashboardWebClient dashboardWebClient;

    public Jt1078ServerSimpleMetricsUpdater(Jt1078InstanceRegistry instanceRegistry, DashboardWebClient.Builder builder) {
        this.instanceRegistry = instanceRegistry;
        this.dashboardWebClient = builder.build();
    }

    public Mono<Void> updateMetrics(String id) {
        return this.instanceRegistry.getInstance(id).map(this::doUpdateMetrics).orElseGet(Mono::empty);
    }

    protected Mono<Void> doUpdateMetrics(JtInstance instance) {
        log.debug("Update status for {}", instance);
        return this.dashboardWebClient.mutateForInstance(instance)
                .get()
                .uri("/actuator/jt1078SimpleMetrics")
                .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .doOnNext(resp -> {
                    log.debug("res: {}", resp);
                    resp.putIfAbsent("updatedAt", JtDashboardConstants.YYYY_MM_DD_HH_MM_SS_SSS.format(LocalDateTime.now()));
                    instance.setMetrics(resp);
                })
                .log(log.getName(), Level.FINEST)
                .doOnError((ex) -> log.error(ex.getMessage(), ex))
                .onErrorResume(this::handleError)
                .then();
    }

    protected Mono<Map<String, Object>> handleError(Throwable ex) {
        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());
        details.put("exception", ex.getClass().getName());
        return Mono.just(details);
    }
}
