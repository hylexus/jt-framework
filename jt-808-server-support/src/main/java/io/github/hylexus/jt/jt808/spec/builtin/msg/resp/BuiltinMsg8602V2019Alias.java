package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @see io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType#SERVER_SET_RECTANGLE_AREA
 */
@Jt808ResponseBody(msgId = 0x8602)
@Data
@Accessors(chain = true)
public class BuiltinMsg8602V2019Alias {

    // 0: 更新区域
    // 1: 追加
    // 2: 修改区域
    @ResponseFieldAlias.Byte(order = 0)
    private byte type;

    // 区域总数
    @ResponseFieldAlias.Byte(order = 1)
    private byte totalAreaCount;

    // 区域项列表
    @ResponseFieldAlias.List(order = 2)
    private List<Area> areas;

    @Data
    @Accessors(chain = true)
    public static class Area {
        @ResponseFieldAlias.Dword(order = 1)
        private long areaId;

        @ResponseFieldAlias.Word(order = 2)
        private int areaAttr;

        @ResponseFieldAlias.Dword(order = 3)
        private int leftTopLat;

        @ResponseFieldAlias.Dword(order = 4)
        private int leftTopLng;

        @ResponseFieldAlias.Dword(order = 5)
        private int rightBottomLat;

        @ResponseFieldAlias.Dword(order = 6)
        private int rightBottomLng;

        // areaId的第 0 位为 0 时没有该字段
        @ResponseFieldAlias.BcdDateTime(order = 8, conditionalOn = "T(io.github.hylexus.oaks.utils.Numbers).getBitAt(#this.areaAttr, 0) == 1")
        private LocalDateTime startTime;

        // areaId的第 0 位为 0 时没有该字段
        @ResponseFieldAlias.BcdDateTime(order = 9, conditionalOn = "T(io.github.hylexus.oaks.utils.Numbers).getBitAt(#this.areaAttr, 0) == 1")
        private Date endTime;

        /**
         * areaId的第 1 位为 0 时没有该字段
         *
         * @see #getBitAt(int, int)
         */
        @ResponseFieldAlias.Word(order = 10, conditionalOn = "#this.getBitAt(#this.areaAttr, 1) == 1")
        private int maxSpeed;

        /**
         * areaId的第 1 位为 0 时没有该字段
         *
         * @see #getBitAt(int, int)
         */
        @ResponseFieldAlias.Byte(order = 11, conditionalOn = "#this.getBitAt(#this.areaAttr, 1) == 1")
        private byte continuousTime;

        @ResponseFieldAlias.Word(order = 40, desc = "夜间最高速度", conditionalOn = "#this.getBitAt(#this.areaAttr, 1) == 1")
        private int maxSpeedAtNight;
        @ResponseFieldAlias.Word(order = 50, desc = "区域名称长度")
        private int areaNameLength;
        @ResponseFieldAlias.String(order = 60, desc = "区域名称")
        private String areaName;

        public int getBitAt(int x, int offset) {
            return Numbers.getBitAt(x, offset);
        }
    }
}
