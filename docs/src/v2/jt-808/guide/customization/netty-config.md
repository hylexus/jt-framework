---
icon: leaf
---

# Netty相关

要自定义 `Netty` 相关配置，只需声明一个 `Jt808ServerNettyConfigure` 类型的 `Bean` 即可。

```java
public interface Jt808ServerNettyConfigure {

    void configureServerBootstrap(ServerBootstrap serverBootstrap);

    void configureSocketChannel(SocketChannel ch);
}
```
