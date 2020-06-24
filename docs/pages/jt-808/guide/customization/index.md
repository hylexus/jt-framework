# 定制自己的组件

::: tip 提示
目前几乎所有的自定义的配置都需要继承配置类 `io.github.hylexus.jt808.support.netty.Jt808ServerConfigure` 。
可以通过如下代码片段的方式来自定义组件：

```java
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Jt808Config extends Jt808ServerConfigure {
    // TODO 重写配置方法或覆盖父类的Bean
}

```
:::


::: tip
本小节主要内容如下：
:::

- [Session相关配置](./session-config.md)
- [Netty相关配置](./netty-config.md)
- [转义相关配置](./escape-config.md)
- [消息转换器配置](./msg-converter-config.md)
- [消息处理器配置](./msg-handler-config.md)