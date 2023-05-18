---
icon: bitbucket
---

# ByteBufContainer(v2.1.1)

::: info 提示

`ByteBufContainer` 是 **2.1.1** 中引入的辅助类。

:::

## 作用

在某些特殊场景下，用来替代 `byte[]`, `ByteBuf`。

因为在特殊场景下 `byte[]` 和 `ByteBuf` 的内容不太方便构造。

详情看下面 `0x8103` 消息中的用法：

## 场景示例

在 `0x8103` 消息中, 参数项的值的类型不是固定的，可能是 `DWORD`, `WORD`, `BYTE` 等类型。

在 `v2.1.1` 之前的版本中，对于这种情况，实体类中只能使用 `byte[]` 或 `ByteBuf` 来表示，但是 `byte[]` 或 `ByteBuf` 的内容构造起来有点麻烦。

所以提供了 `ByteBufContainer` 和 `ByteArrayContainer` 来简化这种操作。

下面简单演示 `ByteBufContainer` 在响应消息中的用法。

::: info tip

下面示例中的 `@ResponseFieldAlias` 注解是 **v2.1.1** 中给 `@ResponseField` 引入的别名。类似于 **spring** 中 `@RequestMapping` 和 `@GetMapping` 的关系。

详情见 [注解驱动开发--注解别名](../annotation-based-dev/annotation-alias.md) 。

:::

```java

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8103)
public class BuiltinMsg8103Alias {

    @ResponseFieldAlias.Byte(order = 100)
    private int paramCount;

    @ResponseFieldAlias.List(order = 200)
    private List<ParamItem> paramItemList;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class ParamItem {

        @ResponseFieldAlias.Dword(order = 100)
        private long msgId;

        @ResponseFieldAlias.Byte(order = 200)
        private int msgLength;

        // 不同 id 的消息内容的数据类型也不同
        // 除了可以使用 byte[], ByteBuf, ByteArrayContainer 之外，也可以使用 ByteBufContainer
        @ResponseFieldAlias.Bytes(order = 300)
        private ByteBufContainer msgContent;
        // private ByteArrayContainer msgContent;
        // private ByteBuf msgContent;
        // private byte[] msgContent;

        public ParamItem(long msgId, ByteBufContainer msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.msgLength = msgContent.length();
        }
    }
}
```

## 如何创建实例?

推荐使用 `ByteBufContainer` 中提供的一系列工厂方法来构建 `ByteBufContainer` 的实例。

下面是 `ByteBufContainer` 的简单用法示例：

```java
class BuiltinMsg8103Test extends BaseReqRespMsgTest {

    @Test
    void test() {
        final List<BuiltinMsg8103Alias.ParamItem> paramItemList = List.of(
                // 手动构建一个 DWORD 类型的实例
                new BuiltinMsg8103Alias.ParamItem(0x0001, ByteBufContainer.ofDword(66)),
                // 手动构建一个 STRING 类型的实例
                new BuiltinMsg8103Alias.ParamItem(0x0013, ByteBufContainer.ofString("www.xxx.fff.zzz.com")),
                // 手动构建一个 WORD 类型的实例
                new BuiltinMsg8103Alias.ParamItem(0x0081, ByteBufContainer.ofWord(11)),
                // 手动构建一个 BYTE 类型的实例
                new BuiltinMsg8103Alias.ParamItem(0x0084, ByteBufContainer.ofByte((byte) 1)),
                // 手动构建一个 BYTES 类型的实例
                new BuiltinMsg8103Alias.ParamItem(0x0032, ByteBufContainer.ofBytes(new byte[]{0x16, 0x32, 0x0A, 0x1E}))
        );
        final BuiltinMsg8103Alias msg = new BuiltinMsg8103Alias()
                .setParamItemList(paramItemList)
                .setParamCount(paramItemList.size());

        final String hexString = encode(
                msg,
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId(terminalId2013)
                        .msgId(BuiltinJt808MsgType.SERVER_SET_TERMINAL_PARAM)
        );

        // ByteBufContainer 会被 `ByteBufContainerFieldSerializer` 自动释放掉
        // ByteBufContainer 会被 `ByteBufContainerFieldSerializer` 自动释放掉
        // ByteBufContainer 会被 `ByteBufContainerFieldSerializer` 自动释放掉
        paramItemList.forEach(it -> assertEquals(0, it.getMsgContent().value().refCnt()));

        assertEquals("7E8103003801391234432300000500000001040000004200000013137777772E7878782E6666662E7A7A7A2"
                + "E636F6D0000008102000B000000840101000000320416320A1EFE7E", hexString);
    }
}
```

## 注意事项

在使用方面，`ByteBufContainer` 和 `ByteArrayContainer` 的显著区别是：`ByteBufContainer` 需要 `release()`。

::: tip

1. 请求消息实体类中出现的 `ByteBufContainer` 会随着 `Jt808Request#release()` 的一起被自动释放掉(前提是你没有做额外的 `retain()` 操作)
2. 响应消息实体类中出现的 `ByteBufContainer` 会在 `ByteBufContainerFieldSerializer` 中自动释放(前提是你没有做额外的 `retain()` 操作)
3. 除以上两种情况之外，在其他地方手动构建的 `ByteBufContainer` 需要你自己在恰当的时机释放掉

:::
