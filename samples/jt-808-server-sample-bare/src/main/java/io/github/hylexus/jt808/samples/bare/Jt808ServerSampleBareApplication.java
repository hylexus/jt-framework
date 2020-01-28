package io.github.hylexus.jt808.samples.bare;

import io.github.hylexus.jt808.boot.annotation.EnableJt808ServerAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJt808ServerAutoConfig
public class Jt808ServerSampleBareApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jt808ServerSampleBareApplication.class, args);
    }

}

