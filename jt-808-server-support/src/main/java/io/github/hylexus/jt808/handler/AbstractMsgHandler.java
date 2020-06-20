package io.github.hylexus.jt808.handler;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.codec.Encoder;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.resp.CommonReplyMsgBody;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.support.handler.scan.BytesEncoderAware;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-24 15:45
 */
@Slf4j(topic = "jt-808.msg.req.handler.abstract-msg-handler")
public abstract class AbstractMsgHandler<T extends RequestMsgBody> implements MsgHandler<T>, BytesEncoderAware {

    // Lazy-init until BytesEncoderAware.setBytesEncoder() method invoked
    protected Encoder encoder;

    @Override
    public void setBytesEncoder(BytesEncoder bytesEncoder) {
        log.info("Binding BytesEncoder [{}] to MsgHandler [{}]", bytesEncoder, this);
        this.encoder = new Encoder(bytesEncoder);
    }

    @Override
    public void handleMsg(RequestMsgMetadata metadata, T body, Jt808Session session) throws IOException, InterruptedException {
        final Optional<RespMsgBody> respInfo = this.doProcess(metadata, body, session);
        if (!respInfo.isPresent()) {
            log.debug("MsgHandler return empty(). [SendResult2Client] canceled.");
            return;
        }

        final RespMsgBody respBody = respInfo.get();
        byte[] respBytes = this.encoder.encodeRespMsg(respBody, session.getCurrentFlowId(), metadata.getHeader().getTerminalId());
        this.send2Client(session, respBytes);

        log.debug("<<<<<<<<<<<<<<< : {}", HexStringUtils.bytes2HexString(respBytes));
    }

    protected void send2Client(Jt808Session session, byte[] bytes) throws InterruptedException {
        session.sendMsgToClient(bytes);
    }

    protected abstract Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, T msg, Jt808Session session);

    protected RespMsgBody commonSuccessReply(RequestMsgMetadata metadata, MsgType replyFor) {
        return CommonReplyMsgBody.success(metadata.getHeader().getFlowId(), replyFor);
    }
}
