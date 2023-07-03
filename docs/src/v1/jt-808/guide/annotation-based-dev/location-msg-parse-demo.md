---
headerDepth: 3
---

# 位置上传报文解析示例

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-bare) 下找到对应代码。
:::

::: tip
本节将以位置上传报文为例，展示如何基于注解来解析报文。
:::

::: danger 注意
- 808文档中定义的位置上传报文有很多字段，**`但是`** 许多厂商实现的808协议位置上传报文 `只是原始808的一个子集` 。
- 本文举例用到的报文也 `不是标准完整的位置上传报文`，但是原理都一样。
- 请谅解本文用到的 `被具体厂商精简过的文档` 不便全部展示出来，但是关键部分会截图展示，不会影响到你阅读本示例。
:::

## 十六进制报文

::: warning 注意
- 这个报文格式可能和你使用的有所不同，请加以区分！！！
- 如何以 `十六进制格式` 发包请移步 [推荐调试工具](src/v1/jt-808/guide/FAQ/debug.md)
:::

```
7E0200004A76890100562600AD000000000000000201DCF7F6074054C1000000000000200128080934300164310100E10400000030E2020000E306005E019A019AE40B01CC000018A20000480264E5045E04019AE601A2637E
```

## 解析位置基本信息

<p class="demo">
    <img :src="$withBase('/img/doc-img/0102-001.png')" alt="精简版位置报文">
    <img :src="$withBase('/img/doc-img/0102-002.png')" alt="精简版位置报文">
</p>

```java
@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0200)
public class LocationUploadMsgBody implements RequestMsgBody {

    // 报警标志
    @BasicField(startIndex = 0, dataType = DWORD)
    private int alarmFlag;

    // 状态
    @BasicField(startIndex = 4, dataType = DWORD)
    private int status;

    // 将上面的 status 字段的第0位取出转为 int 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private int accIntStatus;

    // 将上面的 status 字段的第0位取出转为 boolean 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private Boolean accBooleanStatus;

    // 0 北纬;1 南纬
    // 将上面的 status 字段的第2位取出转为 int 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 2)
    private int latType;

    // 纬度(尚未除以 10^6)
    @BasicField(startIndex = 8, dataType = DWORD)
    private Integer intLat;
    // 纬度(使用转换器除以10^6转为Double类型)
    @BasicField(startIndex = 8, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lat;
    
    // 经度(尚未除以 10^6)
    @BasicField(startIndex = 12, dataType = DWORD)
    private Integer intLng;
    // 经度(使用转换器除以10^6转为Double类型)
    @BasicField(startIndex = 12, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lng;

    // 经度(startIndexMethod使用示例)
    @BasicField(startIndexMethod = "getLngStartIndex", dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lngByStartIndexMethod;

    public int getLngStartIndex() {
        log.info("消息体总长度:{}", this.requestMsgMetadata.getHeader().getMsgBodyLength());
        return 12;
    }

    // 高度
    @BasicField(startIndex = 16, dataType = WORD)
    private Integer height;

    // 速度
    @BasicField(startIndex = 18, dataType = WORD)
    private int speed;

    // 方向
    @BasicField(startIndex = 20, dataType = WORD)
    private Integer direction;
    
    // BCD 长度6字节
    // 时间 yyMMddHHmmss
    // 200128080934 '2020-01-28 08:09:34'
    @BasicField(startIndex = 22, dataType = BCD, length = 6)
    private String time;
}
```

通过以下截图，可能会对映射关系更清晰一些：

<p class="demo">
    <img :src="$withBase('/img/doc-img/location-msg-debug.png')">
</p>

## 关于status字段的解析

<p class="demo">
    <img :src="$withBase('/img/doc-img/0102-002.png')" alt="精简版位置报文">
</p>

status字段是由消息体中第4~7个字节表示的，类型为 `DWORD`。对应到Java中为 `无符号四字节整型` 。你可以将其映射为 `int` 或 `Integer` 。

所以至少有以下几种解析方式：

### 1.手动解析

```java
@BasicField(startIndex = 4, dataType = BYTES, length = 4)
private byte[] statusBytes;
```

代码段中的 `statusBytes` 就是消息体中第4~7个字节，然后你可以将字节数组手动转换到 `int` 。

```java
int status = IntBitOps.intFrom4Bytes(statusBytes);
```

之后，你可以根据文档中表17的定义将 `int` 中对应的 `bit` 提取出来。

```java
// status的第0位-->Acc开关 --> 0:关; 1:开
int accStatus = Numbers.getBitAt(status, 0);
// status的第3位 --> 0:东经; 1:西经
int lngType = Numbers.getBitAt(status, 3);
```

### 2.使用@SlicedFrom解析

```java
// 消息体中第4~7个字节 --> int
@BasicField(startIndex = 4, dataType = DWORD)
private int status;

// 将上面的 status 字段的第0位取出转为 int 类型
@SlicedFrom(sourceFieldName = "status", bitIndex = 0)
private int accIntStatus;

// 将上面的 status 字段的第0位取出转为 boolean 类型
@SlicedFrom(sourceFieldName = "status", bitIndex = 0)
private Boolean accBooleanStatus;

// 0 北纬;1 南纬
// 将上面的 status 字段的第2位取出转为 int 类型
@SlicedFrom(sourceFieldName = "status", bitIndex = 2)
private int latType;
```

### 3.使用@SplittableField解析

