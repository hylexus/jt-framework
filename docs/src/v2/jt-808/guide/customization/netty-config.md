---
icon: leaf
---

# Netty相关

要自定义 `Netty` 相关配置，只需声明一个 `Jt808ServerNettyConfigure` 类型的 `Bean` 即可。

## 2.3.0-rc.5以及之后版本

```java
public interface Jt808ServerNettyConfigure extends JtServerNettyConfigure {

    /**
     * @param configProvider 可以从中读取配置项(环境变量、系统属性、application.yaml、...)
     * @return 返回入参中的 {@code serverBootstrap} 或 返回一个新的 {@link ServerBootstrap ServerBootstrap} 实例
     */
    ServerBootstrap configureServerBootstrap(ConfigurationProvider configProvider, ServerBootstrap serverBootstrap);

    void configureSocketChannel(ConfigurationProvider configProvider, SocketChannel ch);
}
```

## 示例

推荐继承默认实现类来自定义组件:

- `BuiltinJt808ServerNettyConfigure`
- `BuiltinJt808AttachmentServerNettyConfigure`

当然可以直接实现 `Jt808ServerNettyConfigure` 接口，下面是一个示例：

```java

@Component
public class MyJt808ServerNettyConfigure implements Jt808ServerNettyConfigure {

    private static final Logger log = LoggerFactory.getLogger(MyJt808ServerNettyConfigure.class);
    protected final Jt808ServerProps serverProps;

    public MyJt808ServerNettyConfigure(
            Jt808ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    @Override
    public ServerBootstrap configureServerBootstrap(ConfigurationProvider configProvider, ServerBootstrap serverBootstrap) {
        final Jt808NettyTcpServerProps nettyProps = serverProps.getServer();
        final boolean preferEpoll = nettyProps.isPreferEpoll();
        final Class<? extends ServerChannel> channelClass;
        if (preferEpoll && Epoll.isAvailable()) {
            channelClass = EpollServerSocketChannel.class;
            serverBootstrap.group(new EpollEventLoopGroup(nettyProps.getBossThreadCount()), new EpollEventLoopGroup(nettyProps.getWorkerThreadCount()))
                    .channel(channelClass);
        } else {
            channelClass = NioServerSocketChannel.class;
            serverBootstrap.group(new NioEventLoopGroup(nettyProps.getBossThreadCount()), new NioEventLoopGroup(nettyProps.getWorkerThreadCount()))
                    .channel(channelClass);
        }
        log.info("Epoll.isAvailable(): {}, jt808.server.prefer-epoll: {}, channelClass: {}", Epoll.isAvailable(), preferEpoll, channelClass.getName());

        return serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 2048)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public void configureSocketChannel(ConfigurationProvider configProvider, SocketChannel ch) {
        ch.pipeline().addLast("", someHandler1);
        ch.pipeline().addLast("", someHandler2);
        ch.pipeline().addLast("", someHandler3);
        //...
    }

}
```

## 2.3.0-rc.5之前版本

```java
public interface Jt808ServerNettyConfigure {

    void configureServerBootstrap(ServerBootstrap serverBootstrap);

    void configureSocketChannel(SocketChannel ch);
}
```
