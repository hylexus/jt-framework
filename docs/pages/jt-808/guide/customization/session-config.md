# Session相关

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::

## Jt808SessionManager

该组件用来管理每个终端的 `TCP` 连接。

要定制 `Jt808SessionManager` 只需自己声明一个 `Jt808SessionManager` 类型的Bean即可。

```java
public class MySessionManager implements Jt808SessionManager {
    // ......
}
```

```java
@Configuration
public class SomeConfigClass{
    @Bean
    public Jt808SessionManager sessionManager() {
        return new MySessionManager();
    }
}
```

## Jt808SessionManagerEventListener

该组件可以监听 `Jt808Session` 的 `添加`、`移除`、`关闭` 事件。

```java
@Slf4j
public class MyJt808SessionManagerEventListener implements Jt808SessionManagerEventListener {
    @Override
    public void onSessionAdd(@Nullable Jt808Session session) {
        if (session == null) {
            return;
        }
        log.info("[SessionAdd] terminalId = {}, sessionId = {}", session.getTerminalId(), session.getId());
    }
}
```

```java
@Configuration
public class SomeConfigClass{
    // [[非必须配置]] -- 替换内置 Jt808SessionManagerEventListener
    @Bean
    public Jt808SessionManagerEventListener listener() {
        return new MyJt808SessionManagerEventListener();
    }
}
```

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::