```java
@Jt808ReqMsgBody(msgType = 0x0200)
public class LocationUploadMsgBody implements RequestMsgBody {
    // 状态
    @BasicField(startIndex = 4, dataType = DWORD)
    // 将status字段拆分之后放入statusInfo字段
    // 该注解只能用户数字
    @SplittableField(splitPropertyValueIntoNestedBeanField = "statusInfo")
    private int status;

    private LocationUploadStatus statusInfo;

    @Data
    public static class LocationUploadStatus {
        @SplittableField.BitAt(bitIndex = 0)
        private boolean accStatus; // acc开?
        @SplittableField.BitAt(bitIndex = 1)
        private int bit1; //1:定位, 0:未定义
        @SplittableField.BitAt(bitIndex = 2)
        private Boolean isSouthLat;// 是否南纬?
        @SplittableField.BitAt(bitIndex = 3)
        private Integer lngType;
        // 将第0位和第1位同时取出并转为int
        // 在此处无实际意义,只是演示可以这么使用
        @SplittableField.BitAtRange(startIndex = 0, endIndex = 1)
        private int bit0to1;
    }
}
```

## 关于经纬度的解析

808文档中的经纬度定义为 `DWORD` 类型，以度位单位的纬度值 `乘以10的6次方` ，精确到百万分之一度

### 1. 解析为4字节的int

```java
// 纬度(尚未除以 10^6)
@BasicField(startIndex = 8, dataType = DWORD)
private Integer intLat;
```

本示例中结果为 `31258614`。

也就是说，接收到的字节数组中的表示经纬度的 `4个字节` 应该先转换为数字(int即可)，然后再 `除以10^6` 即为真实的经纬度，可以用 `Double` 表示。

### 2. 解析为double

::: tip
但是，本框架并不支持直接从 `byte[]` 到 `double` 的转换。此时可以使用自定义的类型转换器。
:::

- 自定义转换器

```java
public class LngLatReqMsgFieldConverter implements ReqMsgFieldConverter<Double> {
    @Override
    public Double convert(byte[] bytes, byte[] subSeq) {
        return IntBitOps.intFromBytes(subSeq, 0, subSeq.length) * 1.0 / 100_0000;
    }
}
```

- 然后指定 `customerDataTypeConverterClass` 即可

```java
// 纬度(使用转换器除以10^6转为Double类型)
@BasicField(startIndex = 8, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
private Double lat;
```

## 关于位置附加项的解析

<p class="demo">
    <img :src="$withBase('/img/doc-img/0102-003.png')" alt="精简版位置报文">
</p>

根据文档，从消息体的 `第28个字节开始` 就是附加项列表了。

还好附加项报文的格式也是有迹可循的：

- 整体是一个 `List` 结构，暂且将 `List` 的每一个元素称之为 `Item`
- 每个 `Item` 内部结构也是一致的
    - `Id (byte)`
    - `length (byte)`
    - `content (类型不固定)`
- 但是如果将这个附加项解析为一个 `List` 的话
    - 个人感觉取值不是很方便，另外如果附加项内部有嵌套的时候也不好处理
    - 所以额外提供了一个 `@ExtraField` 注解来映射为一个可嵌套的实体
    - 有得必有失，这样一来，有多少个附加项就要定义多少个字段，比较繁琐

### 使用@BasicField解析

由于附加项的类型不固定，仅仅用一个类是无法定义确切类型。
所以，此处的内容自动定义成了`byte[]`。

```java
@Data
public class ExtraInfoItem {
    @BasicField(startIndex = 0, dataType = BYTE)
    private Integer id;

    @BasicField(order = 1, startIndex = 1, dataType = BYTE)
    private Integer length;

    // 类型不固定 仅仅用一个类无法定义确切类型
    @BasicField(order = 2, startIndex = 2, dataType = BYTES, byteCountMethod = "getLength")
    private byte[] rawBytes;

}
```

```java
@BasicField(startIndex = 28,byteCountMethod = "getExtraInfoLength",dataType = LIST)
private List<ExtraInfoItem> extraInfoItemList;
```

### 使用@ExtraField解析

<p class="demo">
    <img :src="$withBase('/img/doc-img/0102-004.png')" alt="精简版位置报文">
</p>

```java
@Data
// 切记@ExtraMsgBody注解不能丢
@ExtraMsgBody(
        byteCountOfMsgId = 1, // 消息Id用1个字节表示
        byteCountOfContentLength = 1 // 附加项长度字段用1个字节表示
)
public class ExtraInfo {
    @ExtraField.NestedFieldMapping(msgId = 0x30, dataType = BYTE)
    private int field0x30;
    
    // 这里写成List仅仅为了示例，在msgId重复时可以使用List类型
    @ExtraField.NestedFieldMapping(msgId = 0x0001, dataType = LIST, itemDataType = DWORD)
    private List<Integer> field0x0001;

    @ExtraField.NestedFieldMapping(msgId = 0x31, dataType = BYTE)
    private int field0x31;

    @ExtraField.NestedFieldMapping(msgId = 0xe1, dataType = DWORD)
    private int field0xe1;

    @ExtraField.NestedFieldMapping(msgId = 0xE4, dataType = BYTES)
    private byte[] field0xe4;

    @ExtraField.NestedFieldMapping(msgId = 0xE5, dataType = DWORD)
    private int field0xe5;

    @ExtraField.NestedFieldMapping(msgId = 0xE6, dataType = BYTE)
    private byte field0xe6;
}
```

- `0xE2` 锁状态字段的说明

<p class="demo">
    <img :src="$withBase('/img/doc-img/0102-005.png')" alt="精简版位置报文">
</p>

::: tip 提示
示例文档中并没涉及到附加项嵌套的情况，但是这种情况在原始808文档中确实是存在的。

如果有嵌套的附加项，可以用类似如下的方式去嵌套解析：

```java
@ExtraField.NestedFieldMapping(msgId = xx, isNestedExtraField = true)
private SomeClass nestedField;
```
:::

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-bare) 下找到对应代码。
:::