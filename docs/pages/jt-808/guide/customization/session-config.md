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
public class CustomizedDemoJt808Config extends Jt808ServerConfigurationSupport {
    
    @Override
    public Jt808SessionManager supplyJt808SessionManager() {
        return MySessionManager.getInstance();
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
public class CustomizedDemoJt808Config extends Jt808ServerConfigurationSupport {
    
    @Override
    public Jt808SessionManagerEventListener supplyJt808SessionManagerEventListener() {
        return new MyJt808SessionManagerEventListener();
    }
}
```

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::
