package io.github.hylexus.jt808.samples.common.service;

import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt808.samples.common.dto.Command9101Dto;
import io.github.hylexus.jt808.samples.common.dto.Command9102Dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 示例代码而已，写的很菜，将就着看吧。
 * <p>
 * 这个类的作用是什么？？？
 * <p>
 * 就是一个远程调用的代理服务。
 * <p>
 * 为了调试简单才这么做的，正常情况下你应该使用类似于 Open-Feign 或其他的组件来远程调用 808 服务。
 * <p>
 * 当然，你可以将 808 和 1078 的逻辑放在同一个服务里，这样就避免了远程调用（不推荐）。
 */
@Slf4j
public class ProxyService {
    public Mono<Resp<Object>> terminalList(WebClient webClient) {

        return webClient
                .get()
                .uri("/jt808/terminal/list")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Resp<Object>>() {
                })
                .doOnError(Throwable.class, throwable -> log.error(throwable.getMessage(), throwable));
    }

    public Mono<Resp<Object>> proxy0x9101Msg(WebClient webClient, Command9101Dto command9101Dto) {
        return proxyJsonRequest(webClient, command9101Dto, "/jt808/send-msg/9101");
    }

    public Mono<Resp<Object>> proxy0x9102Msg(WebClient webClient, Command9102Dto command9102Dto) {
        return proxyJsonRequest(webClient, command9102Dto, "/jt808/send-msg/9102");
    }

    private Mono<Resp<Object>> proxyJsonRequest(WebClient webClient, Object body, String uri) {
        return webClient
                .post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Resp<Object>>() {
                })
                .doOnError(Throwable.class, throwable -> log.error(throwable.getMessage(), throwable));
    }
}
