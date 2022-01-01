@Data
@Accessors(chain = true)
@BuiltinComponent
@Jt808RequestBody
public class LocationBatchUploadMsgV2019 {
    // byte[0,2)    WORD    数据项个数
    @RequestField(order = 100, startIndex = 0, dataType = WORD)
    private int count;

    // byte[2]    WORD    位置数据类型
    @RequestField(order = 200, startIndex = 2, dataType = BYTE)
    private int type;

    @RequestField(order = 300, startIndex = 3, lengthExpression = "#ctx.msgBodyLength() - 3", dataType = LIST)
    private List<Msg0704Item> itemList;

    @Data
    @Accessors(chain = true)
    public static class Msg0704Item {
        // byte[0,2)    WORD    位置汇报数据体长度
        @RequestField(order = 100, startIndex = 0, dataType = WORD)
        private int msgLength;

        // byte[2,n)    WORD    位置汇报数据体
        @RequestField(order = 200, startIndex = 2, lengthExpression = "msgLength", dataType = OBJECT)
        private LocationUploadMsgV2019 locationInfo;
    }
}
