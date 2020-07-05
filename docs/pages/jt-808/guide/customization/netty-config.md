# Netty相关

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::

Netty相关配置需要继承 `Jt808ServerConfigurationSupport` 来覆盖默认逻辑。

```java
@Configuration
public class Jt808Config extends Jt808ServerConfigurationSupport {

    @Override
    public Jt808ServerNettyConfigure jt808ServerNettyConfigure(HeatBeatHandler heatBeatHandler, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        return new DemoJt808ServerNettyConfigure(heatBeatHandler, jt808ChannelHandlerAdapter);
    }
}
```

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::