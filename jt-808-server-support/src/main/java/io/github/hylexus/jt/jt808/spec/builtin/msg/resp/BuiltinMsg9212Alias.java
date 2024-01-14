package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x9212)
public class BuiltinMsg9212Alias {

    // BYTE
    @ResponseFieldAlias.Byte(order = 10)
    private short fileNameLength;

    // 文件名称
    @ResponseFieldAlias.String(order = 20)
    private String fileName;

    // 0x00：图片
    // 0x01：音频
    // 0x02：视频
    // 0x03：文本
    // 0x04：其它
    @ResponseFieldAlias.Byte(order = 30)
    private short fileType;

    // 0x00：完成
    // 0x01：需要补传
    @ResponseFieldAlias.Byte(order = 40)
    private byte uploadResult;

    // 补传数据包数量 需要补传的数据包数量，无补传时该值为0
    @ResponseFieldAlias.Byte(order = 50)
    private short packageCountToReTransmit;

    // 补传数据包列表
    @ResponseFieldAlias.List(order = 60)
    private List<RetransmitItem> retransmitItemList;

    @Data
    public static class RetransmitItem {
        @ResponseFieldAlias.Dword(order = 10)
        private long dataOffset;
        @ResponseFieldAlias.Dword(order = 20)
        private long dataLength;
    }
}
