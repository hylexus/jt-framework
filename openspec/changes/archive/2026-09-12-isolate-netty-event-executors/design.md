## 背景

变更动机见 `proposal.md`。当前共享自动配置会创建三个具名的 `EventExecutorGroup` Bean，分别用于 808 指令服务器、808 附件服务器和 1078 服务器。内置 Netty 配置和示例配置均通过限定符直接注入这些 Bean。TCP 服务器的关闭流程会停止 boss 和 worker 事件循环组，而对于单独注册的消息处理 executor Bean，目前依赖 Spring 推断并执行其关闭方法。

自动配置模块由 Spring Boot 2 和 Spring Boot 3 starter 共用，因此该方案不能依赖仅 Spring 6 支持的 Bean 候选控制能力。具体要求见 `specs/netty-executor-isolation/spec.md`。

## 目标与非目标

**目标：**

- 对 Spring 基于类型的 `ScheduledExecutorService` 查找隐藏框架创建的消息处理 executor。
- 明确 executor 的创建、配置、选择和优雅关闭机制。
- 为 808 指令服务器、808 附件服务器和 1078 服务器提供一致的自定义 API。
- 通过协议相关的 Bean 名消除多个 Provider Bean 之间的歧义。

**非目标：**

- 修改 Netty 线程数、队列上限、拒绝策略或 pipeline 顺序。
- 代表使用方添加或配置应用级 `TaskScheduler`。
- 修补 Spring 的开始时间计算或 Netty 的周期任务参数校验。
- 保留以 `EventExecutorGroup` 类型直接注入或覆盖这些 Bean 的运行时兼容性。

## 技术决策

### 1. 在 `jt-core` 中引入 JT 专用 Provider API

在 `io.github.hylexus.jt.netty` 包下新增公开接口 `JtEventExecutorGroupProvider`，通过一个方法返回 `EventExecutorGroup`。Provider 接口本身不得继承 `EventExecutorGroup`、`Executor`、`ExecutorService` 或 `ScheduledExecutorService`。

新增一个可复用的默认实现：构造时接收一个 `EventExecutorGroup`，始终返回同一个实例，并提供明确的关闭操作以发起优雅关闭。自动配置使用每组属性创建 `DefaultEventExecutorGroup`，再用默认 Provider 包装，并将 Provider 的关闭操作声明为 Bean destroy method。

该 API 放在 `jt-core` 中，因为它与具体协议无关，两个协议支持栈都已经依赖该模块，而且该模块已有共用的 Netty 配置抽象。

考虑过的替代方案：

- 放弃实现 `EventExecutorGroup` 的 `Wrapper`，因为它仍可赋值给 `ScheduledExecutorService`，无法解决 Spring 的类型发现问题。
- 简单 Holder 类也能隐藏类型，但 Provider 更能表达这是一个受支持的应用级自定义扩展点。
- 不采用 Spring 的 Bean 候选标记，因为该能力在受支持的 Spring 5/6 和 Spring Boot 2/3 版本间行为不一致。

### 2. 保留现有 Bean 名，将 Bean 类型改为 Provider

808 指令消息处理器、808 附件消息处理器和 1078 消息处理器继续使用现有 Bean 名及其公开常量。每个自动配置方法都改为返回默认 Provider，仍按原 Bean 名执行 back-off，并继续沿用现有 executor 属性映射。Spring 的调度器判断基于 Bean 类型而非 Bean 名，因此保持名称不变不会影响隔离效果。

内置 Netty 配置工厂方法注入带对应限定符的 Provider，并将 `provider.getEventExecutorGroup()` 传给现有 Netty 配置构造器。构造器和 pipeline 类仍可继续使用 Netty 原生类型，只有 Spring Bean 边界发生变化。

原有三个 Bean 名常量及其字符串值保持不变，不新增 Provider 专用常量，也不提供额外别名。该变更只改变对应 Bean 的类型契约：由 `EventExecutorGroup` 变为 `JtEventExecutorGroupProvider`。这可以保留限定符、条件覆盖和配置引用的稳定性，同时让旧代码在类型注入处明确暴露迁移需求。

考虑过的替代方案：注册框架级 `TaskScheduler` 可以掩盖启动失败，但会使 starter 承担应用任务调度职责，还可能覆盖应用希望选择的 `ScheduledExecutorService`，因此选择隔离内部资源，而不是增加调度器。

### 3. 明确定义 Provider 所有权和生命周期

默认 Provider 拥有传入的 executor，并在 Spring 销毁 Provider Bean 时优雅关闭该 executor。返回稳定实例可以确保由同一 Provider 配置的所有 pipeline 共用预期的消息处理线程池。

