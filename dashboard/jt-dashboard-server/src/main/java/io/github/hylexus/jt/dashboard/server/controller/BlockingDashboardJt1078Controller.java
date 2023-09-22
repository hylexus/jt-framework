package io.github.hylexus.jt.dashboard.server.controller;

import io.github.hylexus.jt.core.model.value.DefaultRespCode;
import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.dashboard.server.common.utils.DashboardUtils;
import io.github.hylexus.jt.dashboard.server.model.converter.DashboardModelConverter;
import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt808Instance;
import io.github.hylexus.jt.dashboard.server.model.vo.StreamAddressVo;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import io.github.hylexus.jt.dashboard.server.service.servlet.BlockingJt1078InstanceChooser;
import io.github.hylexus.jt.exception.ReplayCodeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/1078")
public class BlockingDashboardJt1078Controller {
    private final DashboardWebClient dashboardWebClient;
    private final ProxyInstanceProvider instanceProvider;
    private final BlockingJt1078InstanceChooser jt1078InstanceChooser;

    public BlockingDashboardJt1078Controller(DashboardWebClient.Builder builder, ProxyInstanceProvider instanceProvider, BlockingJt1078InstanceChooser jt1078InstanceChooser) {
        this.dashboardWebClient = builder.build();
        this.instanceProvider = instanceProvider;
        this.jt1078InstanceChooser = jt1078InstanceChooser;
    }

    @PostMapping("/video-stream-address")
    public Resp<StreamAddressVo> streamAddress(HttpServletRequest request, @Validated @RequestBody StreamAddressDto dto) {

        final List<Jt1078Instance> jt1078Instances = this.instanceProvider.getJt1078Instances();
        if (jt1078Instances.isEmpty()) {
            throw new ReplayCodeException(Resp.failure(DefaultRespCode.NO_AVAILABLE_SERVER_INSTANCE, "没有可用的 1078 服务实例"));
        }

        final List<Jt808Instance> jt808Instances = this.instanceProvider.getJt808Instances();
        if (jt808Instances.isEmpty()) {
            throw new ReplayCodeException(Resp.failure(DefaultRespCode.NO_AVAILABLE_SERVER_INSTANCE, "没有可用的 808 服务实例"));
        }

        final Jt1078Instance jt1078ServerInstance = this.jt1078InstanceChooser.chooseInstance(request, dto, jt1078Instances).orElseThrow(() -> new ReplayCodeException(Resp.failure(DefaultRespCode.NO_AVAILABLE_SERVER_INSTANCE, "没有可用的 1078 服务实例")));
        final Resp<Map<String, Object>> resp = this.dashboardWebClient.mutateForInstance(choose808ServerInstance(dto, jt808Instances))
                .post()
                .uri("/api/dashboard-client/jt808/command-sender/9101")
                .bodyValue(DashboardModelConverter.convert(dto, jt1078ServerInstance))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Resp<Map<String, Object>>>() {
                }).block();

        if (Resp.isFailure(resp)) {
            throw new ReplayCodeException(resp);
        }

        final String url = DashboardUtils.constructFlvPlayerUrl(dto, jt1078ServerInstance);
        return Resp.success(new StreamAddressVo().setAddress(url).setType(dto.getProtocolType()));

    }

    private Jt808Instance choose808ServerInstance(StreamAddressDto dto, List<Jt808Instance> serverInstances) {
        return serverInstances.stream()
                .filter(it -> it.getInstanceId().equals(dto.getInstanceId()))
                .findFirst()
                .orElseThrow(() -> new ReplayCodeException(Resp.parameterError("No Jt808ServerInstance found with id : " + dto.getInstanceId())));
    }
}
