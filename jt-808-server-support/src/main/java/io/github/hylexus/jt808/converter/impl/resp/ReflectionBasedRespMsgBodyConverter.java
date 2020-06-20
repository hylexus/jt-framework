package io.github.hylexus.jt808.converter.impl.resp;

import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import io.github.hylexus.jt.codec.encode.RespMsgEncoder;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.resp.AnnotationBasedCommonReplyMsgBodyWrapper;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-02-02 3:26 下午
 */
@Slf4j
public class ReflectionBasedRespMsgBodyConverter extends AbstractBuiltinRespBodyConverter {

    private final RespMsgEncoder respMsgEncoder;
    private final MsgTypeParser msgTypeParser;

    public ReflectionBasedRespMsgBodyConverter(MsgTypeParser msgTypeParser) {
        this.msgTypeParser = msgTypeParser;
        this.respMsgEncoder = new RespMsgEncoder();
    }

    @Override
    public boolean supportsMsgBody(Object msgBody) {
        final Jt808RespMsgBody annotation = AnnotationUtils.findAnnotation(msgBody.getClass(), Jt808RespMsgBody.class);
        return annotation != null;
    }

    @Override
    public Optional<RespMsgBody> convert(Object msgBody, Jt808Session session, RequestMsgMetadata metadata) {

        final MsgType respMsgType = getRespMsgType(msgBody);
        final byte[] bodyBytes;
        try {
            bodyBytes = this.respMsgEncoder.encodeRespMsgBody(msgBody);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }

        final AnnotationBasedCommonReplyMsgBodyWrapper wrapper = new AnnotationBasedCommonReplyMsgBodyWrapper(respMsgType, bodyBytes);
        log.debug("@Jt808RespMsgBody based RespMsgBody --> {}", wrapper);
        return Optional.of(wrapper);
    }

    private MsgType getRespMsgType(Object msgBody) {
        final Jt808RespMsgBody annotation = AnnotationUtils.findAnnotation(msgBody.getClass(), Jt808RespMsgBody.class);
        assert annotation != null;

        final int msgId = annotation.respMsgId();
        return msgTypeParser.parseMsgType(msgId)
                .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId " + msgId));
    }
}
