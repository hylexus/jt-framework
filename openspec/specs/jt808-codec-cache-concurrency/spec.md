# jt808-codec-cache-concurrency Specification

## Purpose

保证 JT/T 808 注解编解码在共享实例并发首次使用及后续使用时，依据完整的字段配置和已完成构造的转换器处理报文。缓存修复必须保持既有公开接口、注解语义、成功串行解析结果和支持环境兼容，用户无需修改消息类、转换器或配置。

## Requirements

### Requirement: Backward-compatible annotation and API contracts

系统 SHALL 保持既有注解及公开 API 的源码和二进制兼容性，包括 `LocationMsgExtraItemMapping`、`KeyValueMapping`、`ValueDescriptor` 的成员、默认值、元注解和别名，以及编解码入口和自定义转换器接口。系统 SHALL NOT 因缓存修复新增注解配置校验、修改既有非竞态错误的异常类型或包装方式、引入迁移要求或新增依赖。

#### Scenario: Existing application upgrades without migration

- **WHEN** 已使用位置附加项注解和自定义转换器的应用替换修复后的框架
- **THEN** 原有消息类、配置和转换器继续使用原有接口
- **AND** 无需增加初始化调用、启动预热或修改注解才能获得并发安全行为

#### Scenario: Previously accepted mapping declarations remain accepted

- **WHEN** 注解包含重复 key，或包含未出现在报文中且值为 `0x100` 的 key 声明
- **THEN** 系统继续按既有映射选择规则处理可读取的附加项
- **AND** 不因本次修复增加重复 key 或 key 范围校验错误

#### Scenario: Existing converter construction error remains compatible

- **WHEN** 自定义转换器通过既有反射构造路径创建失败
- **THEN** 调用方获得与修复前相同类型及包装方式的构造异常
- **AND** 系统不改为静默选择其他转换器、增加异步包装或返回错误实例

### Requirement: Complete field configuration under concurrent access

系统 SHALL 在多个请求共享编解码实例并首次解析同一位置附加项字段时，仅使用该字段完整且一致的 key 映射与默认描述符。并发请求 SHALL 获得与既有完整初始化后成功串行解析一致的类型和值，SHALL NOT 因初始化尚未完成而选择兜底类型、使用缺失映射或依赖半成品状态。初始化失败 SHALL NOT 使其他请求使用部分构建的配置。

#### Scenario: Concurrent first location reports

- **WHEN** 多个请求并发首次解析配置了 `0x01 -> DWORD/Long`、`0x02 -> WORD/Integer`、`0x31 -> BYTE/Integer` 的同一字段
- **THEN** 每个请求的相应附加项均具有所配置的 Java 类型和正确值
- **AND** 初始化竞态不会使上述已配置项变成 `byte[]` 或 `ByteArrayContainer`

#### Scenario: Lookup while field configuration is still being prepared

- **WHEN** 一个请求正在准备字段配置，另一个请求解析其中尚未准备完成的 key
- **THEN** 第二个请求依据完整配置解析该 key
- **AND** 不将准备中的缺失项当成未知 key

#### Scenario: Unknown key during first initialization

- **WHEN** 并发首次访问期间的报文包含未配置 key，而该字段的默认描述符尚在准备中
- **THEN** 该附加项使用注解最终指定的默认描述符
- **AND** 不临时改用其他默认类型

#### Scenario: Later reports retain correct behavior

- **WHEN** 字段配置已经成功准备完成，后续多个请求并发解析相同字段
- **THEN** 已配置项和未知项继续按相同配置返回正确类型和值
- **AND** 后续解析不会修改其他请求正在使用的配置

#### Scenario: Failed preparation does not expose partial configuration

- **WHEN** 字段配置的准备在完成前失败，同时另一个请求尝试解析该字段
- **THEN** 其他请求不能依赖本次失败留下的部分映射返回解析结果
- **AND** 后续准备能够成功时使用完整配置，不能沿用此前的半成品

### Requirement: Preserve mapping selection semantics

系统 SHALL 对已配置 key 使用其 `ValueDescriptor.source()` 和 `target()`，对未配置 key 使用 `defaultKeyValueMapping()`。未显式指定默认描述符时 SHALL 保持 `BYTES/ByteArrayContainer`；显式默认描述符 SHALL 按声明使用。重复 key 的声明 SHALL 保持注解数组中最后一项覆盖前面项的行为。空 `keyValueMappings` SHALL 表示不存在显式映射，所有实际附加项按默认描述符解析。

