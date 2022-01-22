# 1.x 和 2.x 有什么区别?

- 重写了核心 `API`(`Fluent` 风格)
- 使用 `Netty` 的 `ByteBuf` 代替了 **1.x** 中的 `byte[ ]`
- 支持分包
- 支持混合版本(`2011/2013/2019`)
- 注解属性支持 `SpEL(Spring-Expression-Language)`，不再依赖 `XxxAware` 接口
- 替换内置组件不再需要继承指定的类
- 新增 `Jt808HandlerInterceptor`

::: danger 但是

`2.x` 不向下兼容 `1.x`

:::
