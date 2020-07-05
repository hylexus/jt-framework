package io.github.hylexus.jt808.boot.config;

import io.github.hylexus.jt808.converter.BuiltinMsgTypeParser;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author hylexus
 * Created At 2019-08-26 9:14 下午
 */
@Slf4j
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE - 100)
@ConditionalOnMissingBean(Jt808ServerConfigurationSupport.class)
public class DefaultJt808ServerAutoConfigure extends Jt808ServerConfigurationSupport {

    public DefaultJt808ServerAutoConfigure() {
        log.info("<<< DefaultJt808ServerAutoConfigure init ... >>>");
    }

    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return new BuiltinMsgTypeParser();
    }
}
