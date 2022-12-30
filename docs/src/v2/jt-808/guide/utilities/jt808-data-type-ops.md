---
icon: change
---

# 808数据类型处理器

## Jt808ByteReader

用来从 `ByteBuf` 中读取指定类型的 `JT/T-808` 数据类型。

::: tip 内置的支持 Jt808ByteReader 的类

`Jt808Request.bodyAsReader()` 方法可以将消息体转换为 `Jt808ByteReader` 来手动读取数据。

:::

### 简单示例

```java
class Jt808ByteReaderTest {

    @Test
    void read() {
        final ByteBuf originalByteBuf = ByteBufAllocator.DEFAULT.buffer();

        try {
            // 下面两行代码给原始ByteBuf中加入一些测试数据
            JtProtocolUtils.writeBcd(originalByteBuf, "10203040");
            JtProtocolUtils.writeDword(originalByteBuf, 123);

            // 下面依次从 originalByteBuf 中读取 BCD、UnsignedDword
            final ByteBuf afterRead = Jt808ByteReader.of(originalByteBuf)
                    .readBcd(4, bcdString -> assertEquals("10203040", bcdString))
                    .readUnsignedDword(dword -> assertEquals(123, dword))
                    .readable();

            // 读取完成之后，Jt808ByteReader 不会改变内部 originalByteBuf 的引用指向
            assertSame(originalByteBuf, afterRead);

        } finally {
            // Jt808ByteReader 只是个辅助类，不会改变内部 readable() 指向的 ByteBuf 的 refCnt
            // 应该在恰当的时机释放被包装的 originalByteBuf
            originalByteBuf.release();
            assertEquals(0, originalByteBuf.refCnt());
        }

    }

}
```

### 使用Jt808ByteReader来解析请求体

除了使用 `@Jt808RequestBody` 标记的实体类来解析请求，也可以通过 `Jt808ByteReader` 来手动读取数据：

```java
public class TerminalRegisterMsgHandlerV2013 implements SimpleJt808RequestHandler<Jt808Response> {

    // ...

    // 除了使用注解，也可以通过 `Jt808ByteReader` 来手动读取数据
    public Jt808Response handleMsg(Jt808ServerExchange exchange) {
        exchange.request()
                .bodyAsReader()
                // 1. [0-2) WORD 省域ID
                .readUnsignedWord(provinceId -> log.info("省域ID:{}", provinceId))
                // 2. [2-4) WORD 省域ID
                .readUnsignedWord(cityId -> log.info("省域ID:{}", cityId))
                // 3. [4-9) BYTE[5] 制造商ID
                .readString(5, manufacturerId -> log.info("制造商ID:{}", manufacturerId))
                // 4. [9-29) BYTE[20] 终端型号
                .readString(20, terminalType -> log.info("终端型号:{}", terminalType))
                // 5. [29-36) BYTE[7] 终端ID
                .readString(7, terminalId -> log.info("终端ID:{}", terminalId))
                // 6. [36]   BYTE    车牌颜色
                .readByte(color -> log.info("车牌颜色:{}", color))
                // 7. [37,n)   String    车辆标识
                .readString(exchange.request().msgBodyLength() - 37, carIdentifier -> log.info("车辆标识:{}", carIdentifier));
    }
}
```

## Jt808ByteWriter

用来像 `ByteBuf` 中写入指定类型的 `JT/T-808` 数据类型。

::: tip 内置的支持 Jt808ByteWriter 的类

- `Jt808Response.Jt808ResponseBuilder.body(Consumer<Jt808ByteWriter>)`
- `ByteBufJt808MsgBuilder.body(Consumer<Jt808ByteWriter>)`

:::

### 简单示例

```java
class Jt808ByteWriterTest {

    @Test
    void write() {
        final ByteBuf originalByteBuf = ByteBufAllocator.DEFAULT.buffer(128);
        try {
            final Jt808ByteWriter writer = Jt808ByteWriter.of(originalByteBuf);
            final ByteBuf afterWrite = writer
                    .writeBcd("10203040")
                    .writeString("STRING")
                    .writeDWord(100)
                    // 这里返回的 ByteBuf 应该和初始化传入的是同一个对象
                    .writable();

            assertSame(originalByteBuf, afterWrite);

            assertEquals("10203040535452494E4700000064", HexStringUtils.byteBufToString(originalByteBuf));
            assertEquals("10203040535452494E4700000064", HexStringUtils.byteBufToString(afterWrite));

        } finally {
            // Jt808ByteWriter 只是个辅助类，不会改变内部 writable() 指向的 ByteBuf 的 refCnt
            // 应该在恰当的时机释放被包装的 originalByteBuf
            originalByteBuf.release();
            assertEquals(0, originalByteBuf.refCnt());
        }
    }
}
```

### 使用Jt808ByteWriter手动编码响应体

除了使用 `@Jt808ResponseBody` 标记的实体类来编码请求，也可以通过 `Jt808ByteWriter` 来手动写入数据：

```java
public class TerminalRegisterMsgHandlerV2013 implements SimpleJt808RequestHandler<Jt808Response> {

    @Override
    public Jt808Response handleMsg(Jt808ServerExchange exchange) {
        // ...
        return Jt808Response.newBuilder()
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                .terminalId(exchange.request().terminalId())
                .flowId(exchange.session().nextFlowId())
                .version(exchange.request().version())
                // 除了使用注解，也可以通过 `Jt808ByteWriter` 来手动写入数据
                .body(writer -> writer
                        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
                        .writeWord(exchange.request().flowId())
                        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
                        .writeByte(0)
                        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
                        .writeString("AuthCode-123")
                )
                .build();
    }
}

```
