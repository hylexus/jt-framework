package io.github.hylexus.jt.jt808.spec.builtin.msg.extension;

import io.github.hylexus.jt.common.Jt808ByteReader;
import lombok.Data;

@Data
public class BuiltinMsg64 {

    public BuiltinMsg64(byte[] content) {
        this.initFromBytes(content);
    }

    private void initFromBytes(byte[] content) {
        Jt808ByteReader.doWithReader(content, reader -> {
            this.offset0 = reader.readUnsignedDword();
            this.offset4 = reader.readByte();
            this.offset5 = reader.readByte();
            this.offset6 = reader.readByte();
            this.offset7 = reader.readByte();
            this.offset8 = reader.readByte();
            this.offset9 = reader.readByte();
            this.offset10 = reader.readByte();
            this.offset11 = reader.readByte();
            this.offset12 = reader.readByte();
            this.offset13 = reader.readUnsignedWord();
            this.offset15 = reader.readUnsignedDword();
            this.offset19 = reader.readUnsignedDword();
            this.offset23 = reader.readBcd(6);
            this.offset29 = reader.readUnsignedWord();
            this.offset31 = reader.readBytes(16);
        });
    }

    // offset[0,4) 报警 ID: 按照报警先后，从0开始循环累加，不区分报警类型。
    private long offset0;

    // offset[4,5) 标志状态
    // 0x00：不可用
    // 0x01：开始标志
    // 0x02：结束标志
    // 该字段仅适用于有开始和结束标志类型的报警或事件，报警类型或事件类型无开始和结束标志，则该位不可用，填入0x00即可。
    private byte offset4;
    // offset[5,6) 报警/事件类型
    private byte offset5;
    // offset[6,7) 报警级别
    private byte offset6;
    // offset[7,8) 前车车速
    private byte offset7;
    // offset[8,9) 前车/行人距离
    private byte offset8;
    // offset[9,10) 偏离类型
    private byte offset9;

    // offset[10,11) 道路标志识别类型
    private byte offset10;
    // offset[11,12) 道路标志识别数据
    private byte offset11;
    // offset[12,13) 车速
    private byte offset12;
    // offset[13,15) 高程
    private int offset13;

    // offset[15,19) 纬度
    private long offset15;

    // offset[19,23) 经度
    private long offset19;
    // offset[23,29) 日期时间
    private String offset23;
    // offset[29,31] 车辆状态
    private int offset29;
    // offset[31,31+16) 报警标识号
    private byte[] offset31;

    public String getAlarmIdentifiers() {
        return new String(this.offset31);
    }
}
