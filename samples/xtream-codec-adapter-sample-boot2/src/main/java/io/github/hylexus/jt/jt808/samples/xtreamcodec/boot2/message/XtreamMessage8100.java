package io.github.hylexus.jt.jt808.samples.xtreamcodec.boot2.message;

import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC), msgId = 0x8100, maxPackageSize = 1024)
public class XtreamMessage8100 {
    // 1. byte[0,2) WORD 对应的终端注册消息的流水号
    @Preset.JtStyle.Word(desc = "对应的终端注册消息的流水号")
    private int clientFlowId;

    // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
    @Preset.JtStyle.Byte(desc = "注册结果")
    private short result;

    // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
    @Preset.JtStyle.Str(condition = "result == 0", desc = "鉴权码")
    private String authCode;
}
