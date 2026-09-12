---
icon: merge
---

# 升级到 3.0.0

JT/T 808 指令服务器、JT/T 808 附件服务器和 JT/T 1078 服务器不再将裸 `EventExecutorGroup` 注册为 Spring Bean，而是注册 `JtEventExecutorGroupProvider`。这可以防止 Spring 将 Netty 消息处理 executor 误选为应用的
`ScheduledExecutorService`。

## 不变项

三个 Bean 名及对应的公开常量保持不变。已有的 `@Qualifier` 仍使用原常量：

```java
BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP
        BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP
BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP
```

## 需要修改的代码

之前直接注入裸 executor：

```java
public Jt808ServerNettyConfigure jt808ServerNettyConfigure(
        @Qualifier(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
        EventExecutorGroup eventExecutorGroup,
        Jt808ServerProps serverProps,
        Jt808DispatchChannelHandlerAdapter channelHandlerAdapter,
        Jt808TerminalHeatBeatHandler heatBeatHandler) {

    return new BuiltinJt808ServerNettyConfigure(
            serverProps,
            eventExecutorGroup,
            channelHandlerAdapter,
            heatBeatHandler
    );
}
```

升级后注入 Provider，再取出其中的 executor：

```java
public Jt808ServerNettyConfigure jt808ServerNettyConfigure(
        @Qualifier(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
        JtEventExecutorGroupProvider eventExecutorGroupProvider,
        Jt808ServerProps serverProps,
        Jt808DispatchChannelHandlerAdapter channelHandlerAdapter,
        Jt808TerminalHeatBeatHandler heatBeatHandler) {

    return new BuiltinJt808ServerNettyConfigure(
            serverProps,
            eventExecutorGroupProvider.getEventExecutorGroup(),
            channelHandlerAdapter,
            heatBeatHandler
    );
}
```

如果应用覆盖了原有具名 `EventExecutorGroup` Bean，也需要保持原 Bean 名不变，并将返回类型和 Bean 实例改为 `JtEventExecutorGroupProvider`
。默认实现和完整生命周期示例见[业务线程池](../customization/custom-thread-pool.md#替换内置-netty-消息处理线程池)。

## 升级失败提示

如果升级到 3.0.0 后仍保留旧版具名 `EventExecutorGroup` Bean，该 Bean 会使框架的默认 Provider 自动配置回退，但其类型又不能满足新的 Provider 注入点，应用将启动失败。

框架会针对 808 指令服务器、808 附件服务器和 1078 服务器识别这种迁移冲突，并给出类似下面的分析：

```text
Description:

Bean 'jt808MsgProcessorEventExecutorGroup' uses the legacy executor contract:
    Bean type: io.netty.util.concurrent.DefaultEventExecutorGroup
    Legacy type: io.netty.util.concurrent.EventExecutorGroup

Since jt-framework 3.0.0, this bean name must identify:
    io.github.hylexus.jt.netty.JtEventExecutorGroupProvider

The existing bean caused the default Provider auto-configuration to back off,
but it cannot satisfy the Netty configuration injection point.

Action:

Keep the existing bean name:
    jt808MsgProcessorEventExecutorGroup

Change the @Bean method's return type to:
    io.github.hylexus.jt.netty.JtEventExecutorGroupProvider

Wrap the executor with:
    io.github.hylexus.jt.netty.DefaultJtEventExecutorGroupProvider

Migration details:
    https://github.com/hylexus/jt-framework/issues/101
```

Bean 名不需要修改，只需按照上面的示例替换 Bean 类型。问题背景和修复原因见 [issue #101](https://github.com/hylexus/jt-framework/issues/101)。
