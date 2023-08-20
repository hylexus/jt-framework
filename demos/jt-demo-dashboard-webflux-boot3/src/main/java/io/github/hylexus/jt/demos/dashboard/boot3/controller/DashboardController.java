package io.github.hylexus.jt.demos.dashboard.boot3.controller;

import io.github.hylexus.jt.demos.dashboard.boot3.configuration.props.ServerMetadata;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@EnableConfigurationProperties({
        ServerMetadata.class,
})
public class DashboardController {

    private final ServerMetadata serverMetadata;

    public DashboardController(ServerMetadata serverMetadata) {
        this.serverMetadata = serverMetadata;
    }

    @RequestMapping("/server-metadata")
    public ServerMetadata serverMetadata() {
        return serverMetadata;
    }

}
