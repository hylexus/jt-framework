package io.github.hylexus.jt.jt808.samples.debug;

import io.netty.util.ResourceLeakDetector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hylexus
 */
@SpringBootApplication
public class Jt808ServerSampleDebugApplication {

    public static void main(String[] args) {
        // FIXME 如果你不了解 ResourceLeakDetector 是做什么的,请务必注释掉下面这行代码!!!
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        SpringApplication.run(Jt808ServerSampleDebugApplication.class, args);
    }

}

