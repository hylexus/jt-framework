package io.github.hylexus.jt808.samples.annotation;

import io.github.hylexus.jt808.boot.annotation.EnableJt808ServerAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hylexus
 * Created At 2020-01-28 8:47 下午
 */
@SpringBootApplication
@EnableJt808ServerAutoConfig
public class Jt808ServerSampleAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jt808ServerSampleAnnotationApplication.class, args);
    }

}
