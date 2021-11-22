package io.github.hylexus.jt808.samples.mixedversion.entity.req;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import lombok.Data;

@Data
@Jt808ReqMsgBody(msgType = 0x0701, version = Jt808ProtocolVersion.VERSION_2011)
public class ReqMsg0701 implements RequestMsgBody, RequestMsgHeaderAware {
    private RequestMsgHeader requestMsgHeader;

    @BasicField(startIndex = 0, dataType = MsgDataType.DWORD)
    private int length;

    @BasicField(startIndex = 4, dataType = MsgDataType.STRING, byteCountMethod = "contentLengthMethod")
    private String content;

    public int contentLengthMethod() {
        return requestMsgHeader.getMsgBodyLength() - 4;
    }
}
