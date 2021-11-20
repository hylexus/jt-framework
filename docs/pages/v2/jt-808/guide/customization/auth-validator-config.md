# AuthValidator

::: danger 注意

- 该组件 `当且仅当` 你使用了内置的 `BuiltinAuthRequestMsgV2011HandlerForDebugging` 或 `BuiltinAuthRequestMsgV2019HandlerForDebugging` 来处理 `鉴权消息` 时才有效。
- 如果你覆盖了/未启用内置的处理器，那么你也 `不用` 提供 `AuthCodeValidator`。因为此时的鉴权逻辑已经完全交由你自定义的 `MsgHandler` 来处理了。
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

本小节示例可以在 [jt-framework-samples-maven/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework-samples-maven/tree/master/jt-808-server-sample-customized)
找到相关代码。

:::
