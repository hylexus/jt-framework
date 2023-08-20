package io.github.hylexus.jt.demos.dashboard.boot3.controller;

import io.github.hylexus.jt.demos.common.exception.ReplayCodeException;
import io.github.hylexus.jt.demos.common.model.Resp;
import io.github.hylexus.jt.demos.dashboard.boot3.configuration.props.ServerMetadata;
import io.github.hylexus.jt.demos.dashboard.boot3.model.constants.ProtocolType;
import io.github.hylexus.jt.demos.dashboard.boot3.model.converter.SimpleConverter;
import io.github.hylexus.jt.demos.dashboard.boot3.model.dto.StreamAddressDto;
import io.github.hylexus.jt.demos.dashboard.boot3.model.vo.StreamAddressVo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/dashboard/1078")
public class DashboardJt1078Controller {
    private final ServerMetadata serverMetadata;
    private final WebClient.Builder builder;

    public DashboardJt1078Controller(ServerMetadata serverMetadata, WebClient.Builder builder) {
        this.serverMetadata = serverMetadata;
        this.builder = builder;
    }

    @PostMapping("/stream-address")
    public Mono<StreamAddressVo> streamAddress(@Validated @RequestBody StreamAddressDto dto) {

        final ServerMetadata.Jt1078ServerMetadata jt1078ServerInstance = this.choose1078ServerInstance();

        return this.builder.baseUrl(choose808ServerInstance(dto).getBaseUrl()).build()
                .post()
                .uri("/jt808/send-msg/9101")
                .bodyValue(SimpleConverter.convert(dto, jt1078ServerInstance))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Resp<Map<String, Object>>>() {
                })
                .flatMap(resp -> {
                    if (Resp.isFailure(resp)) {
                        return Mono.error(() -> new ReplayCodeException(resp));
                    }

                    final String url = this.constructUrl(dto, jt1078ServerInstance);
                    return Mono.just(new StreamAddressVo().setAddress(url).setType(dto.getProtocolType()));
                });
    }

    private String constructUrl(StreamAddressDto dto, ServerMetadata.Jt1078ServerMetadata jt1078ServerInstance) {
        final boolean http = dto.getProtocolType() == ProtocolType.HTTP || dto.getProtocolType() == ProtocolType.HTTPS;
        return dto.getProtocolType().getScheme()
                + "://"
                + jt1078ServerInstance.getHost()
                + ":"
                + jt1078ServerInstance.getHttpPort()
                + "/jt1078/subscription/"
                + (http ? "http" : "websocket")
                + "/flv/"
                + dto.getSim() + "/" + dto.getChannelNumber()
                + "?timeout=" + dto.getTimeout().toString()
                + "&autoSend9101Command=false";
    }

    private ServerMetadata.Jt1078ServerMetadata choose1078ServerInstance() {
        final int size = this.serverMetadata.getJt1078ServerMetadata().size();
        return this.serverMetadata.getJt1078ServerMetadata().get(ThreadLocalRandom.current().nextInt(size));
    }

    private ServerMetadata.Jt808ServerMetadata choose808ServerInstance(StreamAddressDto dto) {
        return this.serverMetadata.getJt808ServerMetadata().stream()
                .filter(it -> it.getInstanceId().equals(dto.getInstanceId()))
                .findFirst()
                .orElseThrow(() -> new ReplayCodeException(Resp.paramError("No Jt808ServerInstance found with id : " + dto.getInstanceId())));
    }
}
