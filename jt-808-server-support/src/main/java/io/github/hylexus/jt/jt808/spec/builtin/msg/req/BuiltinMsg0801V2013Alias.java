package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 多媒体数据上传
 */
@Data
@Accessors(chain = true)
@BuiltinComponent
@Jt808RequestBody
public class BuiltinMsg0801V2013Alias {

    // 多媒体ID
    @RequestFieldAlias.Dword(order = 1)
    private int multimediaDataID;

    // 多媒体类型 0：图像；1：音频；2：视频；
    @RequestFieldAlias.Byte(order = 2)
    private int multimediaType;

    // 多媒体格式编码 0：JPEG；1：TIF；2：MP3；3：WAV；4：WMV；
    // 其他保留
    @RequestFieldAlias.Byte(order = 3)
    private int multimediaFormatCode;

    // 事件项编码 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰撞侧翻报警触发；其他保留
    @RequestFieldAlias.Byte(order = 4)
    private int eventItemCode;

    // 通道 ID
    @RequestFieldAlias.Byte(order = 5)
    private int channelId;

    // 位置信息汇报
    //(0x0200)消息体
    @RequestFieldAlias.Object(order = 6, length = 28)
    private BuiltinMsg0200V2013Alias location;

    @RequestFieldAlias.Bytes(order = 7, lengthExpression = "#ctx.msgBodyLength() - ( 4 + 4 + 28)")
    private byte[] mediaData;

}
