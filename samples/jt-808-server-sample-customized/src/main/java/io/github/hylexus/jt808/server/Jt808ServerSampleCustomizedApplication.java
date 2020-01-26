package io.github.hylexus.jt808.server;

import io.github.hylexus.jt808.boot.annotation.EnableJt808ServerAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hylexus
 * Created At 2019-08-19 22:46
 */
@SpringBootApplication
@EnableJt808ServerAutoConfig
public class Jt808ServerSampleCustomizedApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jt808ServerSampleCustomizedApplication.class, args);
    }

}
