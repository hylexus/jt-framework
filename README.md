# jt-framework

Jt-808协议服务端。

文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)

## 项目结构

```sh
.
├── build-script    # gradle用到的构建脚本和checkstyle配置
├── docs            # 文档 (vue-press)
├── gradle
├── jt-808-server
├── jt-808-server-spring-boot-stater
├── jt-808-server-support
├── jt-core
├── jt-spring
└── samples         # 示例项目
    ├── jt-808-server-sample-bare           # 几乎零配置的示例
    ├── jt-808-server-sample-annotation     # 注解相关的示例
    └── jt-808-server-sample-customized     # 定制化示例
```

## package

```shell script
./gradlew clean build
```

## TODO

[jt-framework-gist](https://gist.github.com/hylexus/fbd2101575ab1c35bc4f102871da84ca)

- `基于注解` 的请求消息体转换器
   - [ ] 代码重构
- `基于注解` 的响应消息体转换器
   - [ ] 封装通用的响应消息体
   - [x] `服务端通用应答消息` 的注解实现
- 异常处理
    - 处理机制并不完善
        - [x] 处理 `消息转换为实体类` 之后的异常
        - [ ] 如有必要，再处理消息转换之前的异常
    - [ ] 代码重构


文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
