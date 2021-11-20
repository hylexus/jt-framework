package io.github.hylexus.jt808.samples.mixedversion;

import io.github.hylexus.jt808.boot.annotation.EnableJt808ServerAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hylexus
 * Created At 2021-11-20 20:05
 */
@SpringBootApplication
@EnableJt808ServerAutoConfig
public class Jt808ServerSampleMixedVersionApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jt808ServerSampleMixedVersionApplication.class, args);
    }

}
