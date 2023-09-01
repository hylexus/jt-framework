package io.github.hylexus.jt.jt808.samples.debug.config;

import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 */
@Slf4j
@Configuration
public class MyJt808Config {

    @Bean
    public Jt808RequestRouteExceptionHandler jt808RequestRouteExceptionHandler() {
        return (msgId, payload) ->
                log.error("收到未知类型的消息: msgId = {}({}), payload={}", msgId, HexStringUtils.int2HexString(msgId, 4), HexStringUtils.byteBufToString(payload));
    }

}
