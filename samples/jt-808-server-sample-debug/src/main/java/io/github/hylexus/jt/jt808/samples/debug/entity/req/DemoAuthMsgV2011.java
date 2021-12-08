package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.data.Jt808HeaderSpecAware;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;

@Data
@Jt808RequestMsgBody
public class DemoAuthMsgV2011 implements Jt808HeaderSpecAware {
    private Jt808MsgHeader header;

    @RequestField(order = 0, startIndex = 0, dataType = MsgDataType.STRING, lengthMethod = "getAuthCodeByteCount")
    private String authCode;

    public int getAuthCodeByteCount() {
        return header.msgBodyLength();
    }

    @Override
    public void setHeader(Jt808MsgHeader header) {
        this.header = header;
    }
}
