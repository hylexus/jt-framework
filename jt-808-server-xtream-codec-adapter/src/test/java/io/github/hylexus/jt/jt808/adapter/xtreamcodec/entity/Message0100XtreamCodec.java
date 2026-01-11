package io.github.hylexus.jt.jt808.adapter.xtreamcodec.entity;

import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.xtream.codec.core.type.Preset;

@Jt808RequestBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC))
public class Message0100XtreamCodec {
    @Preset.JtStyle.Word(desc = "省域 ID")
    public int provinceId;

    @Preset.JtStyle.Word(desc = "市域 ID")
    public int cityId;

    @Preset.JtStyle.Bytes(version = {2011, 2013}, length = 5, desc = "制造商ID(2011 || 2013)")
    @Preset.JtStyle.Bytes(version = 2019, length = 11, desc = "制造商ID(2019)")
    public String manufacturerId;

    @Preset.JtStyle.Bytes(version = 2011, length = 8, desc = "终端型号(2011)")
    @Preset.JtStyle.Bytes(version = 2013, length = 20, desc = "终端型号(2013)")
    @Preset.JtStyle.Bytes(version = 2019, length = 30, desc = "终端型号(2019)")
    public String terminalType;

    @Preset.JtStyle.Bytes(version = {2011, 2013}, length = 7, desc = "终端ID(2011 || 2013)")
    @Preset.JtStyle.Bytes(version = 2019, length = 30, desc = "终端ID(2019)")
    public String terminalId;

    @Preset.JtStyle.Byte(desc = "车牌颜色")
    public short color;

    // 这里不写 length 表示读取后续所有字节
    @Preset.JtStyle.Str(desc = "车辆标识")
    public String carIdentifier;

}
