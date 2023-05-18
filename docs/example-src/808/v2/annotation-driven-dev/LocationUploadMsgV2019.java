@Data
@Accessors(chain = true)
@Jt808RequestBody
public class LocationUploadMsgV2019 {
    // (1). byte[0,4)  DWORD 报警标志
    // @RequestField(order = 1, startIndex = 0, dataType = DWORD)
    // 从 2.1.1 开始可以不再指定 `startIndex` 属性
    // 从 2.1.1 开始可以不再指定 `startIndex` 属性
    // 从 2.1.1 开始可以不再指定 `startIndex` 属性
    @RequestField(order = 1, dataType = DWORD)
    // @RequestFieldAlias.Dword(order = 1) // v2.1.1
    private long alarmFlag;

    // (2). byte[4,8) DWORD 状态
    @RequestField(order = 2, dataType = DWORD)
    // @RequestFieldAlias.Dword(order = 2)
    private int status;

    // 将上面的 status 字段的第0位取出转为 int 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private int accIntStatus;
    // 将上面的 status 字段的第0位取出转为 boolean 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private Boolean accBooleanStatus;
    // 0 北纬;1 南纬
    // 将上面的 status 字段的第2位取出转为 int 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 2)
    private int latType;

    // (3). byte[8,12) DWORD 纬度
    @RequestField(order = 3, dataType = DWORD)
    // @RequestFieldAlias.Dword(order = 3)
    private long lat;

    // (4). byte[12,16) DWORD 经度
    @RequestField(order = 4, dataType = DWORD)
    // @RequestFieldAlias.Dword(order = 4)
    private long lng;

    // (5). byte[16,18) WORD 高度
    @RequestField(order = 5, dataType = WORD)
    // @RequestFieldAlias.Word(order = 5)
    private Integer height;

    // (6). byte[18,20) WORD 速度
    @RequestField(order = 6, dataType = WORD)
    // @RequestFieldAlias.Word(order = 6)
    private int speed;

    // (7). byte[20,22) WORD 方向
    @RequestField(order = 7, dataType = WORD)
    // @RequestFieldAlias.Word(order = 6)
    private Integer direction;

    // (8). byte[22,28) BCD[6] 时间
    @RequestField(order = 8, dataType = BCD, length = 6)
    // @RequestFieldAlias.Bcd(order = 8, length = 6)
    private String time;

    // @RequestFieldAlias.BcdDateTime(order = 8)
    // private LocalDateTime time;

    // (9). byte[28,n) 附加项列表
    // @RequestField(order = 9, dataType = LIST, lengthExpression = "#request.msgBodyLength() - 28")
    @RequestField(order = 9, dataType = LIST, lengthExpression = "#ctx.msgBodyLength() - 28")
    // @RequestFieldAlias.List(order = 9, lengthExpression = "#ctx.msgBodyLength() - 28")
    private List<ExtraItem> extraItemList;

    @Data
    public static class ExtraItem {
        // 附加信息ID
        @RequestField(order = 0, dataType = BYTE)
        // @RequestFieldAlias.Byte(order = 0)
        private int id;

        // 附加信息长度
        @RequestField(order = 1, dataType = BYTE)
        // @RequestFieldAlias.Byte(order = 1)
        private int contentLength;

        // 附加信息内容
        @RequestField(order = 3, lengthExpression = "#this.contentLength", dataType = BYTES)
        // @RequestFieldAlias.Bytes(order = 3, lengthExpression = "#this.contentLength")
        // private byte[] content; // 2.0.0 开始支持
        private ByteArrayContainer content; // 2.1.1 开始支持
        // private ByteBufContainer content; // 2.1.1 开始支持
    }
}