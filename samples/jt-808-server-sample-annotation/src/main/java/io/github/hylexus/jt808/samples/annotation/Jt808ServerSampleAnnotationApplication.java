package io.github.hylexus.jt808.samples.annotation;

import io.netty.util.ResourceLeakDetector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hylexus
 */
@SpringBootApplication
public class Jt808ServerSampleAnnotationApplication {

    public static void main(String[] args) {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        SpringApplication.run(Jt808ServerSampleAnnotationApplication.class, args);
    }

}
