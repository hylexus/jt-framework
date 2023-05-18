---
icon: async
---

# 请求处理流程

在本项目中，所有请求的处理都是一个风格 ：

- 接收 `Jt808Request` (或由 `Jt808Request` 转换出来的其他类型)
- 处理业务逻辑
- 返回 `Jt808Response` (或可以转换为 `Jt808Response` 的其他类型)

具体流程可以结合 [请求生命周期监听器](../customization/request-lifecycle-listener.md) 来了解。

