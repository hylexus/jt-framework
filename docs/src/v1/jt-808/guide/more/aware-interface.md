# Aware接口

首先声明这种 `Aware` 接口是从 `Spring` 中借鉴(抄袭)的 :joy::joy::joy::joy::joy::joy:。

所以你要是知道 `Spring` 中的诸如 `ApplicationContextAware`、`EnvironmentAware` 等就几乎没必要看本小节了。

## 为什么会提供？

在用注解解析映射请求报文到实体类的时候，字段长度等的计算可能会依赖于消息头中的一些属性。

所以在基于注解的实体类映射时，你可以给实体类实现这些接口以自动注入一些其他信息。

::: danger 注意
目前为止，这些Aware接口只能在 [基于注解处理请求消息映射](src/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.md) 时用于 `请求体消息实体类` 中。
:::

## RequestMsgHeaderAware

每次消息处理时为实体类注入 `RequestMsgHeader` 实例。

```java
public interface RequestMsgHeaderAware {

    void setRequestMsgHeader(RequestMsgHeader header);

}
```

## RequestMsgMetadataAware

每次消息处理时为实体类注入 `RequestMsgMetadata` 实例。

```java
public interface RequestMsgMetadataAware {

    void setRequestMsgMetadata(RequestMsgMetadata metadata);

}
```

## BytesEncoderAware

::: tip 提示
有关 `BytesEncoder` 的内容，[请移步此处](src/v1/jt-808/guide/customization/escape-config.md)。
:::

- `MsgHandler` 中可以实现该接口以注入 `BytesEncoder` 给处理器实例。
- 当然如果是自定义的 `MsgHandler`（接口实现类），完全可以使用 `Spring` 的依赖注入的方式来使用 `BytesEncoder` 实例。
- 内置的 `MsgHandler` 都实现了该接口。

```java
public interface BytesEncoderAware {

    void setBytesEncoder(BytesEncoder bytesEncoder);

}
```
