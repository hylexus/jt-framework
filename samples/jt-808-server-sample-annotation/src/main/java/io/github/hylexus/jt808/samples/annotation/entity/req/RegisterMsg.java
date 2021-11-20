package io.github.hylexus.jt808.samples.annotation.entity.req;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * Created At 2020-06-12 17:24
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0100)
public class RegisterMsg implements RequestMsgBody, RequestMsgHeaderAware {
    private RequestMsgHeader requestMsgHeader;

    @BasicField(startIndex = 0, dataType = WORD)
    private int provinceId;

    @BasicField(startIndex = 2, dataType = WORD)
    private int cityId;

    @BasicField(startIndex = 4, dataType = BYTES, length = 5)
    private byte[] manufacturerId;

    @BasicField(startIndex = 9, dataType = BYTES, length = 20)
    private byte[] terminalType;

    @BasicField(startIndex = 29, dataType = BYTES, length = 7)
    private byte[] terminalId;

    @BasicField(startIndex = 36, dataType = BYTE)
    private byte color;

    @BasicField(startIndex = 37, dataType = STRING, byteCountMethod = "getCarIdentifierLength")
    private String carIdentifier;

    public void setRequestMsgHeader(RequestMsgHeader requestMsgHeader) {
        this.requestMsgHeader = requestMsgHeader;
    }

    public int getCarIdentifierLength() {
        return requestMsgHeader.getMsgBodyLength() - 37;
    }
}
