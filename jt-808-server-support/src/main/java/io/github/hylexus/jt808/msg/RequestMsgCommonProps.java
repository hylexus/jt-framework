package io.github.hylexus.jt808.msg;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-09-19 11:35 下午
 */
@Data
@ToString(exclude = "bodyBytes")
@Accessors(chain = true)
public class RequestMsgCommonProps {
    protected RequestMsgHeader header;
    protected byte[] bodyBytes;
    protected byte checkSum;
    protected MsgType msgType;
}
