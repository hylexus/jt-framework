# ResponseMsgBodyConverter

这里是 `ResponseMsgBodyConverter` 的声明：

```java
public interface ResponseMsgBodyConverter extends OrderedComponent {

    boolean supportsMsgBody(Object msgBody);

    Optional<RespMsgBody> convert(Object msgBody, Session session, RequestMsgMetadata metadata);
}
```

## supportsMsgBody

使用建议参考 [MsgHandler#getSupportedMsgTypes](./msg-handler.md#getsupportedmsgtypes) 即可。
