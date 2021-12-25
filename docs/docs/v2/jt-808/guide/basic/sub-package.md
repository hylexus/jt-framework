# 消息分包

## 请求消息

## 响应消息

### 说明

默认的 `Jt808MsgEncoder` 的实现类 `DefaultJt808MsgEncoder` 会将较大的消息自动分包，分包的阈值可以手动指定。 默认值是 `1024` 字节。

也就是说：`DefaultJt808MsgEncoder` 在编码消息时(**转义之前**)，发现消息包的大小超过了**阈值**，会自动将消息分包发送给客户端。

内置的支持自动分配的返回类型有两种: `Jt808Response` 和被 `@Jt808ResponseBody` 标记的类型。

::: tip

可以将 `jt-808.response.encoder` 日志的级别调整为 `debug` 级别，这样就可以看到分包消息的详情了：

```yaml
logging:
  # 编码器日志
  level.jt-808.response.encoder: debug
```

:::

### Jt808Response

`Jt808Response` 有个属性 `maxPackageSize` 用来控制单个消息包的最大大小。默认 **1024** 字节。

如果响应消息的大小(**转义之前**)超过 `maxPackageSize()` 的阈值，会自动将消息拆分为多个子包发送。

```java
public interface Jt808Response extends Jt808ByteWriter {

    int DEFAULT_MAX_PACKAGE_SIZE = 1024;

    /**
     * 响应消息大小超过该值(默认 {@value #DEFAULT_MAX_PACKAGE_SIZE})会自动分包发送(转义之前)
     *
     * @return 响应消息最大字节数
     */
    default int maxPackageSize() {
        return DEFAULT_MAX_PACKAGE_SIZE;
    }

    /**
     * 指定单个消息包的最大大小(转义之前)
     *
     * @param size 消息包最大大小
     * @return 单个消息包的最大大小
     */
    Jt808Response maxPackageSize(int size);

    // ...
}
```

### @Jt808ResponseBody

`@Jt808ResponseBody` 注解也有个 `maxPackageSize` 属性用来控制单个消息包的最大大小(**转义之前**)。

```java
public @interface Jt808ResponseBody {

    /**
     * @return 单个消息包的最大字节数, 超过该值会自动分包发送
     * @see Jt808Response#DEFAULT_MAX_PACKAGE_SIZE
     * @see Jt808Response#maxPackageSize(int)
     */
    int maxPackageSize() default Jt808Response.DEFAULT_MAX_PACKAGE_SIZE;

    // ...
}
```

### 示例

<CodeGroup>

  <CodeGroupItem title="示例1" active>

@[code](@example-src/808/v2/basic/sub-package/resp/CommonHandler.java)

  </CodeGroupItem>

  <CodeGroupItem title="示例2" >

@[code](@example-src/808/v2/basic/sub-package/resp/TerminalRegisterMsgHandlerV2019.java)

  </CodeGroupItem>

</CodeGroup>



发送下面这条测试报文

```shell
7E010040560100000000013912344329007B000B0002696439383736353433323174797065303031323334353637383132333435363738383736353433323149443030303031323334353637383132333435363738383736353433323101B8CA4A2D313233343539257E
```

然后观察 `debug` 日志的输出:

<p class="">
    <img :src="$withBase('/img/v2/basic/Jt808ResponseBody-sub-package.png.png')">
</p> 

上面日志解释如下：

```sh
# 0x8100 的第1个子包(-: 转义之前; +: 转义之后)
- <<<<<<<<<<<<<<< (0x8100--33) 1/3: 7E810060090100000000013912344329000000030001007B0041757468436FE07E
+ <<<<<<<<<<<<<<< (0x8100--33) 1/3: 7E810060090100000000013912344329000000030001007B0041757468436FE07E
# 0x8100 的第2个子包(-: 转义之前; +: 转义之后)
- <<<<<<<<<<<<<<< (0x8100--33) 2/3: 7E8100600901000000000139123443290001000300026465323031392D2D2DBB7E
+ <<<<<<<<<<<<<<< (0x8100--33) 2/3: 7E8100600901000000000139123443290001000300026465323031392D2D2DBB7E
# 0x8100 的第3个子包(-: 转义之前; +: 转义之后)
- <<<<<<<<<<<<<<< (0x8100--25) 3/3: 7E8100600101000000000139123443290002000300032DBA7E
+ <<<<<<<<<<<<<<< (0x8100--25) 3/3: 7E8100600101000000000139123443290002000300032DBA7E
```

