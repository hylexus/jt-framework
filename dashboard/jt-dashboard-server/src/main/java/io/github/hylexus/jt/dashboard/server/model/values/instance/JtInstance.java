package io.github.hylexus.jt.dashboard.server.model.values.instance;

import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class JtInstance {
    protected String instanceId;
    protected JtRegistration registration;

    public JtInstance() {
    }

    public JtInstance(String instanceId) {
        this.instanceId = instanceId;
    }

    // todo delete
    public static JtInstance create(String id) {
        return new JtInstance(id);
    }

    // todo delete
    public JtInstance register(JtRegistration registration) {
        return new JtInstance(this.getInstanceId());
    }
}
