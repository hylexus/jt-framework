package io.github.hylexus.jt.dashboard.common.model.dto.jt808;

import io.github.hylexus.jt.core.model.dto.SimplePageableDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DashboardTerminalListDto extends SimplePageableDto {
    private String terminalId;
    private String version;
}
