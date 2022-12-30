---
icon: change
---

# 编码解码相关

## Jt808MsgBytesProcessor

`Jt808MsgBytesProcessor` 负责 **转义** 请求消息/响应消息、**计算校验码**。

要想替换该组件，只需要声明一个 `Jt808MsgBytesProcessor` 类型的 `Bean` 即可。

```java
public interface Jt808MsgBytesProcessor {

    /**
     * 转义请求消息
     *
     * @param byteBuf 请求消息, 不包含分隔符 {@link io.github.hylexus.jt.jt808.JtProtocolConstant#PACKAGE_DELIMITER 0X7E}
     * @return 转义之后的消息
     */
    ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    /**
     * 转义响应消息
     *
     * @param byteBuf 响应给客户端的消息, 不包含分隔符 {@link io.github.hylexus.jt.jt808.JtProtocolConstant#PACKAGE_DELIMITER 0X7E}
     * @return 转义之后的消息
     */
    ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    /**
     * 就是校验码
     *
     * @param byteBuf 请求消息/响应消息
     * @return 检验码
     */
    byte calculateCheckSum(ByteBuf byteBuf);
}
```

示例：

```java
// 替换内置的转义等逻辑
@Configuration
public class MyJt808Config {
    // [[ 非必须配置 ]] -- 替换内置的转义等逻辑
    @Bean
    public Jt808MsgBytesProcessor jt808MsgBytesProcessor() {
        return new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT);
    }
}
```

## Jt808MsgDecoder

`Jt808MsgDecoder` 负责将请求中的 **字节流** 初步解析为 `Jt808Request` 对象。

如果内置的实现不符合要求 或者 内置实现返回的 `Jt808Request` 不符合要求，你可以自定义一个解码器。

```java
public interface Jt808MsgDecoder {

    /**
     * 解析请求，返回 {@link Jt808Request} 实例。
     * <p>
     * 如果默认的 {@link io.github.hylexus.jt.jt808.spec.impl.request.DefaultJt808Request DefaultJt808Request} 不满足需求 或
     * {@link  io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder DefaultJt808MsgDecoder} 不符合要求，
     * 可以提供自己的实现类在这里返回自己的 {@link Jt808Request} 实现
     *
     * @param byteBuf 请求消息(不包含分隔符 {@link io.github.hylexus.jt.jt808.JtProtocolConstant#PACKAGE_DELIMITER 0X7E})
     * @return 解析之后的 {@link Jt808Request} 实例.
     * @see Jt808MsgBytesProcessor#doEscapeForReceive(ByteBuf)
     * @see Jt808MsgBytesProcessor#calculateCheckSum(ByteBuf)
     */
    Jt808Request decode(ByteBuf byteBuf);

}
```

示例:

```java
// 替换内置的 Jt808MsgDecoder
@Configuration
public class MyJt808Config {

    // [[ 非必须配置 ]] -- 替换内置的 Jt808MsgDecoder
    @Bean
    public Jt808MsgDecoder jt808MsgDecoder(
            Jt808MsgTypeParser jt808MsgTypeParser,
            Jt808MsgBytesProcessor bytesProcessor,
            Jt808ProtocolVersionDetectorRegistry registry) {
        return new DefaultJt808MsgDecoder(jt808MsgTypeParser, bytesProcessor, registry);
    }
}
```

## Jt808MsgEncoder

`Jt808MsgEncoder` 负责将 `Jt808Response` 对象编码为 **符合808标准** 的字节流。

```java
public interface Jt808MsgEncoder {

    /**
     * 将 {@link Jt808Response} 编码为 {@code 符合808标准} 的字节流
     *
     * @param response 响应消息
     * @return 编码后的字节流
     * @see Jt808MsgBytesProcessor#doEscapeForSend(ByteBuf)
     * @see Jt808MsgBytesProcessor#calculateCheckSum(ByteBuf)
     */
    ByteBuf encode(Jt808Response response);

}
```

如果内置的实现不符合要求，可以声明一个 `Jt808MsgEncoder` 类型的 Bean，替换内置实现：

```java
// 替换内置的 Jt808MsgEncoder
@Configuration
public class MyJt808Config {

    // [[ 非必须配置 ]] -- 替换内置的 Jt808MsgEncoder
    @Bean
    public Jt808MsgEncoder jt808MsgEncoder(Jt808MsgBytesProcessor processor) {
        return new DefaultJt808MsgEncoder(processor, ByteBufAllocator.DEFAULT);
    }
}
```