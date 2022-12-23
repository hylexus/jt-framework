---
icon: token
---

# Session相关

## Jt808Session

`Jt808Session` 的默认实现为 `io.github.hylexus.jt.jt808.spec.session.DefaultJt808Session`。你也可以根据需求来实现自己的 `Jt808Session`。

```java
public class MySession extends DefaultJt808Session {
    private String someField;

    public String getSomeField() {
        return someField;
    }

    public void setSomeField(String someField) {
        this.someField = someField;
    }
}
```

然后在下面的自定义 `Jt808SessionManager` 中替换为自定义的 `Jt808Session`。

## Jt808SessionManager

该组件用来管理每个终端的 `TCP` 连接。

要定制 `Jt808SessionManager` 只需自己声明一个 `Jt808SessionManager` 类型的 `Bean` 即可。

```java
public class MySessionManager implements Jt808SessionManager {

    private static final MySessionManager instance = new MySessionManager();

    public static Jt808SessionManager getInstance() {
        return instance;
    }

    private MySessionManager() {
    }

    protected MySession buildSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        MySession session = new MySession();
        session.channel(channel);
        session.id(generateSessionId(channel));
        session.terminalId(terminalId);
        session.lastCommunicateTimestamp(System.currentTimeMillis());
        session.protocolVersion(version);
        Object key = channel.attr(AttributeKey.valueOf("key")).get();
        session.setSomeField(key == null ? "" : key.toString());
        return session;
    }

    // 这里替换为自定义的Session
    @Override
    public Jt808Session generateSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        return buildSession(terminalId, version, channel);
    }
    // ...
}
```

然后将自定义的 `Jt808SessionManager` 加入到 Spring 容器中就可以替换内置的 `Jt808SessionManager` 了。

```java
// 替换内置的 Jt808SessionManager
@Configuration
public class MyJt808Config {

    // [[ 非必须配置 ]] -- 替换内置的 Jt808SessionManager
    @Bean
    public Jt808SessionManager jt808SessionManager(ObjectProvider<Jt808SessionManagerEventListener> listeners) {
        final Jt808SessionManager sessionManager = MySessionManager.getInstance();
        listeners.stream().sorted(Comparator.comparing(OrderedComponent::getOrder))
                .forEach(sessionManager::addListener);
        return sessionManager;
    }
}
```

## Jt808SessionEventListener

`Jt808SessionEventListener` 会在 `Jt808SessionManager` 中新增、删除 或 关闭 `Jt808Session` 时回调。

```java
public interface Jt808SessionEventListener extends OrderedComponent {

    /**
     * {@link Jt808Session} 新建事件
     * <p>
     * 注意：该方法中不宜做耗时太长/阻塞的操作！！！
     *
     * @param session 新建的 {@link Jt808Session}
     */
    default void onSessionAdd(@Nullable Jt808Session session) {
    }

    /**
     * {@link Jt808Session} 移除事件
     * <p>
     * 注意：该方法中不宜做耗时太长/阻塞的操作！！！
     *
     * @param session 被移除的 {@link Jt808Session}
     */
    default void onSessionRemove(@Nullable Jt808Session session) {
    }

    /**
     * {@link Jt808Session} 关闭事件
     * <p>
     * 注意：该方法中不宜做耗时太长/阻塞的操作！！！
     *
     * @param session     被关闭的 {@link Jt808Session}
     * @param closeReason 关闭原因
     */
    default void onSessionClose(@Nullable Jt808Session session, SessionCloseReason closeReason) {
    }

}
```

想要监听 `Jt808Session` 事件：

- 声明若干个 `Jt808SessionEventListener` 类型的 `Bean` 即可
- 或者可以直接调用 `Jt808SessionManager.addListener(listener)` 方法手动注册监听器

```java
// Session事件监听器
@Configuration
public class MyJt808Config {
    // [[ 非必须配置 ]] -- Session事件监听器 (可以有多个)
    @Bean
    public Jt808SessionEventListener listener1() {
        return new MyJt808SessionEventListener01();
    }

    // [[ 非必须配置 ]] -- Session事件监听器 (可以有多个)
    @Bean
    public Jt808SessionEventListener listener2() {
        return new MyJt808SessionEventListener02();
    }
}
```

## Jt808FlowIdGenerator

`Jt808FlowIdGenerator` 用来生成流水号。要自定义 **流水号的生成策略**，只需声明一个 `Jt808FlowIdGeneratorFactory` 类型的 `Bean` 即可。

```java
public interface Jt808FlowIdGenerator {

    int MAX_FLOW_ID = 0xffff;

    /**
     * @param increment 一次性(连续不间断)递增 {@code increment} 个序列号
     * @return 当前流水号
     */
    int flowId(int increment);

    /**
     * @param count 流水号个数
     * @return 一批连续递增的流水号
     */
    default int[] flowIds(int count) {
        int last = this.flowId(count) - 1;
        final int[] ids = new int[count];
        for (int i = count - 1; i >= 0; i--) {
            ids[i] = last--;
        }
        return ids;
    }

    /**
     * @return 当前流水号(不会自增)
     */
    default int currentFlowId() {
        return flowId(0);
    }

    /**
     * @return 下一个流水号
     * @see #flowId(int)
     */
    default int nextFlowId() {
        return flowId(1);
    }
}
```

替换 `Jt808FlowIdGeneratorFactory` 来控制流水号生成策略：

```java
// 替换流水号生成策略
@Configuration
public class MyJt808Config {
    // [[ 非必须配置 ]] -- 替换流水号生成策略
    @Bean
    public Jt808FlowIdGeneratorFactory jt808FlowIdGeneratorFactory() {
        return new Jt808FlowIdGeneratorFactory.DefaultJt808FlowIdGeneratorFactory();
    }
}
```