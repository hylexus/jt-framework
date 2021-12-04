# AuthValidator

::: danger 注意
- 该组件 `当且仅当` 你使用了内置的 `AuthMsgHandler` 来处理 `鉴权消息` 时才有效。
- 如果你覆盖了/未启用内置的 `AuthMsgHandler`，那么你也 `不用` 提供 `AuthCodeValidator`。因为此时的鉴权逻辑已经完全交由你自定的 `AuthMsgHandler` 来处理了。
- 比较鸡肋的一个内置组件，不过用来 `Quick-Start` 还是很方便的🤣。
:::

```java
@Configuration
public class Jt808Config extends Jt808ServerConfigurationSupport {
    @Override
    public AuthCodeValidator supplyAuthCodeValidator() {
        return (session, requestMsgMetadata, authRequestMsgBody) -> {
            final String terminalId = session.getTerminalId();
            final String authCode = authRequestMsgBody.getAuthCode();
            // 从其他服务验证鉴权码是否正确
            boolean success = clientService.isAuthCodeValid(terminalId, authCode);
            log.info("AuthCode validate for terminal : {} with authCode : {}, result: {}", terminalId, authCode, success);
            return success;
        };
    }
}
```

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 下找到对应代码。
:::
