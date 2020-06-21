# 1.0.5-SNAPSHOT

### ⭐ New Features

- `Jt808SessionManager` 重构，支持自定义

### 🐞 Bug Fixes

- SessionManager#removeBySessionIdAndClose 的bug

### 🔨 Others

- `CommandSender` 文档完善

# 1.0.4-SNAPSHOT

### ⭐ New Features

- `MsgHandler` 可选择性地交给Spring管理，以便于在 `MsgHandler` 中调用其他的由Spring管理的Bean

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/16
    - 默认读写空闲时间改为20分钟
    - 修复示例代码中消息消息的返回值类型 
- https://github.com/hylexus/jt-framework/issues/17
    - 边界情况下默认转义逻辑bug

### 🔨 Others

- 优化 `Session#currentFlowId`
- 新增 `SessionManager#findBySessionId`

# 1.0.3-SNAPSHOT

- `MsgHandler` 可选择性地交给Spring管理，以便于在 `MsgHandler` 中调用其他Bean
- bug-fix: #16,#17

## Bug Fixes

* **decoder:** 嵌套类型丢失数据 (#b1a8eaca, closes #2)

## 2020-02-09
### Other Changes
- docs(todo-list): README.md,CHANGELOG.md
- ExceptionHandler


## 2020-02-08
### Other Changes
- tmp


## 2020-02-06
### Other Changes
- docs


## 2020-02-05
### Other Changes
- CustomReflectionBasedRequestMsgBodyConverter


## 2020-02-03
### Other Changes
- docs


## 2020-02-02
### Other Changes
- docs
- docs
- OrderedComponent
- HandlerMethodArgumentResolver


## 2020-02-01
### Other Changes
- ReflectionBasedRequestMsgHandler
- maven-config


## 2020-01-31
### Other Changes
- README.md
- docs
- docs
- deploy docs
- deploy docs
- deploy docs
- docs


## 2020-01-30
### Other Changes
- tmp
- docs
- Jt808DefaultEnvironmentPostProcessor


## 2020-01-29
### Other Changes
- docs
- docs


## 2020-01-28
### Other Changes
- tmp
- docs
- BytesEncoder
- docs
- docs


## 2020-01-27
### Other Changes
- docs


## 2020-01-26
### Other Changes
- @EnableJt808ServerAutoConfig
- samples
- samples


## 2020-01-25
### Other Changes
- tmp


## 2019-10-28
### Other Changes
- tmp


## 2019-10-22
### Other Changes
- tmp


## 2019-10-21
### Other Changes
- tmp


## 2019-10-17
### Other Changes
- tmp


## 2019-10-16
### Other Changes
- tmp


## 2019-10-13
### Other Changes
- CommandWaitingPool


## 2019-10-09
### Other Changes
- tmp


## 2019-10-06
### Other Changes
- @SlicedFrom


## 2019-10-04
### Other Changes
- oaks-lib --> 1.0.4
- DataType
- @SliceFrom
- SplittableField


## 2019-10-03
### Other Changes
- 附加消息解析


## 2019-10-01
### Other Changes
- init vue-press
- rename 'jt-platform' to 'jt-framework'
- 扁平化解析附加消息


## 2019-09-29
### Other Changes
- 重构消息解析代码


## 2019-09-25
### Other Changes
- tmp


## 2019-09-22
### Other Changes
- RequestMsgHeaderAware
- tmp


## 2019-09-18
### Other Changes
- tmp


## 2019-09-17
### Other Changes
- delete LocalMsgQueue


## 2019-08-29
### Other Changes
- boot-auto-configure


## 2019-08-28
### Other Changes
- boot-auto-configure
- auto-configure
- component-scan


## 2019-08-27
### Other Changes
- auto config


## 2019-08-26
### Other Changes
- init spring-boot-starter


## 2019-08-25
### Other Changes
- tmp


## 2019-08-24
### Other Changes
- bug-fix
- sessionManager
- sessionManager
- eventBus


## 2019-08-22
### Other Changes
- MsgDispatcher


## 2019-08-21
### Other Changes
- migrate to gradle


## 2019-07-07
### Other Changes
- tmp


## 2019-07-06
### Other Changes
- init

