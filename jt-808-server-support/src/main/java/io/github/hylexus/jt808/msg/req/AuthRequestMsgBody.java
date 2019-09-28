package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt.annotation.msg.BasicField;
import io.github.hylexus.jt.annotation.msg.Jt808MsgBody;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Data
@Accessors(chain = true)
@Jt808MsgBody(msgType = 0x0102)
public class AuthRequestMsgBody implements RequestMsgBody, RequestMsgHeaderAware {

    private RequestMsgHeader header;

    /**
     * 鉴权码
     * 长度：
     * <pre>
     * --> 1. {@link MsgDataType#getByteCount()} ==0
     * --> 2. {@link BasicField#length()} == 0
     * --> 3. {@link BasicField#byteCountMethod()}
     * </pre>
     */
    @BasicField(startIndex = 0, dataType = MsgDataType.STRING, byteCountMethod = "getAuthCodeByteCount")
    private String authCode;

    public int getAuthCodeByteCount() {
        return header.getMsgBodyLength();
    }

    @Override
    public void setRequestMsgHeader(RequestMsgHeader header) {
        this.header = header;
    }
}
