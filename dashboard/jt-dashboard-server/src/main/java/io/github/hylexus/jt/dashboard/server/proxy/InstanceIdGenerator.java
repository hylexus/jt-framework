package io.github.hylexus.jt.dashboard.server.proxy;


import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;

public interface InstanceIdGenerator {

    String generateId(JtRegistration registration);

}
