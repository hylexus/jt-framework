package io.github.hylexus.jt.dashboard.server.model.values.instance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class JtInstance {

    protected String instanceId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    protected LocalDateTime createdAt;

    protected JtRegistration registration;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Map<String, Object> metrics;
    protected JtInstanceStatus status = DefaultJtInstanceStatus.unknown();

}
