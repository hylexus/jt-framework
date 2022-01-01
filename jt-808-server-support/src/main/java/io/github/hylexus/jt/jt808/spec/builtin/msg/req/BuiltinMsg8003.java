package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.LIST;
import static io.github.hylexus.jt.jt808.support.data.MsgDataType.WORD;

/**
 * 服务器端补传分包请求
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8003)
public class BuiltinMsg8003 {

    // byte[0,2)  WORD  原始消息流水号(对应要求重传的原始消息第一包的消息流水号)
    @ResponseField(order = 1, dataType = WORD)
    private int firstSubPackageFlowId;

    // byte[2,4)  WORD  重传包总数
    @ResponseField(order = 2, dataType = WORD)
    private int totalCount;

    // byte[4, 2*N)  Bytes  包序号
    @ResponseField(order = 3, dataType = LIST)
    private List<PackageId> packageIdList;

    @Data
    @NoArgsConstructor
    public static class PackageId {
        @ResponseField(order = 1, dataType = WORD)
        private int value;

        public PackageId(int value) {
            this.value = value;
        }
    }
}
