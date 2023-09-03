package io.github.hylexus.jt.jt1078.samples.webflux.boot3.controller;

import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt808.samples.common.dto.Command9101Dto;
import io.github.hylexus.jt808.samples.common.dto.Command9102Dto;
import io.github.hylexus.jt808.samples.common.service.ProxyService;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequestMapping
@RestController
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping("/proxy/808/terminal/list")
    public Mono<Resp<Object>> terminalList(@Validated ProxyCommonParams commonParams) {
        return this.proxyService.terminalList(this.buildWebClient(commonParams));
    }

    @RequestMapping("/proxy/1078/send-msg/9101")
    public Mono<Resp<Object>> realtimeTransmissionRequest(
            @Validated ProxyCommonParams commonParams,
            @Validated @RequestBody Command9101Dto dto) {

        return this.proxyService.proxy0x9101Msg(this.buildWebClient(commonParams), dto);
    }

    @RequestMapping("/proxy/1078/send-msg/9102")
    public Mono<Resp<Object>> realtimeTransmissionControl(
            @Validated ProxyCommonParams commonParams,
            @Validated @RequestBody Command9102Dto dto) {

        return this.proxyService.proxy0x9102Msg(this.buildWebClient(commonParams), dto);
    }

    private WebClient buildWebClient(ProxyCommonParams commonParams) {
        return WebClient.builder()
                .baseUrl("http://" + commonParams.host() + ":" + commonParams.port())
                .build();
    }

    public record ProxyCommonParams(
            @NotNull(message = "host is null") String host,
            @NotNull(message = "port is null") Integer port) {
    }
}
