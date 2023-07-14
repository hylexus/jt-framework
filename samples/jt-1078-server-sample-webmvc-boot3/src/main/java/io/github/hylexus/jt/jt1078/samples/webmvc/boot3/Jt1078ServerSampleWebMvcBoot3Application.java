package io.github.hylexus.jt.jt1078.samples.webmvc.boot3;

import io.netty.util.ResourceLeakDetector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Jt1078ServerSampleWebMvcBoot3Application {

    public static void main(String[] args) {
        // ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        SpringApplication.run(Jt1078ServerSampleWebMvcBoot3Application.class, args);
    }
}
