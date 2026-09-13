---
icon: change
---

# Netty 消息处理线程池

本页介绍 Netty pipeline 使用的协议消息处理线程池。它与业务线程池、Spring 定时任务线程池是不同的资源。

::: warning 3.0.0 注意事项

从 3.0.0 开始，框架通过 `JtEventExecutorGroupProvider` 暴露 Netty 消息处理线程池，不再将裸 `EventExecutorGroup` 注册为 Spring Bean。

升级时是否需要修改自定义 Bean 或注入点，请先阅读[升级到 3.0.0：升级后是否需要修改代码](../migration/upgrade-to-3.0.0.md#升级后是否需要修改代码)。

:::

## 内置线程池配置

内置线程池可以通过 `jt808.msg-processor.executor-group.*` 配置：

```yaml
jt808:
  msg-processor:
    executor-group:
      thread-count: 2
      pool-name: 808-msg-processer
      max-pending-tasks: 128
```

详细配置项参见[消息处理线程池配置](../../config/message-processor.md)。

## Bean 名和类型

各服务器继续使用原有 Bean 名及常量，只有 Bean 类型由 `EventExecutorGroup` 变为 `JtEventExecutorGroupProvider`：

| 服务器         | Bean 名常量                                                     | Bean 名                                         |
|----------------|-----------------------------------------------------------------|-------------------------------------------------|
| 808 指令服务器 | `BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`            | `jt808MsgProcessorEventExecutorGroup`           |
| 808 附件服务器 | `BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP` | `jt808AttachmentMsgProcessorEventExecutorGroup` |
| 1078 服务器    | `BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`             | `jt1078MsgProcessorEventExecutorGroup`          |

这三个 Bean 名保持不变，变化的是 Bean 类型：

```text
旧版: EventExecutorGroup
新版: JtEventExecutorGroupProvider
```

## 替换内置线程池

下面以 808 指令服务器为例。`DefaultJtEventExecutorGroupProvider` 始终返回同一个 executor，并在 Spring 销毁该 Bean 时优雅关闭它：

```java
import io.github.hylexus.jt.netty.DefaultJtEventExecutorGroupProvider;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;

@Configuration
public class SomeConfigurationClass {

    @Bean(
            name = BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP,
            destroyMethod = "close"
    )
    DefaultJtEventExecutorGroupProvider jt808MsgProcessorEventExecutorGroup() {
        return new DefaultJtEventExecutorGroupProvider(
                new DefaultEventExecutorGroup(4)
        );
    }

}
```

808 附件服务器和 1078 服务器只需替换对应的 Bean 名常量；示例中的 Provider 类型和生命周期处理方式相同。

自定义 `JtEventExecutorGroupProvider` 必须在其生命周期内返回同一个 executor。若 executor 由 Provider 创建并持有，Provider 还应通过 `AutoCloseable` 或 Spring Bean destroy method 负责关闭；若 executor 由外部统一管理，则由外部所有者负责关闭。不要在每次调用 `getEventExecutorGroup()` 时创建新的 executor。

## 与 Spring 定时任务的关系

Netty 消息处理线程池和 Spring 定时任务线程池是两类用途不同的资源：

| 线程池                                       | 用途                            | 推荐配置方式                                                             |
|----------------------------------------------|---------------------------------|--------------------------------------------------------------------------|
| `EventExecutorGroup`                         | Netty pipeline 中的协议消息处理 | `jt808.msg-processor.executor-group.*` 或 `JtEventExecutorGroupProvider` |
| `TaskScheduler` / `ScheduledExecutorService` | `@Scheduled` 等应用定时任务     | Spring Boot 默认调度器，或显式声明 `TaskScheduler` Bean                  |

从 3.0.0 开始，框架不再把裸 `EventExecutorGroup` 注册为 Spring Bean。这样 Spring Boot 在查找 `TaskScheduler` 或 `ScheduledExecutorService` 时不会误选 Netty executor。Issue #101 的详细原因分析见 [FAQ：启动时 `initialDelay` 为负数](../../../../frequently-asked-questions/issue-101-scheduled-task-startup-failure.md)。

如果应用已经自行声明 `TaskScheduler`，该 Bean 仍按应用配置使用；不需要把定时任务迁移到 Netty 消息处理线程池。
