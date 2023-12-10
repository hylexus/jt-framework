package io.github.hylexus.jt.dashboard.common.model.dto.jt808;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Duration;

@Data
@Accessors(chain = true)
public class DashboardCommand9101Dto {

    private String sim;

    private String jt1078ServerIp;

    private Integer jt1078ServerPortTcp;

    private Integer jt1078ServerPortUdp;

    private Integer channelNumber;

    private Integer dataType;

    private Integer streamType;

    private Duration timeout = Duration.ofSeconds(10);
}
