## Why

[issue #103](https://github.com/hylexus/jt-framework/issues/103) 指出了位置附加项解析缓存的并发初始化风险。源码确认共享解析器会并发读写普通 `HashMap`，并在 key 映射和默认描述符构建完成前发布缓存；需要修复这些缺陷，同时保持既有 API、注解语义和成功串行解析结果兼容。

## What Changes

- 将每个位置附加项字段的完整 key 映射与默认描述符作为一个缓存单元，完整构建后安全发布，发布后不再修改。
- 修复注解解码器、注解编码器中自定义转换器实例缓存的并发读写和发布问题，保持既有实例复用、反射构造和转换器选择行为。
- 明确兼容性约束：注解定义、默认值、别名、字段顺序、条件、长度计算、描述符选择、重复 key 覆盖行为、结果顺序和资源所有权保持不变；不新增配置校验或迁移要求。
- 对空 `keyValueMappings` 按既有默认描述符语义解析，消除未创建内层缓存导致的空指针。
- 增加并发首次访问、受控初始化窗口、字段隔离及既有注解行为的回归验证。

## Capabilities

### New Capabilities

- `jt808-codec-cache-concurrency`: 规定位置附加项配置和自定义转换器缓存的并发安全行为，以及修复必须遵守的兼容性契约。

### Modified Capabilities

无。现有 `netty-executor-isolation` 规格不涉及本次缓存行为。

## Impact

- 实现范围：`ExtendedJt808FieldDeserializerLocationExtraItem`、`Jt808AnnotationBasedDecoder`、`Jt808AnnotationBasedEncoder` 及相关 `jt-808-server-support` 测试。
- 对外接口：不修改 `LocationMsgExtraItemMapping`、`KeyValueMapping`、`ValueDescriptor`、转换器接口或现有公开方法；不要求用户修改消息类、自定义转换器或配置。
- 兼容环境：保持 `jt-808-server-support` 的 Java 8 最低兼容目标，适用于现有 Spring Boot 2、Spring Boot 3 使用方式；不新增依赖。
- 生产事故归因：本变更修复已确认的缓存竞态，不将“所有 0x0200 持续失败 11 天”的报告认定为已复现的必然结果。
