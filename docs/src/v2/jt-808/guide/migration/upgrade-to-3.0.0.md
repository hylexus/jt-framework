---
icon: merge
---

# 升级到 3.0.0

JT/T 808 指令服务器、JT/T 808 附件服务器和 JT/T 1078 服务器不再将裸 `EventExecutorGroup` 注册为 Spring Bean，而是注册 `JtEventExecutorGroupProvider`。

这项调整对应 Issue [#101](https://github.com/hylexus/jt-framework/issues/101)。问题现象、根因和 epoll 排查说明见 [FAQ：Issue #101：启动时 `initialDelay` 为负数](../../../../frequently-asked-questions/issue-101-scheduled-task-startup-failure.md)。

## 升级后是否需要修改代码

建议所有用户升级到 **3.0.0**，以获得 Issue #101 的修复。本节只判断升级时是否需要修改应用代码：

| 项目情况                                                                                       | 升级后是否需要修改代码                                                                |
|------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| 没有定义这些 Bean，只使用框架默认配置                                                          | 不需要。3.0.0 已自动使用 `JtEventExecutorGroupProvider`。                             |
| 仍有返回 `EventExecutorGroup` 的同名 `@Bean` 方法                                              | 需要。保留 Bean 名，将返回类型和实例改为 `JtEventExecutorGroupProvider`。             |
| 自定义配置类通过 `@Qualifier` 注入这些 Bean，且参数类型是 `EventExecutorGroup`                 | 需要。参数类型改为 `JtEventExecutorGroupProvider`，再调用 `getEventExecutorGroup()`。 |
| 通过 `ApplicationContext.getBean(Bean 名, EventExecutorGroup.class)` 等方式按名称获取这些 Bean | 需要。按 `JtEventExecutorGroupProvider` 获取，再调用 `getEventExecutorGroup()`。      |
| 只自定义了应用自己的 `TaskScheduler` 或 `@Scheduled` 配置                                      | 不需要。`TaskScheduler` 与 Netty 消息处理线程池是两套独立资源。                       |

需要检查的 Bean 名是：

- `jt808MsgProcessorEventExecutorGroup`
- `jt808AttachmentMsgProcessorEventExecutorGroup`
- `jt1078MsgProcessorEventExecutorGroup`

建议先搜索上面的 Bean 名。如果搜不到，再搜索下面的常量名或其全限定名，因为应用通常通过 `@Bean(name = 常量)`、`@Qualifier(常量)` 或静态导入使用它们：

- `BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`
- `io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`
- `BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`
- `io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`
- `BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`
- `io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`

如果 Bean 名和常量都搜索不到，升级时通常不需要修改代码；如果搜索到旧版 `EventExecutorGroup` 类型的定义，请继续阅读下面的代码修改示例。无论是否需要修改代码，都建议升级到 3.0.0。

## 不变项

三个 `Bean` 名及对应的公开常量保持不变。已有的 `@Qualifier` 仍使用原常量：

```text
io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP
io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP
io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP
```

## 需要修改的代码

### 覆盖具名 Bean

以下写法在 **3.0.0** 中需要修改。
`Bean` 名保持不变，但返回类型必须改为 `JtEventExecutorGroupProvider`：

```java {3}
// 旧版
@Bean(name = BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
public EventExecutorGroup eventExecutorGroup() {
    return new DefaultEventExecutorGroup(4);
}
```

```java {3}
// 3.0.0+
@Bean(name = BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, destroyMethod = "close")
public JtEventExecutorGroupProvider eventExecutorGroup() {
    return new DefaultJtEventExecutorGroupProvider(
            new DefaultEventExecutorGroup(4)
    );
}
```

如果方法返回的是 `DefaultEventExecutorGroup`，或者返回类型是 `EventExecutorGroup` 但 Bean 名是上述三个名称，也需要按同样方式修改。不要只修改方法名，或继续暴露裸 `EventExecutorGroup`。

### 修改自定义注入点

之前直接注入裸 executor：

```java {3}
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

升级后注入 `Provider`，再取出其中的 `executor`：

```java {3}
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

如果代码通过 `ApplicationContext`、`BeanFactory` 等 API 按名称获取线程池，也要先获取 Provider：

```java {5}
JtEventExecutorGroupProvider provider = applicationContext.getBean(
        BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP,
        JtEventExecutorGroupProvider.class
);
EventExecutorGroup eventExecutorGroup = provider.getEventExecutorGroup();
```

如果应用覆盖了原有具名 `EventExecutorGroup` Bean，也需要保持原 Bean 名不变，并将返回类型和 Bean 实例改为 `JtEventExecutorGroupProvider`。默认实现和完整生命周期示例见[业务线程池](../customization/custom-thread-pool.md#替换内置-netty-消息处理线程池)。

## 升级失败提示

如果升级到 **3.0.0** 后仍保留旧版具名 `EventExecutorGroup` Bean，该 `Bean` 会使框架的默认 `Provider` 自动配置回退，但其类型又不能满足新的 `Provider` 注入点，应用将启动失败。

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

`Bean` 名不需要修改，只需按照上面的示例替换 Bean 类型。修改后请确认：

1. `@Qualifier` 仍使用原来的 `Bean` 名常量。
2. 注入点类型改为 `JtEventExecutorGroupProvider`。
3. 传给 Netty 配置或 `pipeline` 的对象来自 `provider.getEventExecutorGroup()`。
4. `Provider` 返回同一个 `executor`，并由明确的所有者负责关闭。

问题背景和修复原因见 [FAQ：Issue #101：启动时 `initialDelay` 为负数](../../../../frequently-asked-questions/issue-101-scheduled-task-startup-failure.md)。
