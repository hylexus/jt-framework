package io.github.hylexus.jt.demos.dashboard.boot3.controller;

import io.github.hylexus.jt.demos.common.exception.ReplayCodeException;
import io.github.hylexus.jt.demos.common.model.Resp;
import io.github.hylexus.jt.demos.dashboard.boot3.configuration.props.ServerMetadata;
import io.github.hylexus.jt.demos.dashboard.boot3.model.dto.DashboardTerminalListDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/808")
public class DashboardJt808Controller {
    private final ServerMetadata serverMetadata;
    private final WebClient.Builder builder;

    public DashboardJt808Controller(ServerMetadata serverMetadata, WebClient.Builder builder) {
        this.serverMetadata = serverMetadata;
        this.builder = builder;
    }

    @RequestMapping("/terminal/list")
    public Mono<Resp<Map<String, Object>>> streamAddress(@Validated DashboardTerminalListDto dto) {

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("instanceId", dto.getInstanceId());
        params.add("version", dto.getVersion());
        params.add("terminalId", dto.getTerminalId());
        params.add("pageSize", String.valueOf(dto.getPageSize()));
        params.add("page", String.valueOf(dto.getPage()));

        return this.builder.baseUrl(choose808ServerInstance(dto).getBaseUrl()).build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/jt808/terminal/list").queryParams(params).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Resp<Map<String, Object>>>() {
                });
    }

    private ServerMetadata.Jt808ServerMetadata choose808ServerInstance(DashboardTerminalListDto dto) {
        return this.serverMetadata.getJt808ServerMetadata().stream().filter(it -> it.getInstanceId().equals(dto.getInstanceId()))
                .findFirst()
                .orElseThrow(() -> new ReplayCodeException(Resp.paramError("No Jt808ServerInstance found with id : " + dto.getInstanceId())));
    }
}
