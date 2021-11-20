# JT-framework

::: tip
本文档将介绍808协议处理方式，内容如下：
:::

- [808协议入门](./basic)
    - [协议扫盲](./basic/protocol-introduction.md)
    - [快速开始](./basic/quick-start.md)
- [定制化](./customization/)
    - [Session相关](customization/session-config.md)
    - [Netty相关](./customization/netty-config.md)
    - [转义相关](./customization/escape-config.md)
    - [消息类型](./customization/msg-type-config.md)
    - [消息转换器](./customization/msg-converter-config.md)
    - [消息处理器](./customization/msg-handler-config.md)
    - [TerminalValidator](./customization/terminal-validator-config.md)
    - [AuthValidator](./customization/auth-validator-config.md)
- [注解驱动开发](./annotation-based-dev)
    - [请求消息映射](./annotation-based-dev/req-msg-mapping.md)
    - [消息处理器注册](./annotation-based-dev/msg-handler-register.md)
    - [响应消息映射](./annotation-based-dev/resp-msg-mapping.md)
    - [消息下发](./annotation-based-dev/msg-push.md)
    - [异常处理](./annotation-based-dev/exception-handler.md)
    - [位置上传报文解析示例](./annotation-based-dev/location-msg-parse-demo.md)
- [深入](./more)
    - [消息处理流程](./more/design-of-msg-processing.md)
    - [组件顺序](./more/component-order.md)
    - [Aware接口](./more/aware-interface.md)
- [FAQ](./FAQ)