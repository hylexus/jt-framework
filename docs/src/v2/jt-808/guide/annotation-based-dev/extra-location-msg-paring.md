---
icon: code
---

# 位置附加项列表解析(v2.1.4)

## 2.1.4之前

::: danger 说明

- 在 **2.1.4** **_之前_**，位置上报中的附加项列表只能定义为 `List` 类型，同时 `List` 中的元素的 `content` 类型只能是 `byte[]`、`ByteArrayContainer`、`ByteBufContainer` 类型。
- 全都是 byte[] 类型，使用起来相当繁琐，比如下面的写法：

:::

```java

@Data
@Accessors(chain = true)
@Jt808RequestBody
public class LocationUploadMsgV2019 {

    // 省略其他属性...
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

```

## 2.1.4

::: tip

- **2.1.4** 开始，通过注解别名的扩展提供了 `@RequestFieldAlias.LocationMsgExtraItemMapping` 注解以简化附加项的读取流程
- [BuiltinMsg0200V2019AliasV2](https://github.com/hylexus/jt-framework/blob/master/jt-808-server-support/src/main/java/io/github/hylexus/jt/jt808/spec/builtin/msg/req/BuiltinMsg0200V2019AliasV2.java)
- [BuiltinMsg0200V2013AliasV2](https://github.com/hylexus/jt-framework/blob/master/jt-808-server-support/src/main/java/io/github/hylexus/jt/jt808/spec/builtin/msg/req/BuiltinMsg0200V2013AliasV2.java)

:::

示例如下：

```java

@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0200V2019AliasV2 {
    // 省略其他属性...
    // 省略其他属性...
    // 省略其他属性...

    // (9). byte[28,n) 附加项列表
    // @RequestField(order = 9, startIndex = 28, dataType = LIST, lengthExpression = "#request.msgBodyLength() - 28")
    // @RequestFieldAlias.List(order = 9, lengthExpression = "#ctx.msgBodyLength() - 28")
    // private List<ExtraItem> extraItemList;

    // (9). byte[28,n) 附加项列表
    @RequestFieldAlias.LocationMsgExtraItemMapping(
            order = 9,
            lengthExpression = "#ctx.msgBodyLength() - 28",
            keyValueMappings = {
                    // 基础类型
                    @KeyValueMapping(key = 0x01, value = @ValueDescriptor(source = MsgDataType.DWORD, target = Long.class), desc = "里程，DWORD，1/10km，对应车上里程表读数"),
                    @KeyValueMapping(key = 0x02, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "油量，WORD，1/10L，对应车上油量表读数"),
                    @KeyValueMapping(key = 0x03, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "行驶记录功能获取的速度，WORD，1/10km/h"),
                    @KeyValueMapping(key = 0x04, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "需要人工确认报警事件的 ID，WORD，从 1 开始计数"),
                    @KeyValueMapping(key = 0x11, value = @ValueDescriptor(source = MsgDataType.BYTES, target = byte[].class), desc = "长度1或5；超速报警附加信息见 表 28"),
                    @KeyValueMapping(key = 0x25, value = @ValueDescriptor(source = MsgDataType.DWORD, target = Integer.class), desc = "扩展车辆信号状态位，定义见 表 31"),
                    @KeyValueMapping(key = 0x2A, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "IO状态位，定义见 表 32"),
                    @KeyValueMapping(key = 0x2B, value = @ValueDescriptor(source = MsgDataType.DWORD, target = Long.class), desc = "模拟量，bit0-15，AD0;bit16-31，AD1"),
                    @KeyValueMapping(key = 0x30, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Short.class), desc = "BYTE，无线通信网络信号强度"),
                    @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class), desc = "BYTE，GNSS 定位卫星数"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x64, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg64Alias.class), desc = "苏标: 高级驾驶辅助报警信息，定义见表 4-15"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x65, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg65Alias.class), desc = "苏标: 驾驶员状态监测系统报警信息，定义见表 4-17"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x66, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg66Alias.class), desc = "苏标: 胎压监测系统报警信息，定义见表 4-18"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x67, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg67Alias.class), desc = "苏标: 盲区监测系统报警信息，定义见表 4-20"),
            },
            // keyValueMappings 中没有指定的key, 都会以该属性描述符指定的格式解析
            defaultKeyValueMapping = @ValueDescriptor(source = MsgDataType.BYTES, target = ByteArrayContainer.class)
    )
    private Map<Integer, Object> extraItemMap;

}

```

<p class="">
<img :src="$withBase('/img/v2/annotation-driven-dev/location-msg-extra-item-mapping.png')">
</p>
