package io.github.hylexus.jt.dashboard.server.model.values.instance;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class JtInstance {

    protected String instanceId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    protected LocalDateTime latestCommunicationTime = LocalDateTime.now();

    protected JtRegistration registration;

}
