package io.github.hylexus.jt808.samples.annotation.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TerminalVo {
    private String terminalId;

    private String version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastCommunicationTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String latestGeo;
}
