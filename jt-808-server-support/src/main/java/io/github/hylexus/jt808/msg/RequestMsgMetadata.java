package io.github.hylexus.jt808.msg;

import io.github.hylexus.jt.data.msg.MsgType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-09-19 11:35 下午
 */
@Data
@Accessors(chain = true)
public class RequestMsgMetadata {
    protected RequestMsgHeader header;
    protected byte[] bodyBytes;
    protected byte checkSum;
    protected MsgType msgType;

    @Override
    public String toString() {
        return "RequestMsgMetadata{"
                + "msgType=" + msgType
                + ", header=" + header
                //+ ", bodyBytes=" + Arrays.toString(bodyBytes)
                + ", checkSum=" + checkSum
                + '}';
    }
}
