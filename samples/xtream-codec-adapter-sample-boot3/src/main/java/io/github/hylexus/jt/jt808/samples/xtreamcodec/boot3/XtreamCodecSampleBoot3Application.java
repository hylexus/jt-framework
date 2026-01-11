package io.github.hylexus.jt.jt808.samples.xtreamcodec.boot3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XtreamCodecSampleBoot3Application {

    public static void main(String[] args) {
        // FIXME 如果你不了解 ResourceLeakDetector 是做什么的,请务必注释掉下面这行代码!!!
        // io.netty.util.ResourceLeakDetector.setLevel(io.netty.util.ResourceLeakDetector.Level.PARANOID);
        SpringApplication.run(XtreamCodecSampleBoot3Application.class, args);
    }

}
