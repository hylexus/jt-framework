package io.github.hylexus.jt.dashboard.server.service;

import io.github.hylexus.jt.dashboard.server.model.values.instance.DefaultJtInstanceStatus;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstanceStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public interface JtInstanceStatusConverter {
    JtInstanceStatus convert(JtInstance instance);

    JtInstanceStatusConverter DEFAULT = instance -> {
        if (instance.getRegistration().getUpdatedAt() == null) {
            return DefaultJtInstanceStatus.unknown();
        }
        if (Duration.between(instance.getRegistration().getUpdatedAt(), LocalDateTime.now()).toSeconds() > 30) {
            return DefaultJtInstanceStatus.down();
        }
        return DefaultJtInstanceStatus.up();
    };
}
