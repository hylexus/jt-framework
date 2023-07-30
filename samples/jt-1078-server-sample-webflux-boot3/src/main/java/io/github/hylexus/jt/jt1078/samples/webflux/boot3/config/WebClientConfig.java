package io.github.hylexus.jt.jt1078.samples.webflux.boot3.config;

import io.github.hylexus.jt808.samples.common.service.ProxyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {

    @Bean
    public ProxyService proxyService() {
        return new ProxyService();
    }
}
