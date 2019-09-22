package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt.annotation.Jt808Field;
import io.github.hylexus.jt.annotation.Jt808MsgBody;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Data
@Accessors(chain = true)
@Jt808MsgBody(msgType = 0x0102)
public class AuthRequestMsgBody implements RequestMsgBody {

    @Jt808Field(startIndex = 0, dataType = MsgDataType.STRING, length = 3)
    private String authCode;

}
