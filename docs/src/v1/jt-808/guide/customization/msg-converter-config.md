# 消息转换器

`RequestMsgBodyConverter` 负责将客户端请求中的 `byte[]` 转换为 `请求消息体实体类` 以方便使用。

## 手动实现并注册

::: danger RequestMsgBodyConverter
- 自定义的消息体解析器 `必须` 实现 `RequestMsgBodyConverter` 这个泛型接口
- 当然，这种实现接口并手动注册的方式显得非常繁琐，你完全可以 [参考这里](src/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.md) 使用 `基于注解` 的方式来实现 `RequestMsgBodyConverter` 的功能。
::: 

- 以下为示例性的解析位置消息的 `RequestMsgBodyConverter`

```java
public class LocationUploadMsgBodyConverter2 implements RequestMsgBodyConverter<LocationUploadMsgBody> {

    @Override
    public Optional<LocationUploadMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        LocationUploadMsgBody body = new LocationUploadMsgBody();
        body.setWarningFlag(intFromBytes(bytes, 0, 4));
        body.setStatus(intFromBytes(bytes, 4, 4));
        body.setLat(intFromBytes(bytes, 8, 4) * 1.0 / 100_0000);
        body.setLng(intFromBytes(bytes, 12, 4) * 1.0 / 100_0000);
        body.setHeight((short) intFromBytes(bytes, 16, 2));
        body.setSpeed((short) intFromBytes(bytes, 18, 2));
        body.setDirection((short) intFromBytes(bytes, 20, 2));
        body.setTime(BcdOps.bytes2BcdString(bytes, 22, 6));
        return Optional.of(body);
    }

}
```

- 注册自定义 `RequestMsgBodyConverter`

```java
@Configuration
public class Jt808Config extends Jt808ServerConfigurationSupport { 
    @Override
    public void configureMsgConverterMapping(MsgConverterMapping mapping) {
        super.configureMsgConverterMapping(mapping);
        // 手动将自定义处理器注册到 MsgConverterMapping
        mapping.registerConverter(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationUploadMsgBodyConverter2());
    }
}
```

## 基于注解实现

::: tip 传送门
基于注解来实现 `RequestMsgBodyConverter` 的功能 [请移步这里](src/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.md)
:::

---

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 下找到对应代码。
:::
