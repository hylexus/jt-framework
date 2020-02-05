package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.TerminalCommonReplyMsgBody;
import io.github.hylexus.oaks.utils.IntBitOps;

import java.util.Optional;

/**
 * 终端通用应答
 *
 * @author hylexus
 * Created At 2020-02-02 11:19 上午
 */
@BuiltinComponent
public class BuiltinTerminalCommonReplyMsgBodyConverter implements RequestMsgBodyConverter<TerminalCommonReplyMsgBody> {

    // 0,成功/确认;其他,失败;
    public static final byte RESULT_SUCCESS = 0;

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    @Override
    public Optional<TerminalCommonReplyMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        final byte[] bytes = metadata.getBodyBytes();

        final TerminalCommonReplyMsgBody body = new TerminalCommonReplyMsgBody();
        body.setReplyFlowId(IntBitOps.intFrom2Bytes(bytes, 0));
        body.setReplyMsgId(IntBitOps.intFrom2Bytes(bytes, 2));
        body.setResult(bytes[4]);

        return Optional.of(body);
    }
}
