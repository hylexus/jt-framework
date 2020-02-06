package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinTerminalCommonReplyMsgBody;
import io.github.hylexus.oaks.utils.IntBitOps;

import java.util.Optional;

/**
 * 终端通用应答
 *
 * @author hylexus
 * Created At 2020-02-02 11:19 上午
 */
@BuiltinComponent
public class BuiltinTerminalCommonReplyRequestMsgBodyConverter extends AbstractBuiltinRequestMsgBodyConverter<BuiltinTerminalCommonReplyMsgBody> {

    // 0,成功/确认;其他,失败;
    public static final byte RESULT_SUCCESS = 0;

    @Override
    public Optional<BuiltinTerminalCommonReplyMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        final byte[] bytes = metadata.getBodyBytes();

        final BuiltinTerminalCommonReplyMsgBody body = new BuiltinTerminalCommonReplyMsgBody();
        body.setReplyFlowId(IntBitOps.intFrom2Bytes(bytes, 0));
        body.setReplyMsgId(IntBitOps.intFrom2Bytes(bytes, 2));
        body.setResult(bytes[4]);

        return Optional.of(body);
    }
}
