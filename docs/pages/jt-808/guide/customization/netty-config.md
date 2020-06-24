# Netty相关

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::

Netty相关配置需要继承 `Jt808ServerConfigure` 来覆盖默认逻辑。

```java
@Configuration
public class Jt808Config extends Jt808ServerConfigure {
    @Override
    public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
        // ServerBootstrap 等的配置
        super.configureServerBootstrap(serverBootstrap);
    }
    
    @Override
    public void configureSocketChannel(SocketChannel ch, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        // SocketChannel、ChannelPipeline 等的配置
        super.configureSocketChannel(ch, jt808ChannelHandlerAdapter);
    }
}
```

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::