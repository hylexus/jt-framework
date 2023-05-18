---
icon: customize
---

# 自定义注解(v2.1.1)

## 请先读我

这里说的自定义注解是通过 **v2.1.1** 中引入的注解别名机制来扩展的自定义注解。

下面就以位置上报报文中的经纬度字段和时间字段为例，演示如何扩展自己的注解。

<p class="">
    <img :src="$withBase('/img/v2/annotation-driven-dev/custom-annotation.png')">
</p> 

## 示例1(GeoPoint)

### 目的

这里的经纬度其实就是将 **4** 字节的 `DWORD` 转为数字(`Long`)，然后再除以 10^6^。

```java

@Data
@Jt808RequestBody
public class BuiltinMsg0200V2013Alias {
    // ...

    // (3). byte[8,12) DWORD 纬度
    @RequestFieldAlias.Dword(order = 3)
    private long lat;

    // (4). byte[12,16) DWORD 经度
    @RequestFieldAlias.Dword(order = 4)
    private long lng;

    // ...
}
```

但是内置的转换器实际上不支持从 `DWORD` 到 `Double` 的转换，只能写成 `Long` 然后再手动除以 10^6^ 转为浮点数。

不过你可以扩展自定义注解实现这个转换。

### 定义自己的注解

```java

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestField(dataType = MsgDataType.DWORD, order = -1, customerFieldDeserializerClass = ExtendedJt808FieldDeserializerGeoPoint.class)
public @interface GeoPoint {
    @AliasFor(annotation = RequestField.class, attribute = "order")
    int order();

    @AliasFor(annotation = RequestField.class, attribute = "desc")
    java.lang.String desc() default "";
}
```

### 提供一个转换器

```java
// 自定义的转换器 [不需要] 手动实例化, [也不需要] 注册到 `Jt808FieldDeserializerRegistry` 中
public class ExtendedJt808FieldDeserializerGeoPoint extends AbstractExtendedJt808FieldDeserializer<Object> {
    private final LongFieldDeserializer delegate = new LongFieldDeserializer();

    @Override
    public Object deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {

        // 1. 先委托内置的 LongFieldDeserializer 解析为 LONG
        final Long dword = this.delegate.deserialize(byteBuf, msgDataType, start, length);
        final Class<?> targetClass = context.fieldMetadata().getFieldType();
        // 2. 然后再根据自己的要求进一步转换
        if (Long.class.isAssignableFrom(targetClass) || long.class.isAssignableFrom(targetClass)) {
            return dword;
        } else if (Double.class.isAssignableFrom(targetClass) || double.class.isAssignableFrom(targetClass)) {
            return dword * 1.0 / 1_000_000;
        } else if (BigDecimal.class.isAssignableFrom(targetClass)) {
            return new BigDecimal(String.valueOf(dword)).setScale(6, RoundingMode.UP).divide(new BigDecimal("1000000"), RoundingMode.UP);
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to " + targetClass);
    }

}
```

### 使用自定义注解

```java

@Data
@Jt808RequestBody
public class BuiltinMsg0200V2013Alias {
    // ...

    // (3). byte[8,12) DWORD 纬度
    @GeoPoint(order = 3)
    // 支持 long, double, BigDecimal
    private double lat;

    // (4). byte[12,16) DWORD 经度
    @GeoPoint(order = 4)
    // 支持 long, double, BigDecimal
    private BigDecimal lng;

    // ...
}
```

::: tip

实际上这里演示的这个注解已经在 **2.1.1** 中内置了, 全类名是 `@io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias.GeoPoint`。

:::

## 示例2(BcdDateTime)

### 目的

位置上报报文中的时间字段是以 `BCD` 格式编码的字符串，格式为 `yyMMddHHmmss`。

```java

@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0200V2013Alias {
    // ...

    // (8). byte[22,28) BCD[6] 时间
    @RequestFieldAlias.Bcd(order = 8, length = 6)
    private String time;

    // ...
}
```

但是内置的反序列化器并不支持直接从 `BCD` 转为 `Date` 或 `LocalDateTime`，只能转为 `String` 或其他类型。

从 **2.1.1** 开始，你可以定义自己的注解来完成这种特殊需求。

### 定义自己的注解

```java

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestField(dataType = MsgDataType.BCD, length = 6, customerFieldDeserializerClass = MyExtendedJt808FieldDeserializerBcdTime.class, order = -1)
public @interface BcdDateTime {
    @AliasFor(annotation = RequestField.class, attribute = "order")
    int order();

    @AliasFor(annotation = RequestField.class, attribute = "desc")
    java.lang.String desc() default "";

    // 也可以自己扩展注解的属性
    String pattern() default "yyMMddHHmmss";
}
```

### 提供一个转换器

```java
// 自定义的转换器 [不需要] 手动实例化, [也不需要] 注册到 `Jt808FieldDeserializerRegistry` 中
public class MyExtendedJt808FieldDeserializerBcdTime extends AbstractExtendedJt808FieldDeserializer<Object> {
    private final BcdFieldDeserializer delegate = new BcdFieldDeserializer();

    @Override
    public Object deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final Class<?> targetClass = context.fieldMetadata().getFieldType();
        final String bcd = this.delegate.deserialize(byteBuf, msgDataType, start, length);
        final BcdDateTime annotation = context.fieldMetadata().getAnnotation(BcdDateTime.class);
        final String pattern = annotation.pattern();

        if (LocalDateTime.class.isAssignableFrom(targetClass)) {
            return LocalDateTime.parse(bcd, DateTimeFormatter.ofPattern(pattern));
        } else if (Date.class.isAssignableFrom(targetClass)) {
            try {
                return new SimpleDateFormat(pattern).parse(bcd);
            } catch (ParseException e) {
                throw new Jt808FieldSerializerException(e);
            }
        } else if (String.class.isAssignableFrom(targetClass)) {
            return bcd;
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to " + targetClass);
    }
}
```

### 使用自定义注解

现在可以通过自定义注解将 `BCD` 转换为 `LocalDateTime`、 `Date` 或 `String` 了：

```java

@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0200V2013Alias {
    // ...

    // (8). byte[22,28) BCD[6] 时间
    @BcdDateTime(order = 7, pattern = "yyMMddHHmmss")
    private LocalDateTime time;
    // private Date time;
    // private String time;

    // ...
}
```

::: tip

实际上这里演示的这个注解已经在 **2.1.1** 中内置了, 全类名是 `@io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias.BcdDateTime`。

:::

## 其他说明

- 内置的给 `@RequestField` 提供的别名都在 `@RequestFieldAlias` 中
- 要扩展 `@ResponseField` 也是同样的道理，可以参考 `@ResponseFieldAlias` 中内置的一堆别名
- 要扩展一种不支持的数据类型, 比如 `LWORD`(`Long Word`, `unsigned 64 bit`), 也可以通过注解别名实现
