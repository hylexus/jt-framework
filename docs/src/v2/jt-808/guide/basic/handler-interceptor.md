---
icon: plugin
---

# 处理器拦截器

## 介绍

从 `SpringMvc` 借鉴(抄袭)了一个 `Jt808HandlerInterceptor` ,会在执行处理器方法之前、之后已经处理器方法出现异常时回调对应方法。

这个接口的作用和 `SpringMvc`
的 [HandlerInterceptor](https://github.com/spring-projects/spring-framework/blob/v5.3.14/spring-webmvc/src/main/java/org/springframework/web/servlet/HandlerInterceptor.java)
一样。

::: tip

将 `Jt808HandlerInterceptor` 的实现类实例加入到 `spring` 容器中就可以生效了。

:::

接口定义如下：

```java
public interface Jt808HandlerInterceptor extends OrderedComponent {

    /**
     * 调用处理器方法之前回调
     *
     * @param exchange 当前请求上下文
     * @param handler  处理当前消息的处理器,内置处理器有两种:
     *                 <ol>
     *                      <li>{@link SimpleJt808RequestHandler SimpleJt808RequestHandler} 的实现类</li>
     *                      <li>被 {@link Jt808RequestHandlerMapping @Jt808RequestHandlerMapping} 标记的方法</li>
     *                 </ol>
     * @return true: 会正常调用处理器方法处理消息; false: 直接返回,不再调用处理器方法
     */
    default boolean preHandle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        return true;
    }

    /**
     * 处理器执行结束之后回调
     *
     * @param exchange      当前请求上下文
     * @param handler       处理当前消息的处理器,内置处理器有两种:
     *                      <ol>
     *                           <li>{@link SimpleJt808RequestHandler SimpleJt808RequestHandler} 的实现类</li>
     *                           <li>被 {@link Jt808RequestHandlerMapping @Jt808RequestHandlerMapping} 标记的方法</li>
     *                      </ol>
     * @param handlerResult 处理器处理结果
     */
    default void postHandle(Jt808ServerExchange exchange, Object handler, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
    }

    /**
     * @param exchange  当前请求上下文
     * @param handler   处理当前消息的处理器,内置处理器有两种:
     *                  <ol>
     *                       <li>{@link SimpleJt808RequestHandler SimpleJt808RequestHandler} 的实现类</li>
     *                       <li>被 {@link Jt808RequestHandlerMapping @Jt808RequestHandlerMapping} 标记的方法</li>
     *                  </ol>
     * @param exception 处理器处理消息时候出现的异常(@Nullable)
     */
    default void afterCompletion(Jt808ServerExchange exchange, Object handler, @Nullable Throwable exception) throws Throwable {
    }
}
```

## 示例

实现接口，并加入到 Spring 容器中即可。如果有多个拦截器，可以通过复写 `getOrder()` 方法来控制多个拦截器的执行顺序。

<CodeGroup>

  <CodeGroupItem title="示例1" active>

```java{24}
@Slf4j
@Component
public class DemoInterceptor01 implements Jt808HandlerInterceptor {

    @Override
    public boolean preHandle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        log.info("DemoInterceptor01#preHandle ... terminalId={}, msgType={}", exchange.request().terminalId(), exchange.request().msgType());
        return true;
    }

    @Override
    public void postHandle(Jt808ServerExchange exchange, Object handler, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
        log.info("DemoInterceptor01#postHandle ... terminalId={}, msgType={}, result={}",
                exchange.request().terminalId(), exchange.request().msgType(), handlerResult != null ? handlerResult.getReturnValue() : null);
    }

    @Override
    public void afterCompletion(Jt808ServerExchange exchange, Object handler, @Nullable Throwable exception) throws Throwable {
        log.info("DemoInterceptor01#afterCompletion ... terminalId={}, msgType={}, exception={}",
                exchange.request().terminalId(), exchange.request().msgType(), exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```

  </CodeGroupItem>

  <CodeGroupItem title="示例2" >

```java{24}
@Slf4j
@Component
public class DemoInterceptor02 implements Jt808HandlerInterceptor {

    @Override
    public boolean preHandle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        log.info("DemoInterceptor02#preHandle ... terminalId={}, msgType={}", exchange.request().terminalId(), exchange.request().msgType());
        return true;
    }

    @Override
    public void postHandle(Jt808ServerExchange exchange, Object handler, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
        log.info("DemoInterceptor01#postHandle ... terminalId={}, msgType={}, result={}",
                exchange.request().terminalId(), exchange.request().msgType(), handlerResult != null ? handlerResult.getReturnValue() : null);
    }

    @Override
    public void afterCompletion(Jt808ServerExchange exchange, Object handler, @Nullable Throwable exception) throws Throwable {
        log.info("DemoInterceptor01#afterCompletion ... terminalId={}, msgType={}, exception={}",
                exchange.request().terminalId(), exchange.request().msgType(), exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
```

  </CodeGroupItem>

</CodeGroup>

