package io.github.hylexus.jt.dashboard.server.model.values.instance;

import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt808Registration;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Jt808Instance extends JtInstance {

    @Override
    public Jt808Registration getRegistration() {
        return (Jt808Registration) registration;
    }
}
