package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x9102)
public class BuiltinMsg9102Alias {

    // 逻辑通道号
    @ResponseFieldAlias.Byte(order = 10)
    private byte channelNumber;

    // 控制指令
    // 0: 关闭音视频传输指令
    // 1: 切换码流(增加暂停和继续)
    // 2: 暂停该通过所有流的发送
    // 3: 恢复暂停前流的发送，与暂停前的流类型一致
    // 4: 关闭双向对讲
    @ResponseFieldAlias.Byte(order = 20)
    private byte command;

    // 关闭音视频类型
    // 0: 关闭该通过有关的音视频数据
    // 1: 只关闭该通过有关的音频，保留该通道有关的视频
    // 2: 只关闭该通过有关的视频，保留该通道有关的音频
    @ResponseFieldAlias.Byte(order = 30)
    private byte mediaTypeToClose;

    // 切换码流类型
    // 将之前申请的码流切换为新申请的码流，音频与切换之前保持一致
    // 新申请的码流:
    // 0: 主码流
    // 1: 子码流
    @ResponseFieldAlias.Byte(order = 40)
    private byte streamType;
}
