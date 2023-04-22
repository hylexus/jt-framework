package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @see io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType#SERVER_SET_POLYGON_AREA
 */
@Jt808ResponseBody(msgId = 0x8604)
@Data
@Accessors(chain = true)
public class BuiltinMsg8604V2019Alias {

    @ResponseFieldAlias.Dword(order = 1)
    private long areaId;

    @ResponseFieldAlias.Word(order = 2)
    private int areaAttr;

    // areaId的第 0 位为 0 时没有该字段
    @ResponseFieldAlias.BcdDateTime(order = 8, conditionalOn = "T(io.github.hylexus.oaks.utils.Numbers).getBitAt(#this.areaAttr, 0) == 1")
    private LocalDateTime startTime;

    // areaId的第 0 位为 0 时没有该字段
    @ResponseFieldAlias.BcdDateTime(order = 9, conditionalOn = "T(io.github.hylexus.oaks.utils.Numbers).getBitAt(#this.areaAttr, 0) == 1")
    private Date endTime;

    // areaId的第 1 位为 0 时没有该字段
    @ResponseFieldAlias.Word(order = 10, conditionalOn = "T(io.github.hylexus.oaks.utils.Numbers).getBitAt(#this.areaAttr, 1) == 1")
    private int maxSpeed;

    @ResponseFieldAlias.Byte(order = 11, conditionalOn = "T(io.github.hylexus.oaks.utils.Numbers).getBitAt(#this.areaAttr, 1) == 1")
    private byte continuousTime;

    @ResponseFieldAlias.Word(order = 20)
    private int verticesCount;

    @ResponseFieldAlias.List(order = 30)
    private List<Vertices> vertices;

    @ResponseFieldAlias.Word(order = 40, desc = "夜间最高速度", conditionalOn = "#this.getBitAt(#this.areaAttr, 1) == 1")
    private int maxSpeedAtNight;
    @ResponseFieldAlias.Word(order = 50, desc = "区域名称长度")
    private int areaNameLength;
    @ResponseFieldAlias.String(order = 60, desc = "区域名称")
    private String areaName;

    public int getBitAt(int x, int offset) {
        return Numbers.getBitAt(x, offset);
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class Vertices {
        @ResponseFieldAlias.Dword(order = 1)
        private long lat;
        @ResponseFieldAlias.Dword(order = 2)
        private long lng;
    }
}
