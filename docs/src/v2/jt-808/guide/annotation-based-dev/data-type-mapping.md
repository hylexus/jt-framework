---
icon: map
---

# 数据类型转换(传统方式)

## BYTE

### 反序列化BYTE

| 目标类型                 | 注解                               | 备注             |
|----------------------|----------------------------------|----------------|
| `byte, Byte`         | `@RequestField(dataType = BYTE)` | 有溢出的风险         |
| `short, Short`       | `@RequestField(dataType = BYTE)` |                |
| `int, Integer`       | `@RequestField(dataType = BYTE)` |                |
| `long, Long`         | `@RequestField(dataType = BYTE)` |                |
| `ByteArrayContainer` | `@RequestField(dataType = BYTE)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestField(dataType = BYTE)` | **2.1.1** 开始支持 |
| `BitOperator`        | `@RequestField(dataType = BYTE)` | **2.1.1** 开始支持 |

### 序列化BYTE

| 源类型                  | 注解                                | 备注             |
|----------------------|-----------------------------------|----------------|
| `byte, Byte`         | `@ResponseField(dataType = BYTE)` |                |
| `short, Short`       | `@ResponseField(dataType = BYTE)` |                |
| `int, Integer`       | `@ResponseField(dataType = BYTE)` |                |
| `long, Long`         | `@ResponseField(dataType = BYTE)` |                |
| `ByteArrayContainer` | `@ResponseField(dataType = BYTE)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseField(dataType = BYTE)` | **2.1.1** 开始支持 |
| `BitOperator`        | `@ResponseField(dataType = BYTE)` | **2.1.1** 开始支持 |

## BYTES

### 反序列化BYTES

| 目标类型                 | 注解                                              | 备注             |
|----------------------|-------------------------------------------------|----------------|
| `byte[], Byte[]`     | `@RequestField(dataType = BYTES, length = xxx)` |                |
| `ByteBuf`            | `@RequestField(dataType = BYTES, length = xxx)` |                |
| `String`             | `@RequestField(dataType = BYTES, length = xxx)` |                |
| `ByteArrayContainer` | `@RequestField(dataType = BYTES, length = xxx)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestField(dataType = BYTES, length = xxx)` | **2.1.1** 开始支持 |

### 序列化BYTES

| 源类型                  | 注解                                 | 备注             |
|----------------------|------------------------------------|----------------|
| `byte[], Byte[]`     | `@ResponseField(dataType = BYTES)` |                |
| `ByteBuf`            | `@ResponseField(dataType = BYTES)` |                |
| `String`             | `@ResponseField(dataType = BYTES)` |                |
| `ByteArrayContainer` | `@ResponseField(dataType = BYTES)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseField(dataType = BYTES)` | **2.1.1** 开始支持 |

## WORD

### 反序列化WORD

| 目标类型                 | 注解                               | 备注             |
|----------------------|----------------------------------|----------------|
| `short, Short`       | `@RequestField(dataType = WORD)` | 有溢出的风险         |
| `int, Integer`       | `@RequestField(dataType = WORD)` |                |
| `long, Long`         | `@RequestField(dataType = WORD)` |                |
| `ByteArrayContainer` | `@RequestField(dataType = WORD)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestField(dataType = WORD)` | **2.1.1** 开始支持 |
| `BitOperator`        | `@RequestField(dataType = WORD)` | **2.1.1** 开始支持 |

### 序列化WORD

| 源类型                  | 注解                                | 备注             |
|----------------------|-----------------------------------|----------------|
| `short, Short`       | `@ResponseField(dataType = WORD)` |                |
| `int, Integer`       | `@ResponseField(dataType = WORD)` |                |
| `long, Long`         | `@ResponseField(dataType = WORD)` |                |
| `ByteArrayContainer` | `@ResponseField(dataType = WORD)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseField(dataType = WORD)` | **2.1.1** 开始支持 |
| `BitOperator`        | `@ResponseField(dataType = WORD)` | **2.1.1** 开始支持 |

## DWORD

### 反序列化DWORD

| 目标类型                 | 注解                                | 备注             |
|----------------------|-----------------------------------|----------------|
| `int, Integer`       | `@RequestField(dataType = DWORD)` | 有溢出的风险         |
| `long, Long`         | `@RequestField(dataType = DWORD)` |                |
| `ByteArrayContainer` | `@RequestField(dataType = DWORD)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestField(dataType = DWORD)` | **2.1.1** 开始支持 |
| `BitOperator`        | `@RequestField(dataType = DWORD)` | **2.1.1** 开始支持 |

### 序列化DWORD

| 源类型                  | 注解                                 | 备注             |
|----------------------|------------------------------------|----------------|
| `int, Integer`       | `@ResponseField(dataType = DWORD)` |                |
| `long, Long`         | `@ResponseField(dataType = DWORD)` |                |
| `ByteArrayContainer` | `@ResponseField(dataType = DWORD)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseField(dataType = DWORD)` | **2.1.1** 开始支持 |
| `BitOperator`        | `@ResponseField(dataType = DWORD)` | **2.1.1** 开始支持 |

## BCD

### 反序列化BCD

| 目标类型                 | 注解                                            | 备注             |
|----------------------|-----------------------------------------------|----------------|
| `String`             | `@RequestField(dataType = BCD, length = xxx)` |                |
| `ByteArrayContainer` | `@RequestField(dataType = BCD, length = xxx)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestField(dataType = BCD, length = xxx)` | **2.1.1** 开始支持 |

### 序列号BCD

| 源类型                  | 注解                               | 备注             |
|----------------------|----------------------------------|----------------|
| `String`             | `@ResponseField(dataType = BCD)` |                |
| `ByteArrayContainer` | `@ResponseField(dataType = BCD)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseField(dataType = BCD)` | **2.1.1** 开始支持 |

## STRING

### 反序列化STRING

| 目标类型                 | 注解                                               | 备注             |
|----------------------|--------------------------------------------------|----------------|
| `String`             | `@RequestField(dataType = STRING, length = xxx)` |                |
| `ByteArrayContainer` | `@RequestField(dataType = STRING, length = xxx)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@RequestField(dataType = STRING, length = xxx)` | **2.1.1** 开始支持 |

### 序列化STRING

| 源类型                  | 注解                                  | 备注             |
|----------------------|-------------------------------------|----------------|
| `String`             | `@ResponseField(dataType = STRING)` |                |
| `ByteArrayContainer` | `@ResponseField(dataType = STRING)` | **2.1.1** 开始支持 |
| `ByteBufContainer`   | `@ResponseField(dataType = STRING)` | **2.1.1** 开始支持 |

