---
icon: map
---

# 数据类型转换(注解别名,v2.1.1)

## 相关知识

- [注解别名](./annotation-alias.md)
- [数据类型转换(传统方式)](./data-type-mapping.md)

## 请先读我

相比于上一个章节 [数据类型转换(传统方式)](./data-type-mapping.md) 来说，本章节介绍的通过 [注解别名](./annotation-alias.md) 数据转换方式要更简单一些。

同时提供了 `@RequestFieldAlias.GeoPoint` (经纬度) 和 `@RequestFieldAlias.BcdDateTime` (时间格式的 `BCD`) 注解。

```java

@Data
@Accessors(chain = true)
//@Jt808ReqMsgBody(msgType = 0x0200, version = Jt808ProtocolVersion.VERSION_2019)
public class LocationUploadReqMsgV2019AliasTest {

    // ...
    @RequestFieldAlias.GeoPoint(order = 2)
    // 地理位置(经纬度)支持: long/Long, double/Double, BigDecimal
    private Double intLat;

    // (4). 经度(尚未除以 10^6)
    // @RequestField(order = 3, startIndex = 12, dataType = DWORD)
    @RequestFieldAlias.Dword(order = 3)
    private Long intLng;

    // ...

    // (8). 时间
    // 1. 解析为 String
    // @RequestField(order = 7, startIndex = 22, dataType = BCD, length = 6)
    // @RequestFields.BCD(order = 7, startIndex = 22, length = 6)
    // private String time;

    // 2. 解析为 LocalDateTime
    @RequestFieldAlias.BcdDateTime(order = 7)
    private LocalDateTime time;

    // ...
}
```

## BYTE

### 反序列化BYTE

| 目标类型                 | 注解                          | 备注             |
|----------------------|-----------------------------|----------------|
| `byte, Byte`         | `@RequestFieldAlias.Byte()` | 有溢出的风险         |
| `short, Short`       | `@RequestFieldAlias.Byte()` |                |
| `int, Integer`       | `@RequestFieldAlias.Byte()` |                |
| `long, Long`         | `@RequestFieldAlias.Byte()` |                |
| `ByteArrayContainer` | `@RequestFieldAlias.Byte()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestFieldAlias.Byte()` | **2.1.1** 开始支持 |
| `BitOperator`        | `@RequestFieldAlias.Byte()` | **2.1.1** 开始支持 |

### 序列化BYTE

| 源类型                  | 注解                           | 备注             |
|----------------------|------------------------------|----------------|
| `byte, Byte`         | `@ResponseFieldAlias.Byte()` |                |
| `short, Short`       | `@ResponseFieldAlias.Byte()` |                |
| `int, Integer`       | `@ResponseFieldAlias.Byte()` |                |
| `long, Long`         | `@ResponseFieldAlias.Byte()` |                |
| `ByteArrayContainer` | `@ResponseFieldAlias.Byte()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseFieldAlias.Byte()` | **2.1.1** 开始支持 |
| `BitOperator`        | `@ResponseFieldAlias.Byte()` | **2.1.1** 开始支持 |

## BYTES

### 反序列化BYTES

| 目标类型                 | 注解                                       | 备注             |
|----------------------|------------------------------------------|----------------|
| `byte[]`             | `@RequestFieldAlias.Bytes(length = xxx)` |                |
| `ByteBuf`            | `@RequestFieldAlias.Bytes(length = xxx)` |                |
| `String`             | `@RequestFieldAlias.Bytes(length = xxx)` |                |
| `ByteArrayContainer` | `@RequestFieldAlias.Bytes(length = xxx)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestFieldAlias.Bytes(length = xxx)` | **2.1.1** 开始支持 |

### 序列化BYTES

| 源类型                  | 注解                            | 备注             |
|----------------------|-------------------------------|----------------|
| `byte[]`             | `@ResponseFieldAlias.Bytes()` |                |
| `ByteBuf`            | `@ResponseFieldAlias.Bytes()` |                |
| `String`             | `@ResponseFieldAlias.Bytes()` |                |
| `ByteArrayContainer` | `@ResponseFieldAlias.Bytes()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseFieldAlias.Bytes()` | **2.1.1** 开始支持 |