#### Scenario: Explicit default byte array

- **WHEN** 字段配置了非空映射列表及 `defaultKeyValueMapping = BYTES/byte[]`，报文包含未知 key
- **THEN** 未知项返回 `byte[]`
- **AND** 内容与报文中的原始值字节一致

#### Scenario: Annotation default remains a byte container

- **WHEN** 字段没有显式指定默认描述符，报文包含未知 key
- **THEN** 未知项返回 `ByteArrayContainer`
- **AND** 不将注解默认类型改成 `byte[]`

#### Scenario: Last duplicate declaration wins

- **WHEN** 同一注解数组先声明 `0x31 -> BYTE/Short`，后声明 `0x31 -> BYTE/Integer`
- **THEN** `0x31` 返回 `Integer`
- **AND** 串行解析和并发首次解析均遵守相同覆盖顺序

#### Scenario: Empty mappings use the declared default

- **WHEN** 字段配置 `keyValueMappings = {}`，报文包含一个或多个附加项
- **THEN** 所有项按显式默认描述符或注解默认描述符解析
- **AND** 不因映射列表为空发生缓存相关的空指针异常

### Requirement: Isolate configuration by message field

系统 SHALL 按实际消息字段区分位置附加项配置。不同消息类或同一消息类中的不同字段 SHALL 能为相同 key 声明不同的显式映射与默认描述符，且串行或并发访问 SHALL NOT 使这些配置相互覆盖。

#### Scenario: Same key in different message classes

- **WHEN** 两个消息类的同名字段分别将 `0x01` 配置为 `DWORD/Long` 和 `BYTES/byte[]`，并共享解码器并发解析
- **THEN** 每个字段均返回自身声明的类型和值
- **AND** 未知 key 使用各自字段的默认描述符

#### Scenario: Different annotated fields in one message

- **WHEN** 同一消息类中的两个位置附加项字段为同一 key 使用不同描述符
- **THEN** 每个字段依其自身注解解析
- **AND** 初始化任一字段不会改变另一个字段的解析结果

### Requirement: Preserve result ordering and caller ownership

系统 SHALL 保持位置附加项结果的既有映射行为：key 按报文中的首次出现顺序迭代，同一报文中重复 key 的后一次值覆盖前一次值且不改变该 key 的首次插入位置。系统 SHALL 保持结果映射可由调用方修改，并为每次解析生成独立结果。

#### Scenario: Duplicate keys in the wire payload

- **WHEN** 附加项按 `0x02`、`0x01`、`0x02` 的顺序出现，两个 `0x02` 具有不同的值
- **THEN** 结果迭代顺序为 `0x02`、`0x01`
- **AND** `0x02` 的值来自最后一个对应附加项

#### Scenario: Caller modifies one decoded result

- **WHEN** 调用方增加、替换或删除一次解析结果中的项
- **THEN** 修改继续被允许
- **AND** 不影响配置或其他请求的结果

### Requirement: Preserve field order conditions and length semantics

系统 SHALL 保持 `order`、`conditionalOn`、`length`、`lengthExpression`、`lengthMethod`、`desc` 的既有默认值与别名行为，继续按既有字段顺序、条件求值、长度选择优先级和读取边界执行解析。请求相关条件和动态长度 SHALL 针对当前消息求值，不得变成其他请求共享的字段配置。缓存修复 SHALL NOT 改变零长度、无附加项或被条件跳过时的既有读取行为。

#### Scenario: Conditional field is skipped

- **WHEN** 当前消息的 `conditionalOn` 求值结果使位置附加项字段被跳过
- **THEN** 该字段不执行解析且不消耗字段字节
- **AND** 后续字段保持既有顺序和读取位置

#### Scenario: Existing length selection precedence

- **WHEN** 位置附加项字段同时声明正数 `length`、非空 `lengthExpression` 和非空 `lengthMethod`
- **THEN** 继续使用正数 `length`
- **AND** 未指定正数 `length` 时继续优先使用非空表达式，否则使用长度方法

#### Scenario: Dynamic lengths remain request-specific

