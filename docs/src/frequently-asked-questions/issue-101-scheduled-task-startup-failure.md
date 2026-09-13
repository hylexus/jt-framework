# Issue #101：启动时 `initialDelay` 为负数

## 现象

应用启动时，Spring 上下文初始化失败，日志中出现类似错误：

```text
java.lang.IllegalArgumentException: initialDelay: -10278 (expected: >= 0)
```

这个问题通常发生在应用包含 `@Scheduled` 任务，同时使用 JT/T 808 或 JT/T 1078 Spring Boot starter 的旧版本时。Windows 和 Linux 都可能出现，Linux 日志中经常恰好在错误前打印 epoll 信息，因此容易误以为是 epoll 导致的故障。

## 根因分析

问题是 Spring 定时任务和 Netty 消息处理线程池之间的类型误识别，主要经过以下步骤：

### 1. Netty executor 被注册成了 Spring Bean

旧版本将 Netty 的 `EventExecutorGroup` 直接注册为具名 Spring Bean，例如：

```text
jt808MsgProcessorEventExecutorGroup
```

Netty 的 `EventExecutorGroup` 同时实现了 `ScheduledExecutorService`。因此，虽然这个线程池的真实用途是处理 Netty pipeline 中的协议消息，但 Spring 也会把它看作一个可用于定时任务的 executor。

### 2. Spring Boot 默认调度器因此没有创建

Spring Boot 的任务调度自动配置会在容器中不存在 `TaskScheduler` 和 `ScheduledExecutorService` 时创建默认调度器。

由于容器中已经存在 Netty `EventExecutorGroup` Bean，`ScheduledExecutorService` 的缺失条件不成立，默认的 `ThreadPoolTaskScheduler` 可能不会创建。

### 3. Spring Framework 回退到 Netty executor

Spring Framework 6.1 的 `TaskSchedulerRouter` 在找不到 `TaskScheduler` Bean 时，会继续查找 `ScheduledExecutorService` Bean，并将其包装为 `ConcurrentTaskScheduler`。

结果是，应用的 `@Scheduled` 任务被调度到了 Netty 消息处理线程池，而不是应用专用的 Spring 调度器。

### 4. 两次时间读取产生负的延迟

对于固定延迟任务，Spring 会先根据当前时间和 `initialDelay` 计算目标开始时间，随后在 `ConcurrentTaskScheduler` 中再次读取当前时间并计算两者之间的 `Duration`。

如果第二次读取发生在第一次读取之后，计算结果就可能小于零。使用纳秒级时钟时，这个窗口更容易暴露；负值的具体大小会随启动时序变化，因此日志中的数值通常不固定。

### 5. Netty 严格拒绝负延迟

由于任务最终被提交到 Netty 的 `AbstractScheduledEventExecutor`，Netty 会严格检查 `initialDelay >= 0`，于是抛出异常并取消 Spring 上下文启动。

这也解释了为什么某些 Java 标准线程池表现为“立即执行”，而 Netty executor 直接启动失败：两者对负延迟的处理规则不同。

## epoll 是根因吗？

不是。epoll/NIO 日志只是异常发生前恰好打印出的服务器配置，问题根因是 Netty executor 暴露为 `ScheduledExecutorService` 后被 Spring 选作定时任务调度器。

Windows 同样可能触发这个竞态，Linux 只是更容易观察到，并不代表需要关闭 epoll。

## 如何修复

从 3.0.0 开始，框架使用 `JtEventExecutorGroupProvider` 暴露 Netty 消息处理线程池，不再让 Spring 将它识别为 `ScheduledExecutorService`。因此建议所有用户升级到 3.0.0。

如果应用覆盖了框架的具名 `EventExecutorGroup` Bean，升级时还需要保持 Bean 名不变，并改为返回 `JtEventExecutorGroupProvider`。具体代码修改见[升级到 3.0.0：升级后是否需要修改代码](../v2/jt-808/guide/migration/upgrade-to-3.0.0.md#升级后是否需要修改代码)。

如果暂时无法升级旧版本，也可以显式声明独立的 `TaskScheduler` Bean，避免应用定时任务回退到 Netty executor；但这只是临时规避方案，仍建议升级框架。

相关 Issue：[hylexus/jt-framework#101](https://github.com/hylexus/jt-framework/issues/101)
