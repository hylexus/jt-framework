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
// Upstream: https://github.com/codecentric/spring-boot-admin/blob/83db63e82e916357d36b1e6b4d552e1b6506ecc9/spring-boot-admin-client/src/main/java/de/codecentric/boot/admin/client/registration/DefaultApplicationRegistrator.java

package io.github.hylexus.jt.dashboard.client.registration.impl.registrator;

import io.github.hylexus.jt.dashboard.client.registration.JtApplication;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationClient;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationFactory;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationRegistrator;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class DefaultJtApplicationRegistrator implements JtApplicationRegistrator {

    private final JtApplicationFactory applicationFactory;
    private final JtApplicationClient registrationClient;
    private final AtomicReference<String> registeredId = new AtomicReference<>();
    private final ConcurrentHashMap<String, LongAdder> attempts = new ConcurrentHashMap<>();
    private final String[] dashboardUrls;
    private final boolean registerOnce;

    public DefaultJtApplicationRegistrator(JtApplicationFactory applicationFactory, JtApplicationClient registrationClient, String[] dashboardUrl, boolean registerOnce) {
        this.applicationFactory = applicationFactory;
        this.registrationClient = registrationClient;
        this.dashboardUrls = dashboardUrl;
        this.registerOnce = registerOnce;
    }

    /**
     * @return true 至少有一个地址注册成功
     */
    @Override
    public boolean register() {
        final JtApplication application = this.applicationFactory.createApplication();
        boolean isRegistrationSuccessful = false;

        for (final String dashboardUrl : this.dashboardUrls) {
            final LongAdder attempt = this.attempts.computeIfAbsent(dashboardUrl, (k) -> new LongAdder());
            boolean successful = register(application, dashboardUrl, attempt.intValue() == 0);

            if (!successful) {
                attempt.increment();
            } else {
                attempt.reset();
                isRegistrationSuccessful = true;
                if (this.registerOnce) {
                    break;
                }
            }
        }

        return isRegistrationSuccessful;
    }

    protected boolean register(JtApplication application, String dashboardUrl, boolean firstAttempt) {
        try {
            final String id = this.registrationClient.register(dashboardUrl, application);
            if (this.registeredId.compareAndSet(null, id)) {
                log.info("Application registered itself as {}", id);
            } else {
                log.debug("Application refreshed itself as {}", id);
            }
            return true;
        } catch (Exception ex) {
            if (firstAttempt) {
                log.warn("Failed to register application as {} at jt-dashboard-server ({}): {}. Further attempts are logged on DEBUG level", application, this.dashboardUrls, ex.getMessage());
            } else {
                log.debug("Failed to register application as {} at jt-dashboard-server ({}): {}", application, this.dashboardUrls, ex.getMessage());
            }
            return false;
        }
    }

    @Override
    public void deregister() {
        final String id = this.registeredId.get();
        if (id == null) {
            return;
        }

        for (String url : this.dashboardUrls) {
            try {
                this.registrationClient.deregister(url, id);
                this.registeredId.compareAndSet(id, null);
                if (this.registerOnce) {
                    break;
                }
            } catch (Exception ex) {
                log.warn("Failed to deregister application (id={}) at jt-dashboard-server ({}): {}", id, url, ex.getMessage());
            }
        }
    }
}