- **WHEN** 不同消息实例通过表达式或长度方法为同一字段计算出不同的正数长度
- **THEN** 每条消息只按自身计算的范围读取该字段
- **AND** 读取范围不受其他请求影响，后续字段仍可正确解析

#### Scenario: No readable extra items

- **WHEN** 字段执行解析时对应输入没有可读的附加项字节
- **THEN** 返回独立的空结果映射
- **AND** 不因本次修复新增必须预先初始化映射的条件

#### Scenario: Zero resolved length retains existing behavior

- **WHEN** 字段通过既有入口获得零长度，且当前输入仍有可读的附加项
- **THEN** 继续按既有行为解析当前输入中剩余的可读附加项
- **AND** 不在缓存修复中重新定义零长度为截断、跳过或强制读取某个范围

### Requirement: Safely publish and reuse custom converter instances

系统 SHALL 在共享注解编解码实例并发首次使用同一自定义转换器类时，仅向请求暴露已成功完成构造的实例。每个编解码实例 SHALL 保持按转换器类复用实例的既有生命周期，不改为按请求、线程或字段创建实例。系统 SHALL 保持自定义转换器优先、基础转换器选择和嵌套对象处理的既有规则；用户自定义转换器自身的线程安全责任保持不变。

#### Scenario: Concurrent first custom deserializer use

- **WHEN** 一个解码器的多个请求首次并发使用同一自定义字段解析器类
- **THEN** 成功调用使用同一个已完成构造的实例
- **AND** 实例完成构造前的状态不会被请求使用

#### Scenario: Concurrent first custom serializer use

- **WHEN** 一个编码器的多个请求首次并发使用同一自定义字段序列化器类
- **THEN** 成功调用使用同一个已完成构造的实例
- **AND** 每个请求生成与对应串行编码相同的字节

#### Scenario: Existing custom and basic converter precedence

- **WHEN** 字段指定自定义转换器，或附加项描述符能匹配已注册的基础转换器
- **THEN** 字段继续优先使用指定的自定义转换器，附加项继续使用匹配的基础转换器
- **AND** 不因缓存调整忽略现有注册或改选另一条转换路径

#### Scenario: Nested object extra item

- **WHEN** 附加项描述符指定嵌套对象，且基础转换器没有对应匹配
- **THEN** 继续按该目标类的注解进行嵌套解码
- **AND** 对象内容及当前请求上下文与既有串行行为一致

#### Scenario: Different codec instances retain separate lifecycles

- **WHEN** 两个独立编解码实例使用同一自定义转换器类
- **THEN** 各实例继续管理自身的转换器实例
- **AND** 不将转换器改为跨编解码实例共享的全局实例

### Requirement: Preserve buffer lifecycle and request isolation

系统 SHALL 保持编解码入口的缓冲区所有权和引用计数契约，保留附加项临时内容在成功或失败路径上的既有释放行为。共享配置 SHALL NOT 持有请求、报文缓冲区或请求上下文，也不得将某个请求的解析结果用于后续请求。

#### Scenario: Successful extra item parsing

- **WHEN** 请求完成附加项解析
- **THEN** 输入缓冲区的读取位置和调用方释放责任与既有行为一致
- **AND** 附加项临时内容按原有生命周期释放

#### Scenario: Extra item conversion fails

- **WHEN** 已读取的附加项内容在类型转换或嵌套解码过程中抛出异常
- **THEN** 附加项临时内容仍按既有路径释放
- **AND** 不通过缓存保留失败请求或改变该异常的既有传播方式

### Requirement: Preserve supported platform compatibility

系统 SHALL 保持共享支持模块的 Java 8 最低兼容目标，以及现有 Spring Boot 2 和 Spring Boot 3 使用方式。修复 SHALL NOT 要求升级 JDK、切换 starter、引入新的库或增加框架配置。

#### Scenario: Minimum Java compatibility

- **WHEN** 使用支持模块既有的 Java 8 编译目标构建修复
- **THEN** 不引用高于 Java 8 的语言特性或标准库 API
- **AND** 最低支持运行环境不因本次修复提高

#### Scenario: Reported Java 17 environment and supported starters

- **WHEN** 应用在 Java 17 或现有受支持的 Spring Boot 2、Spring Boot 3 环境使用注解编解码
- **THEN** 获得相同的并发安全与注解兼容行为
- **AND** 不需要调整既有配置或消息定义
