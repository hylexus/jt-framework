package io.github.hylexus.jt808.samples.mixedversion.config;

import io.github.hylexus.jt808.boot.config.Jt808ServerConfigurationSupport;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 * Created At 2019-09-22 3:43 下午
 */
@Slf4j
@Configuration
public class Jt808Configuration extends Jt808ServerConfigurationSupport {

    // [[必须配置]] -- 自定义消息类型解析器
    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return new Jt808MsgTypeParser();
    }

}
