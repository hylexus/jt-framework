# TerminalValidator

::: tip 鸣谢
- 该组件由 [anotherMe17](https://github.com/anotherMe17) 贡献。
- 非常感谢老哥 [anotherMe17](https://github.com/anotherMe17) 的 [Pull request#19](https://github.com/hylexus/jt-framework/pull/19) 。
:::

```java
@Configuration
public class Jt808Configuration extends Jt808ServerConfigurationSupport {
    // [非必须配置] -- 可替换内置 TerminalValidator
    @Override
    public TerminalValidator terminalValidator() {
        return new TerminalValidator() {
            @Override
            public boolean validateTerminal(RequestMsgMetadata metadata) {
                return true;
            }

            @Override
            public boolean needValidate(RequestMsgMetadata metadata, Integer msgId) {
                return true;
            }
        };
    }
}
```
