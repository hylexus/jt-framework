@Data
@Accessors(chain = true)
@BuiltinComponent
@Jt808RequestBody
public class LocationBatchUploadMsgV2019 {
    // byte[0,2)    WORD    数据项个数
    // 从 2.1.1 开始可以不再指定 `startIndex` 属性
    // 从 2.1.1 开始可以不再指定 `startIndex` 属性
    // 从 2.1.1 开始可以不再指定 `startIndex` 属性
    // @RequestField(order = 100, startIndex = 0, dataType = WORD)
    @RequestField(order = 100, dataType = WORD)
    // @RequestFieldAlias.Word(order = 100) // v2.1.1
    private int count;

    // byte[2]    WORD    位置数据类型
    @RequestField(order = 200, dataType = BYTE)
    // @RequestFieldAlias.Byte(order = 200)
    private int type;

    @RequestField(order = 300, lengthExpression = "#ctx.msgBodyLength() - 3", dataType = LIST)
    // @RequestFieldAlias.List(order = 300, lengthExpression = "#ctx.msgBodyLength() - 3")
    private List<Msg0704Item> itemList;

    @Data
    @Accessors(chain = true)
    public static class Msg0704Item {
        // byte[0,2)    WORD    位置汇报数据体长度
        @RequestField(order = 100, dataType = WORD)
        // @RequestFieldAlias.Word(order = 100)
        private int msgLength;

        // byte[2,n)    WORD    位置汇报数据体
        @RequestField(order = 200, lengthExpression = "msgLength", dataType = OBJECT)
        // @RequestFieldAlias.Object(order = 200, lengthExpression = "msgLength")
        private LocationUploadMsgV2019 locationInfo;
    }
}
