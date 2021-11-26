package io.github.hylexus.jt808.samples.mixedversion.entity.req;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0704)
public class ReqMsg0704 implements RequestMsgBody, RequestMsgHeaderAware {

    private RequestMsgHeader requestMsgHeader;

    // 数据项个数  包含的位置汇报数据项个数，>0
    @BasicField(startIndex = 0, dataType = WORD)
    private int itemCount;

    // 位置数据类型   0:正常位置批量汇报，1:盲区补报
    @BasicField(startIndex = 2, dataType = BYTE)
    private byte locationType;

    @BasicField(startIndex = 3, dataType = BYTES, byteCountMethod = "getLocationInfoByteCount")
    private byte[] locationInfoBytes;

    public void setRequestMsgHeader(RequestMsgHeader requestMsgHeader) {
        this.requestMsgHeader = requestMsgHeader;
    }

    public int getLocationInfoByteCount() {
        return requestMsgHeader.getMsgBodyLength() - 3;
    }

}
