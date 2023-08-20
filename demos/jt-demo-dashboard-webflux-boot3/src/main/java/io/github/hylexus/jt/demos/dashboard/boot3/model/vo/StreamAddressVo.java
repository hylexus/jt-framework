package io.github.hylexus.jt.demos.dashboard.boot3.model.vo;

import io.github.hylexus.jt.demos.dashboard.boot3.model.constants.ProtocolType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StreamAddressVo {
    private String address;
    private ProtocolType type;
}
