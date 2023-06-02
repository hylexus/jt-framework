package io.github.hylexus.jt.jt1078.samples.webflux.boot3;

import io.netty.util.ResourceLeakDetector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hylexus
 */
@SpringBootApplication
public class Jt1078ServerSampleWebFluxBoot3Application {

    public static void main(String[] args) {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        SpringApplication.run(Jt1078ServerSampleWebFluxBoot3Application.class, args);
    }

}
