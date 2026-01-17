---
icon: proposal
---

# 请先读我

## 介绍 <Badge text="3.0.0" type="tip" vertical="top"/>

从 <Badge text="3.0.0" type="tip" vertical="middle"/> 开始，**jt-framework** 适配了 [xtream-codec-core](https://hylexus.github.io/xtream-codec/guide/core/annotation-driven/builtin-annotations.html) 的注解。

## 前置条件

- <span style="color:red;">JDK21+</span>
- spring-boot-2.x 或 spring-boot-3.x

::: tip 提示

[xtream-codec](https://github.com/hylexus/xtream-codec) 是基于 <span style="color:red;">JDK21+</span> 的，
因此在 jt-framework 中使用 **xtream-codec** 的编解码库，必须要 <span style="color:red;">JDK21+</span>。

:::

## xtream-codec 注解 VS jt-framework 注解

### 数据处理维度

**jt-framework** 注解对请求和响应分开处理了：

- `@RequestField`
- `@ResponseField`
- 另外提供了两种内置别名注解
    - `@RequestFieldAlias`
    - `@ResponseFieldAlias`

**xtream-codec** 注解不区分请求和响应：

- 底层统一使用 `@XtreamField` 这一个注解
- 另外提供了两种内置别名注解
    - `@Preset.RustStyle.XXX()`: **rust** 风格的别名注解
    - `@Preset.JtStyle.XXX()`: **JT** 风格的别名注解

### 数据类型维度

- **xtream-codec** 注解支持的数据类型更加丰富
- **xtream-codec** 自定义编解码器更加灵活
- **xtream-codec** 注解支持 `Record`

具体参考: [xtream-codec-core 内置类型对比](https://hylexus.github.io/xtream-codec/guide/core/annotation-driven/builtin-annotations.html#%E5%86%85%E7%BD%AE%E7%B1%BB%E5%9E%8B%E5%AF%B9%E6%AF%94)

### 混合版本

- **xtream-codec** 注解支持在同一个实体类上处理不同版本的协议字段

### 调试埋点

**xtream-codec** 支持调试埋点。

具体参考: [xtream-codec-core 调试埋点](https://hylexus.github.io/xtream-codec/guide/core/annotation-driven/entity-codec-tracker.html)
