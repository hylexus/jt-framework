# 定制化

::: tip 提示

- 从 `1.0.7-RELEASE` 开始，几乎所有的自定义的配置都需要继承配置类 `io.github.hylexus.jt808.boot.config.Jt808ServerConfigurationSupport`
- 并且`不再支持`使用 `io.github.hylexus.jt808.support.netty.Jt808ServerConfigure` 配置（容易导致Bean之间的循环依赖）

可以通过如下代码片段的方式来自定义组件：

```java

@Configuration
public class Jt808Config extends Jt808ServerConfigurationSupport {
    // TODO 重写配置方法或覆盖父类的Bean
}

```

:::

::: tip

本小节主要内容如下：

:::

- [Session相关配置](src/v1/jt-808/guide/customization/session-config.md)
- [Netty相关配置](src/v1/jt-808/guide/customization/netty-config.md)
- [转义相关配置](src/v1/jt-808/guide/customization/escape-config.md)
- [消息类型配置](src/v1/jt-808/guide/customization/msg-type-config.md)
- [消息转换器配置](src/v1/jt-808/guide/customization/msg-converter-config.md)
- [消息处理器配置](src/v1/jt-808/guide/customization/msg-handler-config.md)
- [TerminalValidator](src/v1/jt-808/guide/customization/terminal-validator-config.md)
- [AuthValidator](src/v1/jt-808/guide/customization/auth-validator-config.md)