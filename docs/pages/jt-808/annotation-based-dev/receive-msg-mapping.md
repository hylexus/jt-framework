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

## @SplittableField

## @SlicedFrom

## @ExtraField&@ExtraMsgBody