package io.github.hylexus.jt.dashboard.client.controller.model.dto;

import io.github.hylexus.jt.core.model.dto.SimplePageableDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardSessionListDto extends SimplePageableDto {
    private boolean withSubscribers = false;
    private String sim;
    private short channelNumber = -1;
}
