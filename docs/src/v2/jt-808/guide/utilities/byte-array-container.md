---
icon: bitbucket
---

# ByteArrayContainer(v2.1.1)

::: info 提示

`ByteArrayContainer` 是 **2.1.1** 中引入的辅助类。

:::

## 作用

`ByteArrayContainer` 的作用和 [ByteBufContainer](./byte-buf-container.md) 是一样的。

只不过 `ByteArrayContainer` 底层是个 `byte[]` 不需要手动 `release()`。

而 [ByteBufContainer](./byte-buf-container.md) 底层是个 `ByteBuf`, 需要在恰当的时机释放掉。

## 场景示例

此处还是以 `0x8103` 消息中的 参数项的值为例。

```java

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8103)
public class BuiltinMsg8103 {

    @ResponseField(order = 100, dataType = MsgDataType.BYTE)
    private int paramCount;

    @ResponseField(order = 200, dataType = MsgDataType.LIST)
    private List<ParamItem> paramItemList;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class ParamItem {

        @ResponseField(order = 100, dataType = MsgDataType.DWORD)
        private long msgId;

        @ResponseField(order = 200, dataType = MsgDataType.BYTE)
        private int msgLength;

        // 不同 id 的消息内容的数据类型也不同
        // 除了可以使用 byte[], ByteBuf, ByteBufContainer 之外，也可以使用 ByteArrayContainer
        @ResponseField(order = 300, dataType = MsgDataType.BYTES)
        private ByteArrayContainer msgContent;
        // private ByteBufContainer msgContent;
        // private ByteBuf msgContent;
        // private byte[] msgContent;

        public ParamItem(long msgId, ByteArrayContainer msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.msgLength = msgContent.length();
        }
    }
}
```

## 如何创建实例?

推荐使用 `ByteArrayContainer` 提供的工厂方法来创建实例。

```java
class BuiltinMsg8103Test extends BaseReqRespMsgTest {
    @Test
    void test1() {
        final List<BuiltinMsg8103.ParamItem> paramItemList = List.of(
                // 手动构建一个 DWORD 类型的实例
                new BuiltinMsg8103.ParamItem(0x0001, ByteArrayContainer.ofDword(66)),
                // 手动构建一个 STRING 类型的实例
                new BuiltinMsg8103.ParamItem(0x0013, ByteArrayContainer.ofString("www.xxx.fff.zzz.com")),
                // 手动构建一个 WORD 类型的实例
                new BuiltinMsg8103.ParamItem(0x0081, ByteArrayContainer.ofWord(11)),
                // 手动构建一个 BYTE 类型的实例
                new BuiltinMsg8103.ParamItem(0x0084, ByteArrayContainer.ofByte((byte) 1)),
                // 手动构建一个 BYTES 类型的实例
                new BuiltinMsg8103.ParamItem(0x0032, ByteArrayContainer.ofBytes(new byte[]{0x16, 0x32, 0x0A, 0x1E}))
        );
        final BuiltinMsg8103 msg = new BuiltinMsg8103()
                .setParamItemList(paramItemList)
                .setParamCount(paramItemList.size());

        final String hexString = encode(
                msg,
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId(terminalId2013)
                        .msgId(BuiltinJt808MsgType.SERVER_SET_TERMINAL_PARAM)
        );

        assertEquals("7E8103003801391234432300000500000001040000004200000013137777772E7878782E6666662E7A7A7A2"
                + "E636F6D0000008102000B000000840101000000320416320A1EFE7E", hexString);
    }
}
```