---
icon: code
---

# 位置上传报文解析示例

::: danger

- **808**文档中定义的位置上传报文有很多字段，**但是** 许多厂商实现的 **808协议** 位置上传报文 **只是原始808的一个子集** 。
- 本文举例用到的报文也 **不是标准完整的位置上传报文**，但是原理都一样。
- 此处以 **2019** 版的位置上传报文为例，展示如何基于注解来解析报文。

:::

## 解析报文到实体类

实体类映射关系如下：

@[code](@example-src/808/v2/annotation-driven-dev/LocationUploadMsgV2019.java)

消息处理器如下：

```java{6,7}
@Slf4j
@Component
@Jt808RequestHandler
public class LocationMsgHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0200, versions = VERSION_2019)
    public BuiltinServerCommonReplyMsg processLocationUploadMsgV2019(Jt808RequestEntity<LocationUploadMsgV2019> request) {
        log.info("LocationUpload -- V2019 -- {}", request.body());

        return BuiltinServerCommonReplyMsg.success(request.msgId(), request.flowId());
    }
}
```

## 测试

测试报文如下

```shell
7e02004086010000000001893094655200E4000000000000000101D907F2073D336C000000000000211124114808010400000026030200003001153101002504000000001404000000011504000000FA160400000000170200001803000000EA10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B017118000000000000000000000000000000000000000000000000567e
```

消息处理器截图如下：

<p class="demo">
    <img :src="$withBase('/img/v2/annotation-driven-dev/location-upload-v2019-debug.png')">
</p>
