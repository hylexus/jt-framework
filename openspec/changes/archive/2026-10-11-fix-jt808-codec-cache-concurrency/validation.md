## 验收结果

2026-10-09 完成本变更验证。生产代码仅修改位置附加项配置缓存，以及注解编码器、解码器的自定义转换器实例缓存。注解、公开接口、反射工具、长度提取器、内置消息定义和依赖保持不变。

| 验证环境 | 范围 | 结果 |
|---|---|---|
| 原实现 `2f882f5b`，Java 21 | 修改前新增的串行兼容基线 | 16/16 通过 |
| 修复实现，Java 21 | 三个新增测试类 | 27/27 通过 |
| 修复实现，Java 21 | `jt-808-server-support` 全模块 | 98/98 通过 |
| 修复实现，Zulu Java 8.0.482 | 支持模块全模块 | 98/98 通过 |
| 修复实现，Zulu Java 8.0.482 | Spring Boot 2.7.18 自动配置 / Boot 2 starter | 9/9、2/2 通过 |
| 修复实现，Temurin Java 17.0.18 | 支持模块全模块 | 98/98 通过 |
| 修复实现，Temurin Java 17.0.18 | Spring Boot 3.3.0 依赖环境中的新增缓存回归 / Boot 3 starter | 27/27、2/2 通过 |
| 独立编译的原解析器，Java 17 | 显式映射初始化窗口、默认项初始化窗口、准备失败后的半成品、空映射 | 4/4 检出原缺陷，按预期失败 |

成功测试均为零失败、零错误、零跳过。Java 8/17 是实际测试 JVM；Gradle 构建进程运行在 Java 21。支持模块沿用 `--release 8`，三个修改类的编译产物主版本号均为 52。

## 规格覆盖

测试类位于 `jt-808-server-support/src/test/java/io/github/hylexus/jt/jt808/support/`：

- `data/deserialize/extension/ExtendedJt808FieldDeserializerLocationExtraItemTest`：13 个串行兼容/默认映射测试，以下简写为 `LocationTest`。
- `data/deserialize/extension/ExtendedJt808FieldDeserializerLocationExtraItemConcurrencyTest`：8 个并发/资源测试，以下简写为 `ConcurrencyTest`。
- `annotation/codec/Jt808ConverterCacheTest`：6 个转换器选择/生命周期/发布测试，以下简写为 `ConverterTest`。

| 规格要求 | 验收证据 |
|---|---|
| Backward-compatible annotation and API contracts | 8 个相关类型的 `javap -public -s` 签名与修改前一致；注解及转换器接口源码逐字节一致；`LocationTest.lastDuplicateDeclarationWinsWithoutAdditionalKeyValidation`；`ConverterTest` 的两项反射构造失败和重试测试 |
| Complete field configuration under concurrent access | `ConcurrencyTest.concurrentFirstAndLaterRequestsKeepTypesDefaultsAndClassIsolation`；暂停后续显式项与暂停默认描述符的两项测试；`failedPreparationIsNotPublishedAndAWaitingRequestCanRetry` |
| Preserve mapping selection semantics | `LocationTest.preservesDeclaredTypesAndAnnotationDefault`、`preservesExplicitByteArrayDefaultAndFieldIsolation`、`lastDuplicateDeclarationWinsWithoutAdditionalKeyValidation`、`emptyMappingsUseImplicitAndExplicitDefaults` |
| Isolate configuration by message field | `LocationTest` 的跨消息类/同消息双字段场景；`ConcurrencyTest` 的跨类并发和 `concurrentFieldsInTheSameMessageRemainIsolated` |
| Preserve result ordering and caller ownership | `LocationTest.preservesWireOrderDuplicateValuesAndMutableIndependentResults` |
| Preserve field order conditions and length semantics | `LocationTest` 的固定长度与顺序、条件跳过、表达式优先级、长度方法、动态长度、空输入和零长度测试；`ConcurrencyTest.noItemsDoNotRequirePreparingMetadata` |
| Safely publish and reuse custom converter instances | `ConverterTest` 的串行复用、不同编解码实例隔离、暂停构造期间的并发解析/编码、异常包装与重试；`LocationTest` 的基础注册转换器与嵌套对象/当前请求上下文测试 |
| Preserve buffer lifecycle and request isolation | 所有测试输入由当前请求独立创建和释放；`ConcurrencyTest` 的基础转换器、嵌套对象成功及失败路径验证临时内容引用计数归零；读取位置和调用方输入引用计数保持原行为 |
| Preserve supported platform compatibility | Java 8 与 Java 17 实际运行矩阵；Boot 2/3 验证；Java 8 编译/API 目标及字节码版本验证 |

并发任务采用启动屏障、每请求独立缓冲区、有时限的 `Future.get()` 和线程池清理，工作线程中的断言会传播至测试结果。受控窗口测试暂停测试侧描述符的读取，不修改生产公开 API 或加入测试钩子。