## WORD

### 反序列化WORD

| 目标类型                 | 注解                          | 备注             |
|----------------------|-----------------------------|----------------|
| `short, Short`       | `@RequestFieldAlias.Word()` | 有溢出的风险         |
| `int, Integer`       | `@RequestFieldAlias.Word()` |                |
| `long, Long`         | `@RequestFieldAlias.Word()` |                |
| `ByteArrayContainer` | `@RequestFieldAlias.Word()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestFieldAlias.Word()` | **2.1.1** 开始支持 |
| `BitOperator`        | `@RequestFieldAlias.Word()` | **2.1.1** 开始支持 |

### 序列化WORD

| 源类型                  | 注解                           | 备注             |
|----------------------|------------------------------|----------------|
| `short, Short`       | `@ResponseFieldAlias.Word()` |                |
| `int, Integer`       | `@ResponseFieldAlias.Word()` |                |
| `long, Long`         | `@ResponseFieldAlias.Word()` |                |
| `ByteArrayContainer` | `@ResponseFieldAlias.Word()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseFieldAlias.Word()` | **2.1.1** 开始支持 |
| `BitOperator`        | `@ResponseFieldAlias.Word()` | **2.1.1** 开始支持 |

## DWORD

### 反序列化DWORD

| 目标类型                 | 注解                           | 备注             |
|----------------------|------------------------------|----------------|
| `int, Integer`       | `@RequestFieldAlias.Dword()` | 有溢出的风险         |
| `long, Long`         | `@RequestFieldAlias.Dword()` |                |
| `ByteArrayContainer` | `@RequestFieldAlias.Dword()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestFieldAlias.Dword()` | **2.1.1** 开始支持 |
| `BitOperator`        | `@RequestFieldAlias.Dword()` | **2.1.1** 开始支持 |

### 序列化DWORD

| 源类型                  | 注解                            | 备注             |
|----------------------|-------------------------------|----------------|
| `int, Integer`       | `@ResponseFieldAlias.Dword()` |                |
| `long, Long`         | `@ResponseFieldAlias.Dword()` |                |
| `ByteArrayContainer` | `@ResponseFieldAlias.Dword()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseFieldAlias.Dword()` | **2.1.1** 开始支持 |
| `BitOperator`        | `@ResponseFieldAlias.Dword()` | **2.1.1** 开始支持 |

## BCD

### 反序列化BCD

| 目标类型                 | 注解                                     | 备注             |
|----------------------|----------------------------------------|----------------|
| `String`             | `@RequestFieldAlias.Bcd(length = xxx)` |                |
| `ByteArrayContainer` | `@RequestFieldAlias.Bcd(length = xxx)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestFieldAlias.Bcd(length = xxx)` | **2.1.1** 开始支持 |

### 序列号BCD

| 源类型                  | 注解                          | 备注             |
|----------------------|-----------------------------|----------------|
| `String`             | `@ResponseFieldAlias.Bcd()` |                |
| `ByteArrayContainer` | `@ResponseFieldAlias.Bcd()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseFieldAlias.Bcd()` | **2.1.1** 开始支持 |

## STRING

### 反序列化STRING

| 目标类型                 | 注解                                        | 备注             |
|----------------------|-------------------------------------------|----------------|
| `String`             | `@RequestFieldAlias.String(length = xxx)` |                |
| `ByteArrayContainer` | `@RequestFieldAlias.String(length = xxx)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestFieldAlias.String(length = xxx)` | **2.1.1** 开始支持 |

### 序列化STRING

| 源类型                  | 注解                             | 备注             |
|----------------------|--------------------------------|----------------|
| `String`             | `@ResponseFieldAlias.String()` |                |
| `ByteArrayContainer` | `@ResponseFieldAlias.String()` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseFieldAlias.String()` | **2.1.1** 开始支持 |

