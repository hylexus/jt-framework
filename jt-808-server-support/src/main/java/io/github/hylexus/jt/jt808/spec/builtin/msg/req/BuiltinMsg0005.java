package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.LIST;
import static io.github.hylexus.jt.jt808.support.data.MsgDataType.WORD;

/**
 * 终端补传分包请求
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0005 {

    // byte[0,2)  WORD  原始消息流水号(对应要求重传的原始消息第一包的消息流水号)
    @RequestField(order = 1, dataType = WORD)
    private int firstSubPackageFlowId;

    // byte[2,4)  WORD  重传包总数
    @RequestField(order = 2, dataType = WORD)
    private int totalCount;

    // byte[4, 2*N)  Bytes  包序号
    @RequestField(order = 3, dataType = LIST, lengthExpression = "#ctx.msgBodyLength() - 4")
    private List<PackageId> packageIdList;

    @Data
    @NoArgsConstructor
    public static class PackageId {
        @RequestField(order = 1, dataType = WORD)
        private int value;
    }
}
