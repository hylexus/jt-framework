package io.github.hylexus.jt.builder.jt808;

import io.github.hylexus.jt.utils.ProtocolUtils;
import lombok.Data;

@Data
public class Jt808MsgHeaderSpec {
    // 1. byte[0-1]    消息ID word(16)
    private int msgId;
    // 2. byte[2-3]    消息体属性 word(16)
    //private int msgBodyProps;
    // bit[10-12]    数据加密方式
    //    此三位都为 0，表示消息体不加密
    //    第 10 位为 1，表示消息体经过 RSA 算法加密
    //    其它保留
    private int encryptionType = 0b000;
    // 3. byte[4-9]    终端手机号或设备ID bcd[6]
    private String terminalId;
    // 4. byte[10-11]    消息流水号 word(16)
    private int flowId;

    public byte[] toBytes(int bodyLength) {
        int bodyProps = ProtocolUtils.generateMsgBodyPropsForJt808(bodyLength, encryptionType, false, 0);
        return ProtocolUtils.generateMsgHeaderForJt808RespMsg(msgId, bodyProps, terminalId, flowId);
    }
}