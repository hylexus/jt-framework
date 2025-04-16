---
icon: merge
---

# 升级到2.3.0-rc.5

## 请先读我

::: tip

如果你没有自定义过下面两种类型的组件，可以忽略这个升级提示。

:::

1. `JtServerNettyConfigure` 及其子接口和实现类
    - `Jt808ServerNettyConfigure`
        - 删除了 ~~DefaultJt808ServerNettyConfigure~~
        - 新增 `BuiltinJt808ServerNettyConfigure`
    - `Jt808AttachmentServerNettyConfigure`
        - 删除了 ~~DefaultJt808AttachmentServerNettyConfigure~~
        - 新增 `BuiltinJt808AttachmentServerNettyConfigure`
2. `AbstractRunner` 构造器参数变更

## 变更原因

之前版本的 `JtServerNettyConfigure` 设计不太合理：

```java
// 之前版本的 Jt808ServerNettyConfigure(相关方法已经删除)
public interface Jt808ServerNettyConfigure {

    void configureServerBootstrap(ServerBootstrap serverBootstrap);

    void configureSocketChannel(SocketChannel ch);
}
```

默认实现中在 `configureServerBootstrap()` 方法中调用了 `configureSocketChannel()`，这可能导致用户覆盖掉对 `configureSocketChannel()` 方法的调用。

下面是新的 `Jt808ServerNettyConfigure` 接口方法:

```java
// 新的 Jt808ServerNettyConfigure
public interface Jt808ServerNettyConfigure extends JtServerNettyConfigure {

    /**
     * @param configProvider 可以从中读取配置项(环境变量、系统属性、application.yaml、...)
     * @return 返回入参中的 {@code serverBootstrap} 或 返回一个新的 {@link ServerBootstrap ServerBootstrap} 实例
     */
    ServerBootstrap configureServerBootstrap(ConfigurationProvider configProvider, ServerBootstrap serverBootstrap);

    void configureSocketChannel(ConfigurationProvider configProvider, SocketChannel ch);
}
```

新版本的 `configureServerBootstrap()` 和 `configureSocketChannel()` 方法的调用移动到了 `AbstractRunner` 中，并且 `AbstractRunner` 的构造器参数也发生了变化。

## 升级参考

如果你没有自定义 `JtServerNettyConfigure` 及其子接口和实现类，那么你不需要做任何改动。

反之，请参考下面链接：

- [定制化 - Netty相关 - 自定义示例](../../guide/customization/netty-config.md#示例)
