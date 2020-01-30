package io.github.hylexus.jt808.boot.annotation;

import io.github.hylexus.jt808.boot.config.Jt808ServerAutoConfigure;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2020-01-26 2:07 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
//@Import({Jt808ServerAutoConfigure.class})
@ImportAutoConfiguration(Jt808ServerAutoConfigure.class)
public @interface EnableJt808ServerAutoConfig {
}