## 实际验证命令

针对性测试与全模块测试：

```shell
./gradlew :jt-808-server-support:test \
  --tests '*ExtendedJt808FieldDeserializerLocationExtraItem*Test' \
  --tests '*Jt808ConverterCacheTest' --console=plain

./gradlew :jt-808-server-support:test --console=plain
```

运行环境验证使用临时 `/tmp/jt103-runtime.gradle`，未修改项目构建配置。以下为可重建的运行矩阵部分；仅将测试 JVM 切换为指定路径，并在 Boot 3 测试中加载同一组编译后的缓存回归类：

```groovy
allprojects {
    tasks.withType(Test).configureEach {
        def testJava = System.getProperty('jt103.testJava')
        if (testJava != null) {
            executable = testJava
            doFirst {
                systemProperties.keySet().findAll {
                    it.startsWith('java.') || it.startsWith('sun.') || it.startsWith('jdk.')
                }.each { systemProperties.remove(it) }
            }
        }
    }
}

gradle.projectsEvaluated {
    def support = rootProject.findProject(':jt-808-server-support')
    if (support == null) {
        return
    }
    if (System.getProperty('jt103.includeCodecTests') == 'true') {
        def starter = rootProject.project(':jt-808-server-spring-boot-starter')
        starter.tasks.named('test', Test) {
            dependsOn support.tasks.named('testClasses')
            testClassesDirs += support.sourceSets.test.output.classesDirs
            classpath += support.sourceSets.test.output
            includes = [
                '**/Jt808TaskSchedulerIsolationTest.class',
                '**/ExtendedJt808FieldDeserializerLocationExtraItemTest.class',
                '**/ExtendedJt808FieldDeserializerLocationExtraItemConcurrencyTest.class',
                '**/Jt808ConverterCacheTest.class'
            ]
            filter {
                includeTestsMatching '*Jt808TaskSchedulerIsolationTest'
                includeTestsMatching '*ExtendedJt808FieldDeserializerLocationExtraItemTest'
                includeTestsMatching '*ExtendedJt808FieldDeserializerLocationExtraItemConcurrencyTest'
                includeTestsMatching '*Jt808ConverterCacheTest'
            }
        }
    }
}
```

支持模块现有构建会将构建 JVM 的系统属性转发给测试 JVM，因此临时脚本删除转发的运行时身份属性，让测试 JVM 使用自身的 `java.home` 等属性。此调整只服务于验证，未更改仓库的构建行为。

```shell
./gradlew -I /tmp/jt103-runtime.gradle \
  -Djt103.testJava=/Users/hylexus/.local/share/mise/installs/java/zulu-8/bin/java \
  :jt-808-server-support:test \
  :jt-808-server-spring-boot-autoconfigure:test \
  :jt-808-server-spring-boot-starter-boot2:test --console=plain

./gradlew -I /tmp/jt103-runtime.gradle \
  -Djt103.testJava=/Users/hylexus/.local/share/mise/installs/java/temurin-17/bin/java \
  -Djt103.includeCodecTests=true \
  :jt-808-server-support:test \
  :jt-808-server-spring-boot-starter:test --console=plain
```

## 原实现对照

从 `2f882f5b` 读取原位置附加项解析器源码，用 Java 8 目标独立编译到 `/tmp/jt103-original-classes`，再通过临时 Gradle 初始化脚本将该目录置于测试 classpath 首位。工作区源文件及正式编译目录始终保持修复版本，原实现测试报告输出到 `/tmp/jt103-original-results`。

以下四项均检出原缺陷：

- `waitsForAnExplicitMappingThatIsStillBeingPrepared`：原实现提前返回，未等待完整映射。
- `waitsForTheDeclaredDefaultThatIsStillBeingPrepared`：原实现提前返回，未等待默认描述符。
- `failedPreparationIsNotPublishedAndAWaitingRequestCanRetry`：原实现提前使用半成品缓存。
- `emptyMappingsUseImplicitAndExplicitDefaults`：原实现因内层 map 未创建发生空指针。

修复版本中相同测试均通过；完成对照后恢复正式测试 classpath 并重新运行 Java 17 全模块测试。

## 范围与结论

源码和签名比对确认没有注解/公开接口/依赖变更，没有新增 key 范围或重复 key 校验。转换器的同步构造、反射失败包装与实例生命周期保持原有方式；附加项解析循环、长度提取和缓冲区释放代码未调整。

本次验证确认并修复半成品发布、默认映射初始化窗口、空映射以及转换器实例缓存读写安全问题。原解析器对照没有依赖随机触发 `HashMap` 结构损坏，也不能据此认定 issue 所述“生产实例持续 11 天全部 0x0200 失败”的完整事故因果链已复现。

```shell
openspec validate fix-jt808-codec-cache-concurrency --strict
```

结果：规格严格校验通过。
