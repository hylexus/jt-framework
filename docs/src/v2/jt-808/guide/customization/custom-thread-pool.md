---
icon: change
---

# 业务线程池(v2.3.0-rc.2)

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
