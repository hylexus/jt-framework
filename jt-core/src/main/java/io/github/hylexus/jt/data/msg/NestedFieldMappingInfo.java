package io.github.hylexus.jt.data.msg;

import io.github.hylexus.jt.annotation.msg.req.extra.ExtraMsgBody;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.Data;

/**
 * @author hylexus
 * Created At 2019-10-03 4:21 下午
 */
@Data
public class NestedFieldMappingInfo {
    private int msgId;
    private MsgDataType dataType;
    private boolean isNestedExtraField = false;
    private int byteCountOfMsgId = ExtraMsgBody.DEFAULT_BYTE_COUNT_OF_MSG_ID;
    private int byteCountOfContentLength = ExtraMsgBody.DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH;
    private JavaBeanFieldMetadata fieldMetadata;

    @Override
    public String toString() {
        return "NestedFieldMappingInfo{"
                + "msgId=" + "(" + HexStringUtils.int2HexString(msgId, 2, true) + ")"
                + msgId
                + ", dataType=" + dataType
                + ", isNestedExtraField=" + isNestedExtraField
                + ", byteCountOfMsgId=" + byteCountOfMsgId
                + ", byteCountOfContentLength=" + byteCountOfContentLength
                + '}';
    }
}
