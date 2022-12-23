# 请求消息映射

## @Jt808ReqMsgBody

该注解只能标记于 `请求消息体实体类` 上。

::: danger 注意
目前为止，`请求消息体实体类` 必须实现标记接口 `RequestMsgBody` 。
:::

类似于 `Hibernate` 和 `MyBatis` 中的 `@Table` 注解。

| 属性      | 解释                | 取值示例 |
| --------- | ------------------- | -------- |
| `msgType` | 808报文类型，消息Id | 0x0200   |

```java
@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0200)
public class LocationUploadMsgBody implements RequestMsgBody {
    // ...
}
```

## @BasicField

只能标记于字段上。类比于`Hibernate` 或 `MyBatis` 中的 `@Column`、`@Basic`。

| 属性      | 解释                | 取值示例 |
| --------- | ------------------- | -------- |
| `startIndex` | 起始字节索引 | 0、4、12   |
| `startIndexMethod` | 作用和startIndex相同，当startIndex无法直接指定时可根据该属性定义的方法名返回字节数 | `"getLength"`   |
| `dataType` | 数据类型 | WORD、DWORD   |
| `length` | 长度，字节数 | 4、6   |
| `byteCountMethod` | 作用和length相同，当length无法直接指定时可根据该属性定义的方法名返回字节数 | `"getLength"`   |
| `customerDataTypeConverterClass` | 自定义的类型转换器 |    |

```java
@BasicField(startIndex = 4, dataType = BYTES, length = 4)
private byte[] statusBytes;

@BasicField(startIndex = 4, dataType = BYTES, byteCountMethod = 'getLength')
private byte[] statusBytes1;

public int getLength() {
    return 4;
}
```
## @SplittableField
将被修饰的字段拆分之后赋值给另一个bean。

::: danger 注意
该注解目前仅仅适用于 `int` 、 `short` 、 `byte` 类型的字段。
:::

| 属性      | 解释                | 取值示例 |
| --------- | ------------------- | -------- |
| `splitPropertyValueIntoNestedBeanField` | 目标属性 |   |

用法请[参考示例](/jt-808/guide/annotation-based-dev/location-msg-parse-demo.md#2.使用@SlicedFrom解析)。

## @SlicedFrom

::: danger 注意
该注解目前仅仅适用于 `int` 、 `short` 、 `byte` 类型的字段。
:::

| 属性      | 解释                | 取值示例 |
| --------- | ------------------- | -------- |
| `sourceFieldName` | 源字段名 |   | aFieldName
| `bitIndex` | 源字段中的第几个bit   | 0
| `startBitIndex` | 源字段中的起始bit索引 |1  |
| `endBitIndex` | 源字段中的终止bit索引 | 2   |

用法请[参考示例](/jt-808/guide/annotation-based-dev/location-msg-parse-demo.md#2.使用@SlicedFrom解析)。

## @ExtraField&@ExtraMsgBody

- @ExtraField

适用于类似位置附加消息的结构。

| 属性      | 解释                | 取值示例 |
| --------- | ------------------- | -------- |
| `startIndex` | 起始字节索引 | 28  |
| `length` | 字节数   | 20
| `byteCountMethod` | 作用和length相同，当length无法直接指定时可根据该属性定义的方法名返回字节数 |aFieldName  |
| `byteCountOfMsgId` | 消息ID占用几个字节 | 1   |
| `byteCountOfContentLength` | 表示消息长度的字段占用几个字节 |  1  |

- @ExtraMsgBody

用于嵌套的附加信息实体类。

| 属性      | 解释                | 取值示例 |
| --------- | ------------------- | -------- |
| `byteCountOfMsgId` | 消息ID占用几个字节 | 1   |
| `byteCountOfContentLength` | 表示消息长度的字段占用几个字节 |  1  |

用法请[参考示例](/jt-808/guide/annotation-based-dev/location-msg-parse-demo.md#解析位置附加项列表)。
