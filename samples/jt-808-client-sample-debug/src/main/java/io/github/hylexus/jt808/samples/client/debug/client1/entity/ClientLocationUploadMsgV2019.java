package io.github.hylexus.jt808.samples.client.debug.client1.entity;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x0200)
@BuiltinComponent
public class ClientLocationUploadMsgV2019 {
    // (1). byte[0,4)  DWORD 报警标志
    @ResponseField(order = 1, dataType = DWORD)
    private long alarmFlag;

    // (2). byte[4,8) DWORD 状态
    @ResponseField(order = 2, dataType = DWORD)
    private int status;

    // (3). byte[8,12) DWORD 纬度
    @ResponseField(order = 3, dataType = DWORD)
    private long lat;

    // (4). byte[12,16) DWORD 经度
    @ResponseField(order = 4, dataType = DWORD)
    private long lng;

    // (5). byte[16,18) WORD 高度
    @ResponseField(order = 5, dataType = WORD)
    private Integer height;

    // (6). byte[18,20) WORD 速度
    @ResponseField(order = 6, dataType = WORD)
    private int speed;

    // (7). byte[20,22) WORD 方向
    @ResponseField(order = 7, dataType = WORD)
    private Integer direction;

    // (8). byte[22,28) BCD[6] 时间
    @ResponseField(order = 8, dataType = BCD)
    private String time;

    // (9). byte[28,n) 附加项列表
    @ResponseField(order = 9, dataType = LIST)
    private List<ExtraItem> extraItemList;

    @Data
    @Accessors(chain = true)
    public static class ExtraItem {
        // 附加信息ID
        @ResponseField(order = 0, dataType = BYTE)
        private int id;

        // 附加信息长度
        @ResponseField(order = 1, dataType = BYTE)
        private int contentLength;

        // 附加信息内容
        @ResponseField(order = 3, dataType = BYTES)
        private byte[] content;
    }
}
