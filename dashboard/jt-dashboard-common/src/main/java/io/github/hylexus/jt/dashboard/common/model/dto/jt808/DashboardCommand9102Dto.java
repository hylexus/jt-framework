package io.github.hylexus.jt.dashboard.common.model.dto.jt808;

import lombok.Data;

import java.time.Duration;

@Data
public class DashboardCommand9102Dto {

    private String sim;

    private Integer channelNumber;

    private Integer command;

    private Integer mediaTypeToClose;

    private Integer streamType;

    private Duration timeout = Duration.ofSeconds(3);
}
