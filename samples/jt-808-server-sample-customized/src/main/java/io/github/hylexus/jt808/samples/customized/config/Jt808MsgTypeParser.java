package io.github.hylexus.jt808.samples.customized.config;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.converter.MsgTypeParser;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-22 3:39 下午
 */
public class Jt808MsgTypeParser implements MsgTypeParser {

    @Override
    public Optional<MsgType> parseMsgType(int msgType) {
        // 先使用自定义解析器
        Optional<MsgType> type = Jt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
        return type.isPresent()
                ? type
                // 自定义解析器无法解析,使用内置解析器
                : BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
    }

}
