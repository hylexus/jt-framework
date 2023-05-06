---
icon: at
---

# 请求消息映射

## @Jt808RequestHandler

`@Jt808RequestHandler` 注解的作用类似于 `WebFlux/WebMvc` 中的 `@Controller`/`@RestController`。

就是标记一下被该注解修饰的类中存在若干个能处理请求的处理器方法。没有其他任何属性。

::: danger

被该注解标记的类应该是被 `Spring` 管理的类。一般来说应该和 `Spring` 的 `@Component` 等注解同时出现。

因为在 **2.x** 中去掉了配置包手动扫描的功能(配置繁琐，还不如直接交给 `Spring` 来处理)。

:::

## @Jt808RequestHandlerMapping

该注解和 `WebFlux/WebMvc` 中 `@RequestMapping` 、`@GetMapping` 等注解功能类似。

表明被该注解修饰的方法具有处理请求的能力。

### 属性

| 属性         | 说明   | 默认值              | 示例                                            |
|------------|------|------------------|-----------------------------------------------|
| `msgType`  | 消息类型 | --               | `{0x0001}`、`{0x0102}`                         |
| `versions` | 协议版本 | `AUTO_DETECTION` | `VERSION_2013` 、`{VERSION_2019,VERSION_2013}` |
| `desc`     | 描述信息 | `""`             | `"Some description ..."`                      |

### 示例

```java{4}
@Component
@Jt808RequestHandler
public class CommonHandler {
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019, desc = "xxx")
    public TerminalRegisterReplyMsg clientRegisterV2019(Jt808RequestEntity<BuiltinMsg0100V2019> request) {
        final BuiltinMsg0100V2019 body = request.body();
        log.info("client register v2019 : {}", body);
        return new TerminalRegisterReplyMsg().setFlowId(request.flowId()).setResult((byte) 0).setAuthCode("AuthCode2019----");
    }
}
```

## @Jt808RequestBody

该注解和 `WebFlux/WebMvc` 中 `@RequestBody` 注解功能类似。 可以将 **请求体** 映射到被该注解修饰的类。

和 `@Jt808RequestHandler` 注解一样，该注解仅仅起一个标记作用，暂时没有其他属性。

## @RequestField

### 属性

| 属性                               | 说明                                                        | 默认值  | 示例                                 |
|----------------------------------|-----------------------------------------------------------|------|------------------------------------|
| `order`                          | 字段解析顺序(不要求连续，只比较大小)                                       | --   | `0`、`1`、`2`                        |
| ~~startIndex~~                   | 起始字节索引(**2.1.1**开始不再需要指定)                                 | `-1` | `0`、`2`、`4`、`8`                    |
| ~~startIndexExpression~~         | 作用和 `startIndex` 相同,但是该属性是基于 SpEL 的(**2.1.1**开始不再需要指定)    | `""` | `"#ctx.msgBodyLength() - 27 + 1"`  |
| ~~startIndexMethod~~             | 作用和 `startIndex` 相同,但是该属性返回的是一个**方法名**(**2.1.1**开始不再需要指定) | ""   | `somMethodName`                    |
| `length`                         | 该字段所占的字节数、长度                                              | `-1` | `2`、`4`                            |
| `lengthExpression`               | 作用和 `length` 相同,但是该属性是基于 `SpEL` 的                         | ""   | `#this.someFieldValue + 1 - 2 * 3` |
| `lengthMethod`                   | 作用和 `length` 相同, 但是该属性返回的是一个 **方法名**                      | `""` | `"SomMethodName"`                  |
| `dataType`                       | 数据类型                                                      | --   | `DWORD` 、`BYTES`                   |
| `customerFieldDeserializerClass` | 自定义的类型转换器                                                 | --   | `XxxDeserializer.class`            |

### SpEL

该注解的 `startIndexExpression` 和 `lengthExpression` 两个属性支持 `SpEL`。可用的 `SpEL`元数据如下：

| 属性        | 说明                                                                             |
|-----------|--------------------------------------------------------------------------------|
| `this`    | 正在迭代的当前对象                                                                      |
| `request` | 本次请求对应的 `Jt808Request` 实例                                                      |
| `header`  | 本次请求对应的 `Jt808Request#header()` 实例                                             |
| `ctx`     | `AnnotationDecoderContext` 实例，代表正在迭代对象的上下文(msgBodyLength 属性代表当前正在迭代的数据大小(字节数)) |
| `context` | 同 `ctx`                                                                        |

::: tip

`SpEL` 绑定的 `RootObject` 就是当前正在迭代的对象。

所以 `#this.someField` 等价于 `someField`、`#this.someMethod()` 等价于 `someMethod()` (`#this` 可以省略)。

:::

### 示例

::: tip

从 **2.1.1** 开始 `startIndex`, `startIndexExpression`, `startIndexMethod` 可以省略。

:::

```java{5,9,13,17,21,25,30}
@Jt808RequestBody
public class BuiltinMsg0100V2019 {
    // 1. [0-2) WORD 省域ID
    // WORD 类型固定长度就是2字节 所以无需指定length
    @RequestField(order = 1, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, dataType = WORD)
    private int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @RequestField(order = 3, dataType = STRING, length = 11)
    private String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @RequestField(order = 4, dataType = STRING, length = 30)
    private String terminalType;

    // 5. [45-75) BYTE[30] 终端ID
    @RequestField(order = 5, dataType = STRING, length = 30)
    private String terminalId;

    // 6. [75]   BYTE    车牌颜色
    @RequestField(order = 6, dataType = BYTE)
    private byte color;

    // 7. [76,n)   String    车辆标识
    // 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
    @RequestField(order = 7, dataType = STRING, lengthExpression = "#ctx.msgBodyLength() - 76")
    private String carIdentifier;
}
```

## @SlicedFrom

::: danger

该注解目前仅仅适用于 `int` 、 `short` 、 `byte` 类型的字段。

**2.1.1** 开始支持 `long` 类型。

:::

### 属性

| `sourceFieldName` | 源字段名           |   |
|-------------------|----------------|---|
| `bitIndex`        | 源字段中的第几个`bit`  | 0 |
| `startBitIndex`   | 源字段中的起始`bit`索引 | 1 |
| `endBitIndex`     | 源字段中的终止`bit`索引 | 2 |

### 示例

::: tip

从 **2.1.1** 开始 `startIndex`, `startIndexExpression`, `startIndexMethod` 可以省略。

:::

```java{9,13,18}
@Jt808RequestBody
public class BuiltinMsg0200V2013 {

    // (2). byte[4,8) DWORD 状态
    @RequestField(order = 2, dataType = DWORD)
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
}
```