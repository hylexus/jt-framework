package io.github.hylexus.jt808.samples.annotation.config;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.boot.config.Jt808ServerConfigurationSupport;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.session.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-02-02 1:22 下午
 */
@Configuration
public class Jt808Configuration extends Jt808ServerConfigurationSupport {

    // [[必须配置]] -- 自定义消息类型解析器
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

    // [非必须配置] -- 可替换内置SessionManager
    @Override
    public Jt808SessionManager supplyJt808SessionManager() {
        return SessionManager.getInstance();
    }

    // [非必须配置] -- 可替换内置Jt808SessionManagerEventListener
    @Override
    public Jt808SessionManagerEventListener supplyJt808SessionManagerEventListener() {
        return new Jt808SessionManagerEventListener() {
            @Override
            public void onSessionClose(@Nullable Jt808Session session, ISessionCloseReason closeReason) {
                Jt808SessionManagerEventListener.super.onSessionClose(session, closeReason);
            }
        };
    }

    // [非必须配置] -- 可替换内置转义逻辑
    @Override
    public BytesEncoder supplyBytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder();
    }

}