应用提供自定义 Provider 时，由应用负责其生命周期约定。如果 Provider 拥有 executor，可以实现 `AutoCloseable` 或其他 Spring 支持的销毁机制；如果 executor 由外部统一管理，则可将关闭责任留给外部所有者。框架不会单独销毁自定义 Provider 返回的 executor，以避免重复关闭。

### 4. 将调度器隔离作为 Bean 选择契约进行测试

为 Provider 增加聚焦于稳定实例和优雅关闭的单元测试。增加自动配置上下文测试，覆盖三个具名 Provider Bean、默认配置 back-off、正确的限定符选择，以及上下文中不存在框架拥有的 `ScheduledExecutorService` Bean。

在 Spring Boot 2 和 Spring Boot 3 starter 层增加调度回归测试：启用注解调度，在 JT/T Provider 存在时验证 Spring 仍能创建并选择自己的 `TaskScheduler`。通过断言 Bean 类型和调度器选择使测试保持确定性；纳秒级负延迟依赖执行时序，不作为主要断言。

更新 customized 808 sample，使其注入具名 Provider，并在创建自定义 Netty 配置时取出其中的 executor。

### 5. 为旧版同名 executor Bean 提供精确的迁移诊断

Spring Boot 默认的缺 Bean 分析只能看到最终缺少 `JtEventExecutorGroupProvider`，无法说明旧版同名 `EventExecutorGroup` Bean 已经使 `@ConditionalOnMissingBean(name = ...)` 回退，因而阻止默认 Provider 创建。808 和 1078 自动配置模块分别提供 `FailureAnalyzer`，继续使用 Spring Boot 默认的 `FailureAnalysisReporter` 输出结果。

诊断器仅在以下条件同时成立时返回分析结果：异常链缺少的类型是 `JtEventExecutorGroupProvider`；注入点限定符是当前协议支持的既有 Bean 名；BeanFactory 中确实存在该名称的 Bean；该 Bean 类型实现了 `EventExecutorGroup`。其他异常返回 `null`，交由 Spring Boot 的通用分析器处理。

诊断器通过 `META-INF/spring.factories` 注册，并设置高于通用缺 Bean分析器的优先级。该扩展点和注册方式同时受 Spring Boot 2.7 与 3.3 支持。错误说明应明确“旧 Bean 使默认 Provider 自动配置回退”，操作建议应要求保留原 Bean 名、将类型改为 `JtEventExecutorGroupProvider`，并链接 issue #101。

考虑过在自动配置阶段主动抛出专用异常，但这会把迁移诊断混入正常 Bean 创建流程。使用 `FailureAnalyzer` 可以保持配置逻辑不变，并且只改变启动失败时的呈现。

## 风险与权衡

- [现有应用注入或覆盖旧版裸 executor Bean] -> 将变更标记为 Bean 类型层面的 breaking change，保持 Bean 名和常量不变，记录变更前后的类型配置方式，并更新仓库示例。
- [自定义 Provider 每次调用都意外创建新 executor] -> 记录稳定实例约定并纳入能力规格；默认实现强制保持实例一致。
- [应用上下文关闭时 executor 线程泄漏] -> 为默认 Provider 配置明确的 destroy method，并在生命周期测试中验证关闭状态。
- [同时启用多个 JT/T 服务器时解析到错误 Provider] -> 所有注入点均使用不同的常量和限定符，并测试同时包含多个 Provider 的上下文。
- [外部管理的自定义 executor 被重复关闭] -> 自动配置只管理默认 Provider；自定义 Provider 的生命周期由应用控制。
- [迁移诊断错误接管无关的缺 Bean 异常] -> 同时校验缺失类型、限定符、既有 Bean 名和旧 Bean 实际类型；任一条件不满足即返回 `null`。

## 迁移方案

1. 新增 Provider API 和默认实现，不额外注册裸 executor Bean。
2. 保留三个现有 Bean 名常量，将对应自动配置 Bean 的返回类型改为 Provider。
3. 将 808、1078 自动配置和 customized sample 改为注入带限定符的 Provider。
4. 为受支持的 starter 版本增加确定性的隔离、back-off、选择和生命周期测试。
5. 在发布说明或相关自定义文档中记录从裸 `EventExecutorGroup` Bean 迁移到 Provider Bean 的方法。

使用方迁移时，使用自定义 Provider 包装现有 `EventExecutorGroup`，继续以原服务器专用 Bean 名注册，然后将自定义 Netty 配置的注入类型由裸 executor 改为 Provider。

回滚时恢复原有裸具名 Bean 并撤销 Provider 注册即可；该变更不涉及持久化数据或线路协议迁移。
