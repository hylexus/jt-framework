---
icon: code
---

# 批量位置上传报文解析示例

::: danger

- **808**文档中定义的位置上传报文有很多字段，**但是** 许多厂商实现的 **808协议** 位置上传报文 **只是原始808的一个子集** 。
- 本文举例用到的报文也 **不是标准完整的位置上传报文**，但是原理都一样。
- 此处以 **2019** 版的位置批量上传报文为例，展示如何基于注解来解析报文。

:::

## 解析报文到实体类

实体类映射关系如下：

@[code](@example-src/808/v2/annotation-driven-dev/LocationBatchUploadMsgV2019.java)

消息处理器如下：

```java{6,7}
@Slf4j
@Component
@Jt808RequestHandler
public class LocationMsgHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0704)
    public BuiltinServerCommonReplyMsg processLocationBatchUploadMsgV2019(Jt808RequestEntity<LocationBatchUploadMsgV2019> request) {
        log.info("LocationBatchUpload -- V2019 -- {}", request.body());

        return BuiltinServerCommonReplyMsg.success(request.msgId(), request.flowId());
    }
}
```

## 测试

测试报文如下

```shell
7E070400E401583860765500040003010049000000000004000301D9F190073CA3C1000C00000000211204082941010400D728AD3001003101092504000000001404000000041504000000001604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000211130171352010400D728AD3001003101092504000000001404000000041504000000001604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000211130171357010400D728AD300115310109250400000000140400000004150400000000160400000000170200011803000000407E
```

消息处理器截图如下：

<p class="demo">
    <img :src="$withBase('/img/v2/annotation-driven-dev/location-batch-upload-v2019-debug.png')">
</p>
