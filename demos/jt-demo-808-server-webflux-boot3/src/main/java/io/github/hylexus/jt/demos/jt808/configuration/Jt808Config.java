package io.github.hylexus.jt.demos.jt808.configuration;

import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
public class Jt808Config {

    public enum MyMsgType implements MsgType {
        CLIENT_REGISTER(0x0100, "终端注册"),
        CLIENT_AUTH(0x0102, "终端鉴权"),
        MSG_0X_1205(0x1205, "0x1205"),
        // ...
        // TODO 这里扩展其他类型
        ;

        private final int msgId;
        private final String desc;

        MyMsgType(int msgId, String desc) {
            this.msgId = msgId;
            this.desc = desc;
        }

        private static final Map<Integer, MsgType> mappings = new HashMap<>(values().length);

        static {
            for (MyMsgType value : values()) {
                mappings.put(value.msgId, value);
            }
        }

        @Override
        public Optional<MsgType> parseFromInt(int msgId) {
            return Optional.ofNullable(mappings.get(msgId));
        }

        @Override
        public int getMsgId() {
            return msgId;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }

    // [[ 必须配置 ]] -- 提供自定义的 Jt808MsgTypeParser
    @Bean
    public Jt808MsgTypeParser jt808MsgTypeParser() {
        // 优先使用自定义类型解析
        return msgId -> MyMsgType.CLIENT_AUTH.parseFromInt(msgId)
                // 使用内置类型解析
                .or(() -> BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgId));
    }
}
