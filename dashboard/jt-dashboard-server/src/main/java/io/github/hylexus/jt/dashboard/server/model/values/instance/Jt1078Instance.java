package io.github.hylexus.jt.dashboard.server.model.values.instance;

import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt1078Registration;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Jt1078Instance extends JtInstance {
    public Jt1078Instance() {
    }

    @Override
    public Jt1078Registration getRegistration() {
        return (Jt1078Registration) registration;
    }

    @Override
    public String toString() {
        return "Jt1078Instance{"
                + "instanceId='" + instanceId + '\''
                + ", registration.baseUrl=" + registration.getBaseUrl()
                + '}';
    }
}
