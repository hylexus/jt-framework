---
icon: at
---

# 注解别名(v2.1.1)

::: tip

注解别名是 <Badge text="2.1.1" type="tip" vertical="middle"/> 中开始引入的。

:::

## 为什么要引入别名机制?

一句话概括就是: 简化代码，同时又能方便地扩展自定义注解。

::: tip

- 为 `@RequestField` 注解提供了别名 `@RequestFieldAlias`
- 为 `@ResponseField` 注解提供了别名 `@ResponseFieldAlias`
- 相当于 **Spring** 中 `@RequestMapping` 和 `@GetMapping` 的关系

:::

以 **位置上报** 报文为例，解析经纬度的实体类可能是下面这种写法：

```java

@Jt808RequestBody
public class BuiltinMsg0200V2019 {
    // ...
    // (3). byte[8,12) DWORD 纬度
    @RequestField(order = 3, startIndex = 8, dataType = DWORD)
    private long lat;

    // (4). byte[12,16) DWORD 经度
    @RequestField(order = 4, startIndex = 12, dataType = DWORD)
    private long lng;

    // (8). byte[22,28) BCD[6] 时间
    @RequestField(order = 8, startIndex = 22, dataType = BCD, length = 6)
    private String time;
    // ...
}
```

你可能会对于上面的代码片段有几个疑问：

1. `order` 是必须要指定的吗？
    - 这个问题暂且不讨论(暂且就认为是必须的吧)
    - [参考Stackoverflow](https://stackoverflow.com/questions/5001172/java-reflection-getting-fields-and-methods-in-declaration-order)
2. `startIndex` 是必须要指定的吗？
    - 其实这个属性是从 **1.x** 继承过来的
    - **1.x** 的版本是必须的，因为 **1.x** 是通过 `byte[]` 来解析报文的，没有维护 `readerIndex`
    - **2.x** 的版本通过 `Netty` 的 `ByteBuf` 来解析的。虽然有这个 `startIndex` 属性，但是 `Jt808FieldDeserializer` 的实现类中几乎没用到这个属性。也就是说 **2.x**
      中可以不用指定这个属性了(实现类依赖于 `Netty` 的 `ByteBuf` 的 `readerIndex`)。
3. `dataType` 是必须要指定的吗？
    - `dataType` 和 `length` 两个属性其实在一定角度来看可以看成一回事。
    - `dataType= DWOD` 也就是间接指定了 `length = 4`
    - `dataType = BYTE` 也就是间接指定了 `length = 1`
    - 但是 `dataType = STRING` 时, 就需要指定 `length` 了。
    - 所以 `@RequestField` 注解中包含了这么多属性。
    - 但是在使用时难免会疑惑: 到底该哪些属性是必须的???
4. `length` 是必须要指定的吗？(和上一个问题类似)

鉴于以上几个问题，**2.1.1** 开始引入了注解别名机制。

说白了就是:

- 为 `@RequestField` 注解提供了快捷方式 `@RequestFieldAlias`
- 为 `@ResponseField` 注解提供了快捷方式 `@ResponseFieldAlias`

下面是使用注解别名实现的同样效果的实体类映射(暂且先忽略 `order` 属性)：

```java

@Jt808RequestBody
public class BuiltinMsg0200V2019Alias {
    // ...
    // (3). byte[8,12) DWORD 纬度
    @RequestFieldAlias.Dword(order = 3)
    private long lat;

    // (4). byte[12,16) DWORD 经度
    @RequestFieldAlias.Dword(order = 4)
    private long lng;

    // (8). byte[22,28) BCD[6] 时间
    @RequestFieldAlias.Bcd(order = 8, length = 6)
    private String time;
    // ...
}
```

省去了 `startIndex` 和 部分 `length` 属性。
上面代码中的 `@RequestFieldAlias` 相比于 `@RequestField` 是不是简介多了？

## @RequestFieldAlias

`RequestFieldAlias` 是给 `@RequestField` 提供的别名(快捷方式)。

```java
public @interface RequestFieldAlias {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.WORD, order = -1)
    @interface Word {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.DWORD, order = -1)
    @interface Dword {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.BCD, order = -1)
    @interface Bcd {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "length")
        int length() default -1;

        @AliasFor(annotation = RequestField.class, attribute = "lengthExpression")
        java.lang.String lengthExpression() default "";

        @AliasFor(annotation = RequestField.class, attribute = "lengthMethod")
        java.lang.String lengthMethod() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";

    }

    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
}
```

## @ResponseFieldAlias

`@ResponseFieldAlias` 是给 `@ResponseField` 提供的别名(快捷方式)。

```java
public @interface ResponseFieldAlias {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.WORD, order = -1)
    @interface Word {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "customerFieldSerializerClass")
        Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass() default Jt808FieldSerializer.PlaceholderFiledSerializer.class;

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.DWORD, order = -1)
    @interface Dword {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "customerFieldSerializerClass")
        Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass() default Jt808FieldSerializer.PlaceholderFiledSerializer.class;

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
}
```

## FAQ

### 兼容性

**Question**: 注解别名和原来的 `@RequestField`、 `@ResponseField` 兼容吗？

**Answer**: 兼容

- 注解别名就是对 `@RequestField`、 `@ResponseField` 的 `{简化,扩展}`
- 底层的 `Jt808FieldDeserializer` 和 `Jt808FieldSerializer` 读取的依然是 `@RequestField` 和 `@ResponseField`
- 实际上是借助于 **Spring** 提供的 `@AliasFor` 的强大功能实现的

### 混合使用

**Question**: 混合使用问题？

1. `@RequestFieldAlias` 和 `@RequestField` 能混合使用吗？
2. `@ResponseFieldAlias` 是给 `@ResponseField` 能混合使用吗？

**Answer**: 可以混合使用

本质上和上一个问题是同一个问题。

底层的 `Jt808FieldDeserializer` 和 `Jt808FieldSerializer` 读取的依然是 `@RequestField` 和 `@ResponseField`。

别名(`Alias`)只是个快捷方式或者扩展。
