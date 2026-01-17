---
icon: launch
---

# 快速开始

## 引入依赖

### spring-boot-2.x

::: code-tabs#gradle

@tab maven

```xml
<!--  -->
<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-xtream-codec-adapter-spring-boot-starter-boot2</artifactId>
    <version>3.0.0-rc.1</version>
</dependency>
```

@tab:active gradle

```groovy
implementation 'io.github.hylexus.jt:jt-808-server-xtream-codec-adapter-spring-boot-starter-boot2:3.0.0-rc.1'
```

:::

### spring-boot-3.x

::: code-tabs#gradle

@tab maven

```xml
<!--  -->
<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-xtream-codec-adapter-spring-boot-starter</artifactId>
    <version>3.0.0-rc.1</version>
</dependency>
```

@tab:active gradle

```groovy
implementation 'io.github.hylexus.jt:jt-808-server-xtream-codec-adapter-spring-boot-starter:3.0.0-rc.1'
```

:::

## 配置

[参考这里](/v2/jt-808/config/xtream-codec.md)

## 编写实体类

::: tip 提示

主要区别在于 `drivenBy()` 属性：

- 使用 **xtream-codec** 编解码库
    - `@Jt808ResponseBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC), msgId = 0x8100)`
    - `@Jt808RequestBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC))`

- 使用 **jt-framework 默认的编解码库**
    - `@Jt808RequestBody()`
    - `@Jt808ResponseBody(msgId = 0x8100)`
    - `@Jt808RequestBody(drivenBy = @DrivenBy(DrivenBy.Type.DEFAULT))`
    - `@Jt808ResponseBody(drivenBy = @DrivenBy(DrivenBy.Type.DEFAULT), msgId = 0x8100)`

:::

::: details 点击查看 `0x0100` 消息定义

```java {6-7}

import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.xtream.codec.core.type.Preset;

// drivenBy 属性 指定了该类基于 xtream-codec-core 提供的 注解 和 编解码器
@Jt808RequestBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC))
public class XtreamMessage0100AllInOne {
    @Preset.JtStyle.Word(desc = "省域 ID")
    private int provinceId;

    @Preset.JtStyle.Word(desc = "市域 ID")
    private int cityId;

    @Preset.JtStyle.Bytes(version = {2011, 2013}, length = 5, desc = "制造商ID(2011 || 2013)")
    @Preset.JtStyle.Bytes(version = 2019, length = 11, desc = "制造商ID(2019)")
    private String manufacturerId;

    @Preset.JtStyle.Bytes(version = 2011, length = 8, desc = "终端型号(2011)")
    @Preset.JtStyle.Bytes(version = 2013, length = 20, desc = "终端型号(2013)")
    @Preset.JtStyle.Bytes(version = 2019, length = 30, desc = "终端型号(2019)")
    private String terminalType;

    @Preset.JtStyle.Bytes(version = {2011, 2013}, length = 7, desc = "终端ID(2011 || 2013)")
    @Preset.JtStyle.Bytes(version = 2019, length = 30, desc = "终端ID(2019)")
    private String terminalId;

    @Preset.JtStyle.Byte(desc = "车牌颜色")
    private short color;

    // 这里不写 length 表示读取后续所有字节
    @Preset.JtStyle.Str(desc = "车辆标识")
    private String carIdentifier;

    // getters, setters
}
```

:::

::: details 点击查看 `0x8100` 消息定义

```java {6-7}

import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.core.type.Preset;

// drivenBy 属性 指定了该类基于 xtream-codec-core 提供的 注解 和 编解码器
@Jt808ResponseBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC), msgId = 0x8100)
public class XtreamMessage8100 {
    // 1. byte[0,2) WORD 对应的终端注册消息的流水号
    @Preset.JtStyle.Word(desc = "对应的终端注册消息的流水号")
    private int clientFlowId;

    // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
    @Preset.JtStyle.Byte(desc = "注册结果")
    private short result;

    // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
    @Preset.JtStyle.Str(condition = "result == 0", desc = "鉴权码")
    private String authCode;

    // getters, setters
}
```

:::

::: info 提示

更多注解用法参考:
[io.github.hylexus.xtream.codec.ext.jt808.builtin.messages](https://github.com/hylexus/xtream-codec/tree/develop/ext/jt/jt-808-server-spring-boot-starter-reactive/src/main/java/io/github/hylexus/xtream/codec/ext/jt808/builtin/messages/request)

:::

## 编写处理器

::: tip 提示

处理器写法和之前一样，只是 请求的解码方式 和 响应的编码方式 不一样

:::

```java
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.*;

@Component
@Jt808RequestHandler
public class XtreamCodecSampleHandler {
    private static final Logger log = LoggerFactory.getLogger(XtreamCodecSampleHandler.class);

    // 7e0100405601000000000139123443290000000b0002696431323334353637383974797065313233343536373839303132333435363738393031323334353669643132333435363738393031323334353637383930313233343536373801b8ca4a2d313233343539517e
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public XtreamMessage8100 clientRegisterV2019(Jt808RequestEntity<XtreamMessage0100AllInOne> request) {
        final XtreamMessage0100AllInOne body = request.body();
        log.info("client register v2019 : {}", body);
        return new XtreamMessage8100().setClientFlowId(request.flowId()).setResult((byte) 0).setAuthCode("AuthCode2019----");
    }

    // 7e0100002f0139123443230000000b0002696433323174797065313233343536373839303132333435366964313233343501b8ca4a2d3132333435395a7e
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2013)
    public XtreamMessage8100 clientRegisterV2013(Jt808Request request, XtreamMessage0100AllInOne body) {
        log.info("client register v2013 : {}", body);
        return new XtreamMessage8100().setClientFlowId(request.flowId()).setResult((byte) 0).setAuthCode("AuthCode2013----");
    }
}

```
