package io.github.hylexus.jt.jt1078.boot.configuration.ffmpeg;

import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcessManager;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl.DefaultPlatformProcessManager;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;

public class Jt1078FfmpegConfiguration {
    @Bean
    public PlatformProcessManager ffmpegProcessManager() {
        return new DefaultPlatformProcessManager(Executors.newFixedThreadPool(10, new DefaultThreadFactory("ppm")));
    }
}
