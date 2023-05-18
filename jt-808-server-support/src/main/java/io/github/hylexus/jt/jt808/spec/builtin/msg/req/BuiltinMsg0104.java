package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * 终端参数查询结果
 *
 * <p>使用参考:
 *
 * <pre> {@code
 *  @Component
 *  @Jt808RequestHandler
 *  public class TerminalParamQueryHandler {
 *
 *    @Jt808RequestHandlerMapping(msgType = 0x0104)
 *    public BuiltinServerCommonReplyMsg terminalParamQueryResult(
 *       Jt808Request request, Jt808RequestEntity<BuiltinMsg0104> requestEntity)
 *         throws JsonProcessingException {
 *      // 思路: 从body中取出数据,然后手动转换.解析思路: https://github.com/hylexus/jt-framework/issues/61
 *
 *     return BuiltinServerCommonReplyMsg.success(
 *             requestEntity.msgType().getMsgId(), requestEntity.flowId());
 *     }
 *  }} </pre>
 *
 * @author puruidong
 * @version 2022/4/12
 * @see <a href="https://github.com/hylexus/jt-framework/issues/61">issues#61</a>
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0104 {
    /**
     * 1. 流水号 WORD
     */
    @RequestField(order = 0, dataType = WORD)
    int serverFlowId;

    /**
     * 2. 参数个数
     */
    @RequestField(order = 1, dataType = BYTE)
    int count;

    /**
     * 3. 参数项列表
     */
    @RequestField(
            order = 2,
            dataType = LIST,
            lengthExpression = "#ctx.msgBodyLength() - 3")
    List<TerminalParam> paramList;

    @Data
    @Accessors(chain = true)
    public static class TerminalParam {

        // 参数id DWORD[4]
        @RequestField(order = 0, dataType = DWORD)
        private int paramId;

        // 参数长度 BYTE[1]
        @RequestField(order = 1, dataType = BYTE)
        private int contentLength;

        // 参数值
        @RequestField(
                order = 2,
                dataType = BYTES,
                lengthExpression = "#this.contentLength")
        private byte[] paramContent;
    }
}
