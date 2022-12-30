---
icon: rank
---

# 分包相关

要替换内置的分包暂存器只需将对应类型的 `Bean` 加入到 `Spring` 容器即可。

```java
// Jt808ResponseSubPackageStorage + Jt808RequestSubPackageStorage
@Configuration
public class MyJt808Config {
    // [[ 非必须配置 ]] -- 替换内置响应消息分包暂存器
    @Bean
    public Jt808ResponseSubPackageStorage myJt808ResponseSubPackageStorage() {
        return new MyResponseSubPackageStorage(new CaffeineJt808ResponseSubPackageStorage.StorageConfig());
    }

    // [[ 非必须配置 ]] -- 替换内置请求消息分包暂存器
    @Bean
    public Jt808RequestSubPackageStorage myJt808RequestSubPackageStorage(Jt808RequestMsgDispatcher dispatcher) {
        return new MyRequestSubPackageStorage(ByteBufAllocator.DEFAULT, dispatcher, new CaffeineJt808RequestSubPackageStorage.StorageConfig());
    }
}
```
