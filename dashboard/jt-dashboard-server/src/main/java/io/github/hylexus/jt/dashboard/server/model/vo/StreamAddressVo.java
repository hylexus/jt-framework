package io.github.hylexus.jt.dashboard.server.model.vo;

import io.github.hylexus.jt.dashboard.server.model.constants.ProtocolType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StreamAddressVo {
    private String address;
    private ProtocolType type;
}
