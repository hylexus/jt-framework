# 异常处理

直接实现 `MsgHandler` 等接口并 `手动注册` 的组件的异常处理可以在实现类中自己处理。

基于注解的 `MsgHandler` 的异常处理可以直接由注解实现。

具体配置请移步 [配置文档](../../config/#exception-handler-scan)

## @Jt808RequestMsgHandlerAdvice

类比于 `Spring` 的 `@ControllerAdvice` 注解。

```java
@Slf4j
@Jt808RequestMsgHandlerAdvice
public class BuiltinDefaultExceptionHandler {
    // ...
}
```

## @Jt808ExceptionHandler

类比于 `Spring` 的 `@ExceptionHandler` 注解。

```java
@Slf4j
@Jt808RequestMsgHandlerAdvice
public class BuiltinDefaultExceptionHandler {

    @Jt808ExceptionHandler({Throwable.class})
    public void processThrowable(Throwable throwable) {
        log.info("BuiltinDefaultExceptionHandler :", throwable);
    }
}
```

::: tip 传送门
内置的异常处理器位于 `BuiltinDefaultExceptionHandler` 中。
:::
