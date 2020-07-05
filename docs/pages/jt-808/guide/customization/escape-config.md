# 转义相关

::: tip
每个硬件厂商实现808协议的时候对转义的处理可能略有不同。

如果内置的转义逻辑不符合要求，可以自己实现 BytesEncoder 接口实现转义逻辑。

之后覆盖内置的处理逻辑即可。
:::

```java
@Configuration
public class Jt808Config extends Jt808ServerConfigurationSupport {
    
    @Override
    public BytesEncoder supplyBytesEncoder() {
        return new BytesEncoder() {
    
            @Override
            public byte[] doEscapeForReceive(byte[] bytes, int start, int end) throws MsgEscapeException {
                return ...;
            }
    
            @Override
            public byte[] doEscapeForSend(byte[] bytes, int start, int end) throws MsgEscapeException {
                return ...;
            }
        };
    }
}
```

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::