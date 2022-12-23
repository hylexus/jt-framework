# 注解驱动开发

::: tip 提示
从客户端接收消息时，字节数组到消息实体类的映射，内部已经 `提供并自动注册` 了一个叫 `ReflectionBasedRequestMsgBodyConverter` 的转换器来完成。

所以，对于 `常见的报文` 可以直接基于注解来完成自动映射，而没必要提供一个 `RequestMsgBodyConverter` 。
:::

::: danger 注意：
主动下发消息给客户端时的编码问题，目前只提供了一个最基本的内测版的注解。并且可能在后续版本删除。

所以，本文档暂时不会介绍这部分内容。
:::

::: tip
本小节主要内容如下：
:::

- [请求消息映射](src/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.md)
- [消息处理器注册](src/v1/jt-808/guide/annotation-based-dev/msg-handler-register.md)
- [响应消息映射](src/v1/jt-808/guide/annotation-based-dev/resp-msg-mapping.md)
- [消息下发](src/v1/jt-808/guide/annotation-based-dev/msg-push.md)
- [异常处理](src/v1/jt-808/guide/annotation-based-dev/exception-handler.md)
- [位置上传报文解析示例](src/v1/jt-808/guide/annotation-based-dev/location-msg-parse-demo.md)
