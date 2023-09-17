package io.github.hylexus.jt.dashboard.server.service.servlet;

import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.service.Jt1078InstanceChooser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface BlockingJt1078InstanceChooser extends Jt1078InstanceChooser {
    Optional<Jt1078Instance> chooseInstance(HttpServletRequest request, StreamAddressDto dto, List<Jt1078Instance> availableInstances);
}
