# MsgHandler

这里是 `MsgHandler` 的声明：

```java
public interface MsgHandler<T extends RequestMsgBody> extends OrderedComponent {

    default Set<MsgType> getSupportedMsgTypes() {
        return Collections.emptySet();
    }

    void handleMsg(RequestMsgMetadata metadata, T body, Session session) throws IOException, InterruptedException;
}
```

## getSupportedMsgTypes

::: tip 注意
∵ 方法 `getSupportedMsgTypes()` 可能会被调用多次。

∴ `不推荐你在方法里直接new一个结果返回`，即便是内部 `MsgHandlerMapping` 的实现使用了 `基于Map的本地缓存`。

:arrow_right: 实现类应该确保每次调用 `都返回同一个` `Set<MsgType>` 实例:

- 以免重复创建不必要的对象（即便这样对程序的结果可能没有影响）。
- 你可以使用 `java.util.Collections.singleton(T)` 等类似功能的方法返回一个单例对象
- 你可以直接返回一个成员对象，而不是直接去在方法里 `new` 一个实例。
:::

- 示例实现

```java
@Slf4j
@BuiltinComponent
public class AuthMsgHandler extends AbstractMsgHandler<AuthRequestMsgBody> {
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        // 每次都返回同一个实例
        return Collections.singleton(BuiltinJt808MsgType.CLIENT_AUTH);
    }
}

@Slf4j
@BuiltinComponent
public class ReflectionBasedRequestMsgHandler extends AbstractMsgHandler {
    private final Set<MsgType> supportedMsgTypes = new HashSet<>();

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        // 每次都返回同一个实例
        return supportedMsgTypes;
    }
}
```