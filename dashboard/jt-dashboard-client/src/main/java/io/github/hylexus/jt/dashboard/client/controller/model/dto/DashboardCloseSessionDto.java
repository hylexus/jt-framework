package io.github.hylexus.jt.dashboard.client.controller.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardCloseSessionDto {
    private List<String> sessionIdList;
    // or
    private List<String> simList;
}
