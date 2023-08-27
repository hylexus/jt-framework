package io.github.hylexus.jt.dashboard.server.controller;

import io.github.hylexus.jt.dashboard.common.exception.ReplayCodeException;
import io.github.hylexus.jt.dashboard.server.model.constants.ProtocolType;
import io.github.hylexus.jt.dashboard.server.model.converter.SimpleConverter;
import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt1078Registration;
import io.github.hylexus.jt.dashboard.server.model.vo.StreamAddressVo;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt808Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import io.github.hylexus.jt.model.value.Resp;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/dashboard/1078")
public class DashboardJt1078Controller {
    private final WebClient.Builder builder;
    private final ProxyInstanceProvider instanceProvider;

    public DashboardJt1078Controller(WebClient.Builder builder, ProxyInstanceProvider instanceProvider) {
        this.builder = builder;
        this.instanceProvider = instanceProvider;
    }

    @PostMapping("/stream-address")
    public Mono<StreamAddressVo> streamAddress(@Validated @RequestBody StreamAddressDto dto) {

        final List<Jt1078Instance> instances = this.instanceProvider.getJt1078Instances();
        final Jt1078Instance jt1078ServerInstance = this.choose1078ServerInstance(instances);
        final List<Jt808Instance> jt808Instances = this.instanceProvider.getJt808Instances();
        return this.builder.baseUrl(choose808ServerInstance(dto, jt808Instances).getRegistration().getBaseUrl()).build()
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

    private String constructUrl(StreamAddressDto dto, JtInstance jt1078ServerInstance) {
        final boolean http = dto.getProtocolType() == ProtocolType.HTTP || dto.getProtocolType() == ProtocolType.HTTPS;
        return dto.getProtocolType().getScheme()
                + "://"
                + ((Jt1078Registration) jt1078ServerInstance.getRegistration()).getHost()
                + ":"
                + ((Jt1078Registration) jt1078ServerInstance.getRegistration()).getHttpPort()
                + "/jt1078/subscription/"
                + (http ? "http" : "websocket")
                + "/flv/"
                + dto.getSim() + "/" + dto.getChannelNumber()
                + "?timeout=" + dto.getTimeout().toString()
                + "&autoSend9101Command=false";
    }

    private Jt1078Instance choose1078ServerInstance(List<Jt1078Instance> serverInstances) {
        final int size = serverInstances.size();
        return serverInstances.get(ThreadLocalRandom.current().nextInt(size));
    }

    private Jt808Instance choose808ServerInstance(StreamAddressDto dto, List<Jt808Instance> serverInstances) {
        return serverInstances.stream()
                .filter(it -> it.getInstanceId().equals(dto.getInstanceId()))
                .findFirst()
                .orElseThrow(() -> new ReplayCodeException(Resp.parameterError("No Jt808ServerInstance found with id : " + dto.getInstanceId())));
    }
}
