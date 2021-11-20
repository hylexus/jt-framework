package io.github.hylexus.jt808.samples.mixedversion.entity.resp;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;

public class RegisterRespMsgV2011 implements RespMsgBody {
    /**
     * 应答流水号 WORD 对应的终端注册消息的流水号
     */
    private final int replyFlowId;
    /**
     * 结果 BYTE
     * <p>
     * 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
     */
    private final byte result;
    /**
     * 鉴权码 STRING 只有在成功后才有该字段
     */
    private final String authCode;

    public RegisterRespMsgV2011(int replyFlowId, byte result, String authCode) {
        this.replyFlowId = replyFlowId;
        this.result = result;
        this.authCode = authCode;
    }

    @Override
    public byte[] toBytes() {
        return Bytes.concatAll(
                IntBitOps.intTo2Bytes(replyFlowId),
                new byte[]{result},
                authCode == null ? null : authCode.getBytes(JtProtocolConstant.JT_808_STRING_ENCODING)
        );
    }

    @Override
    public MsgType replyMsgType() {
        return BuiltinJt808MsgType.CLIENT_REGISTER_REPLY;
    }
}
