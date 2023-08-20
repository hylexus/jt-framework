package io.github.hylexus.jt.demos.dashboard.boot3.model.dto;

import io.github.hylexus.jt.demos.common.model.dto.jt808.TerminalListDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DashboardTerminalListDto extends TerminalListDto {
    @NotNull(message = "instanceId is null")
    @NotEmpty(message = "instanceId is empty")
    private String instanceId;
}
