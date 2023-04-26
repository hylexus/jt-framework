package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@BuiltinComponent
@Jt808RequestBody
public class BuiltinMsg0704V2013Alias {
    // byte[0,2)    WORD    数据项个数
    @RequestFieldAlias.Word(order = 100)
    private int count;

    // byte[2]    WORD    位置数据类型
    @RequestFieldAlias.Byte(order = 200)
    private int type;

    @RequestFieldAlias.List(order = 300, lengthExpression = "#ctx.msgBodyLength() - 3")
    private List<Msg0704Item> itemList;

    @Data
    @Accessors(chain = true)
    public static class Msg0704Item {
        // byte[0,2)    WORD    位置汇报数据体长度
        @RequestFieldAlias.Word(order = 100)
        private int msgLength;

        // byte[2,n)    WORD    位置汇报数据体
        @RequestFieldAlias.Object(order = 200, lengthExpression = "msgLength")
        private BuiltinMsg0200V2013Alias locationInfo;
    }
}
