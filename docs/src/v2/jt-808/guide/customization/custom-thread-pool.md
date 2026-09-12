---
icon: change
---

# 业务线程池 (v2.3.0-rc.2)

::: tip

业务线程池是 <Badge text="2.3.0-rc.2" type="tip" vertical="middle"/> 中新增的。

:::

## 配置内置业务线程池

```yaml
jt808:
  # Netty 线程池
  msg-processor:
    executor-group:
      thread-count: 2
      pool-name: 808-msg-processer
      max-pending-tasks: 128
  ## 业务线程池(指令服务器 和 附件服务器共用)
  msg-handler:
    enabled: true
    core-pool-size: 64
    max-pool-size: 128
    keep-alive: 1m
    max-pending-tasks: 256
    daemon: true
    pool-name: 808-handler
```

## 替换内置业务线程池

要替换内置的业务线程池，请提供一个 `Jt808ServerSchedulerFactory` 类型的 `Bean`。

```java

@Configuration
public class SomeConfigurationClass {

    @Bean
    Jt808ServerSchedulerFactory jt808ServerSchedulerFactory() {
        return new Jt808ServerSchedulerFactory() {
            @Override
            public ExecutorService getMsgHandlerExecutor() {
                // 这里返回自定义线程池
                return null;
            }
        };
    }

}
```

## Netty 消息处理线程池

Netty pipeline 使用独立的 `EventExecutorGroup` 处理协议消息。框架通过 `JtEventExecutorGroupProvider` 将这些线程池与 Spring 的 `TaskScheduler` 类型查找隔离，避免应用的 `@Scheduled` 任务误用 Netty 消息处理线程。

各服务器继续使用原有 Bean 名及常量，只有 Bean 类型由 `EventExecutorGroup` 变为 `JtEventExecutorGroupProvider`：

| 服务器         | Bean 名常量                                                     | Bean 名                                         |
|----------------|-----------------------------------------------------------------|-------------------------------------------------|
| 808 指令服务器 | `BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`            | `jt808MsgProcessorEventExecutorGroup`           |
| 808 附件服务器 | `BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP` | `jt808AttachmentMsgProcessorEventExecutorGroup` |
| 1078 服务器    | `BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP`             | `jt1078MsgProcessorEventExecutorGroup`          |

### 替换内置 Netty 消息处理线程池

下面以 808 指令服务器为例。`DefaultJtEventExecutorGroupProvider` 始终返回同一个 executor，并在 Spring 销毁该 Bean 时优雅关闭它：

```java
import io.github.hylexus.jt.netty.DefaultJtEventExecutorGroupProvider;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;

@Configuration
public class SomeConfigurationClass {

    // v3.0.0+
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

自定义 `JtEventExecutorGroupProvider` 必须在其生命周期内返回同一个 executor。若 executor 由 Provider 创建并持有，Provider 还应通过 `AutoCloseable` 或 Spring Bean destroy method 负责关闭；若 executor
由外部统一管理，则由外部所有者负责关闭。
