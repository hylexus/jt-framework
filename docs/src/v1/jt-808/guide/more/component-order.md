# 组件顺序

从 [协议入门](src/v1/jt-808/guide/basic) 和 [注解驱动开发](src/v1/jt-808/guide/annotation-based-dev) 的文档不难看出以下问题：


## 请求消息映射的实现

请求消息映射的实现方式：

- 手动实现并注册 `RequestMsgBodyConverter`
- 基于 `@Jt808ReqMsgBody` 注解方式处理
- 内置了 `AuthRequestMsgBodyConverter`

## 请求消息的业务处理的实现

请求消息的业务处理实现方式：

- 手动实现并注册 `MsgHandler` 接口
- 基于 `@Jt808RequestMsgHandler` 注解方式处理 
- 内置了 `AuthMsgHandler`

## 引出的问题

以 `将请求消息映射为实体类` 的 `RequestMsgBodyConverter` 为例，假如：

- 1. `jt808.entity-scan.enable-builtin-entity = true`
    - 至少内置了鉴权消息的实体类
- 2. 自定义并注册了 `RequestMsgBodyConverter` 的实现类
- 3. 同时扫描了 `@Jt808ReqMsgBody` 修饰的请求消息体实体类

::: danger ？？？
那么此时到底由谁去处理 `byte[] -> 请求消息体实体类` 的映射功能呢？？？
:::

- 像处理器链一样逐个往下调用谁能处理就谁处理，否则直接抛到下游？
    - 实现类多了有点头大，实现也有点麻烦。
    - 所以此处不按这种方式处理
- 按优先级找一个组件来处理？
    - 个人认为一种消息由一个组件来处理就够了
    - 如果处理逻辑太复杂，可以在单个组件内调用其他专门的复杂逻辑处理流程
    - 所以此处选择了这种优先级的处理方式，**相同功能的组件只会按照优先级注册其仅注册一个**

所以提供了一个 `io.github.hylexus.jt808.support.OrderedComponent` 接口，来处理组件注册时的 `优先级(相互覆盖)` 问题。

## OrderedComponent

以下是 `OrderedComponent` 接口的声明：

```java
public interface OrderedComponent {

    int DEFAULT_ORDER = 0;

    int ANNOTATION_BASED_DEV_COMPONENT_ORDER = 100;

    int BUILTIN_COMPONENT_ORDER = 200;


    default int getOrder() {
        return DEFAULT_ORDER;
    }

    default boolean shouldBeReplacedBy(OrderedComponent other) {
        // 数字越小优先级越高
        // 数字小的覆盖数字大的
        return this.getOrder() > other.getOrder();
    }
}
```

内置的 `OrderedComponent` 实现类至少有以下几个：

<p class="">
    <img :src="$withBase('/img/builtin-ordered-component.png')" alt="builtin-ordered-component">
</p>

::: tip 由以上源码不难看出组件的优先级问题：
相同功能的组件只会 `按照优先级注册其仅注册一个` ：

- 1. 手动实现并注册的 `MsgHandler` 和 `RequestMsgBodyConverter` 优先级最高
- 2. 基于注解实现的 `MsgHandler` 和 `RequestMsgBodyConverter` 次之
- 3. 内置组件的优先级最低
:::


