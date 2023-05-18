package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeaderAware;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;

@Data
@Jt808RequestBody
public class DemoAuthMsgV2013 implements Jt808RequestHeaderAware {
    private Jt808RequestHeader header;

    @RequestField(order = 0, dataType = MsgDataType.STRING, lengthMethod = "getAuthCodeByteCount")
    private String authCode;

    public int getAuthCodeByteCount() {
        return header.msgBodyLength();
    }

    @Override
    public void setHeader(Jt808RequestHeader header) {
        this.header = header;
    }
}
