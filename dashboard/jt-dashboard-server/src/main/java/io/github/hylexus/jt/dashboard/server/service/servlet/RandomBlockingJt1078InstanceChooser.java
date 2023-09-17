package io.github.hylexus.jt.dashboard.server.service.servlet;

import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBlockingJt1078InstanceChooser implements BlockingJt1078InstanceChooser {
    @Override
    public Optional<Jt1078Instance> chooseInstance(HttpServletRequest request, StreamAddressDto dto, List<Jt1078Instance> availableInstances) {
        final int size = availableInstances.size();
        if (size == 0) {
            return Optional.empty();
        }
        return Optional.of(availableInstances.get(ThreadLocalRandom.current().nextInt(size)));
    }
}
