package io.github.hylexus.jt808.samples.annotation.config;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-02-02 1:22 下午
 */
@Configuration
public class Jt808Config extends Jt808ServerConfigure {

    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return msgType -> {
            Optional<MsgType> type = Jt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
            return type.isPresent()
                    ? type
                    // 自定义解析器无法解析,使用内置解析器
                    : BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
        };
    }

}
