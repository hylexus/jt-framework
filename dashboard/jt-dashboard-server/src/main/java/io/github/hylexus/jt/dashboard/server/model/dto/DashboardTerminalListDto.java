package io.github.hylexus.jt.dashboard.server.model.dto;

import io.github.hylexus.jt.dashboard.common.model.dto.jt808.TerminalListDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

// import javax.validation.constraints.NotEmpty;
// import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class DashboardTerminalListDto extends TerminalListDto {
    @NotNull(message = "instanceId is null")
    @NotEmpty(message = "instanceId is empty")
    private String instanceId;
}
