package io.github.hylexus.jt.dashboard.client.controller.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class DashboardTerminalVo {
    private String terminalId;

    private String version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastCommunicationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
