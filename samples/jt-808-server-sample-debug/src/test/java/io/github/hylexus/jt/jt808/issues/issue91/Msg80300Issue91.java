package io.github.hylexus.jt.jt808.issues.issue91;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.List;

/**
 * 这个例子全是使用自定义编码器实现的
 * <p>
 * 后续会完善编解码逻辑 支持更复杂的数据类型
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8300)
public class Msg80300Issue91 {

    @ResponseField(order = 0, dataType = MsgDataType.BYTE)
    private byte flag;

    @ResponseFieldAlias.Object(order = 2, customerFieldSerializerClass = CustomJt808FieldSerializerIssue91.class)
    private Object data;

    // 总时间+
    // Red+段落数N+[时长+百分比]*N
    // Green+段落数N+[时长+百分比]*N
    // Blue+段落数N+[时长+百分比]*N
    // 总时间:2字节(0表示一直循环)，单位:秒
    // RGB类型:1字节，1为Red，2为Green，3为Blue
    // 段落数:2字节，PWI段落数
    // 时长:2字节，单位:20ms，0为一直保持
    // 百分比:1字节，0-100，PWI控制百分比，0%为灭，100%为全亮
    @Data
    public static class Item0x30 {
        // 1 byte
        private int command = 0x30;

        // 2 + (2 + (1 + 2+ (3 * N))) 字节
        private int length;

        private Item0x31Data data;
    }

    @Data
    // 2 + (1 + 2+ (3 * N)) 字节
    public static class Item0x31Data {
        // 总时间 2字节(0表示一直循环)，单位:秒
        private int totalTime;

        private List<Item0x31DataItem> dataItemList;
    }

    @Data
    // 1 + 2+ (3 * N) 字节
    public static class Item0x31DataItem {
        // 1字节，1为Red，2为Green，3为Blue
        private int rgbType;

        // 段落数:2字节，PWI段落数
        private int pwmCount;

        // N 个 [时长+百分比]
        // 3 * N 字节
        private List<PulseWidthModulation> pwmList;
    }

    // PWM(3 字节)
    @Data
    public static class PulseWidthModulation {
        // 时长:2字节，单位:20ms，0为一直保持
        private Duration time;
        // 百分比:1字节，0-100，PWI控制百分比，0%为灭，100%为全亮
        private int percent;

        public PulseWidthModulation() {
        }

        public PulseWidthModulation(Duration time, int percent) {
            this.time = time;
            this.percent = percent;
        }
    }

    @Data
    // RGB类型 + 总循环次数 + 时间间隔
    public static class Item0x31 {
        // 1 byte
        private int command = 0x31;

        // 2 bytes: 1 + 3 + 2 = 6
        private int length = 6;

        // 1 byte: RGB类型
        private int rgbType;

        // 目前 jt-framework 还不支持映射 3 字节的数字
        // 这里先自定义编码器处理一下
        // 3 bytes: 总循环次数
        private int numberOfCycles;

        // 1 bytes: 时间间隔
        private Duration duration;
    }

}
