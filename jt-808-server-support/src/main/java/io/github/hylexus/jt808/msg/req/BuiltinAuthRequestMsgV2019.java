package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0102, version = Jt808ProtocolVersion.VERSION_2019)
@BuiltinComponent
public class BuiltinAuthRequestMsgV2019 implements RequestMsgBody, RequestMsgHeaderAware {

    private RequestMsgHeader header;

    // 鉴权码长度
    @BasicField(startIndex = 0, dataType = MsgDataType.BYTE)
    private byte authCodeLength;

    // 鉴权码内容
    @BasicField(startIndex = 1, dataType = MsgDataType.STRING, byteCountMethod = "getAuthCodeLength")
    private String authCode;

    // IMEI
    @BasicField(startIndexMethod = "getImeiStartIndex", dataType = MsgDataType.BYTES, length = 15)
    private byte[] imei;

    // 软件版本号
    @BasicField(startIndexMethod = "getSoftwareVersionStartIndex", dataType = MsgDataType.BYTES, length = 20)
    private byte[] softwareVersion;

    public int getImeiStartIndex() {
        return authCodeLength + 1;
    }

    public int getSoftwareVersionStartIndex() {
        return authCodeLength + 16;
    }

    @Override
    public void setRequestMsgHeader(RequestMsgHeader header) {
        this.header = header;
    }
}
