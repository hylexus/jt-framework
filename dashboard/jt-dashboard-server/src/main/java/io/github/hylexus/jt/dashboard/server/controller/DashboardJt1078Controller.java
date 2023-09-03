package io.github.hylexus.jt.dashboard.server.controller;

import io.github.hylexus.jt.core.model.value.DefaultRespCode;
import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.dashboard.server.model.constants.ProtocolType;
import io.github.hylexus.jt.dashboard.server.model.converter.DashboardModelConverter;
import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt1078Registration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt808Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.model.vo.StreamAddressVo;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import io.github.hylexus.jt.exception.ReplayCodeException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/dashboard/1078")
public class DashboardJt1078Controller {
    private final DashboardWebClient dashboardWebClient;
    private final ProxyInstanceProvider instanceProvider;

    public DashboardJt1078Controller(DashboardWebClient.Builder builder, ProxyInstanceProvider instanceProvider) {
        this.dashboardWebClient = builder.build();
        this.instanceProvider = instanceProvider;
    }

    @PostMapping("/video-stream-address")
    public Mono<Resp<StreamAddressVo>> streamAddress(@Validated @RequestBody StreamAddressDto dto) {

        final List<Jt1078Instance> jt1078Instances = this.instanceProvider.getJt1078Instances();
        if (jt1078Instances.isEmpty()) {
            return Mono.error(() -> new ReplayCodeException(Resp.failure(DefaultRespCode.NO_AVAILABLE_SERVER_INSTANCE, "没有可用的 1078 服务实例")));
        }

        final List<Jt808Instance> jt808Instances = this.instanceProvider.getJt808Instances();
        if (jt808Instances.isEmpty()) {
            return Mono.error(() -> new ReplayCodeException(Resp.failure(DefaultRespCode.NO_AVAILABLE_SERVER_INSTANCE, "没有可用的 808 服务实例")));
        }

        final Jt1078Instance jt1078ServerInstance = this.choose1078ServerInstance(jt1078Instances);

        return this.dashboardWebClient.mutateForInstance(choose808ServerInstance(dto, jt808Instances))
                .post()
                .uri("/api/dashboard-client/jt808/command-sender/9101")
                .bodyValue(DashboardModelConverter.convert(dto, jt1078ServerInstance))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Resp<Map<String, Object>>>() {
                })
                .flatMap(resp -> {
                    if (Resp.isFailure(resp)) {
                        return Mono.error(() -> new ReplayCodeException(resp));
                    }

                    final String url = this.constructUrl(dto, jt1078ServerInstance);
                    return Mono.just(Resp.success(new StreamAddressVo().setAddress(url).setType(dto.getProtocolType())));
                });
    }

    private String constructUrl(StreamAddressDto dto, JtInstance jt1078ServerInstance) {
        final boolean http = dto.getProtocolType() == ProtocolType.HTTP || dto.getProtocolType() == ProtocolType.HTTPS;
        final String url = dto.getProtocolType().getScheme()
                + "://"
                + ((Jt1078Registration) jt1078ServerInstance.getRegistration()).getHost()
                + ":"
                + ((Jt1078Registration) jt1078ServerInstance.getRegistration()).getHttpPort()
                + "/api/dashboard-client/jt1078/video-stream/"
                + (http ? "http" : "websocket")
                + "/flv/"
                + dto.getSim() + "/" + dto.getChannelNumber();

        if (dto.getTimeout().toSeconds() > 0) {
            return url + "?timeout=" + dto.getTimeout().toMillis();
        }

        return url;
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
