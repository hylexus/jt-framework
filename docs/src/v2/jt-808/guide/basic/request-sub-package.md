---
icon: object
---

# 请求消息分包

## 分包合并

收到终端上报的分包请求时，会先将分包消息 **暂存** 到 **分包暂存器(Jt808RequestSubPackageStorage)**;等所有子包都到达后会自动合并消息，投递给消息处理器处理。

### 分包暂存器

```java
/**
 * 遇到分包请求时会回调 {@link #saveSubPackage(Jt808Request)} 将分包暂存起来。
 * <p>
 * 实现类至少应该实现下面几个功能:
 *
 * <ol>
 *     <li>当所有子包都到达后，实现类应该负责将消息合并 && 将合并后的完整消息使用 {@link Jt808RequestMsgDispatcher#doDispatch(Jt808Request)} 投递出去</li>
 *     <li>某些子包丢失未到达时应该自动发送 `0x8003` 消息给终端，要求终端重传某些子包</li>
 *     <li>长时间未到达服务端的子包应该及时回收掉, 最长暂存多久由具体实现类自行决定</li>
 * </ol>
 *
 * @author hylexus
 * @see Jt808RequestMsgDispatcher#doDispatch(Jt808Request)
 */
public interface Jt808RequestSubPackageStorage {

    /**
     * 分包请求到达时回调该方法，暂存分包请求。
     * <p>
     * 如有必要，实现类应该自行回收掉 {@link Jt808Request#body()} 和 {@link Jt808Request#rawByteBuf()}
     *
     * @param subPackage 分包请求
     */
    void saveSubPackage(Jt808Request subPackage);

}
```

### 内置分包暂存器

::: danger

如果配置了 `jt808.request-sub-package-storage.type = none`，也就意味着所有的分包请求都会被**丢弃**!!!

:::

::: danger

如果配置了 `jt808.request-sub-package-storage.type = none`，也就意味着所有的分包请求都会被**丢弃**!!!

:::

::: danger

如果配置了 `jt808.request-sub-package-storage.type = none`，也就意味着所有的分包请求都会被**丢弃**!!!

:::

- `CaffeineJt808RequestSubPackageStorage`
    - 基于 `caffeine` 的请求消息分包暂存器
    - 当 `jt808.request-sub-package-storage.type = caffeine` 时启用
- `Jt808RequestSubPackageStorage.NO_OPS`
    - 空的实现(忽略所有分包请求)
    - 当 `jt808.request-sub-package-storage.type = none` 时启用

### 示例

下面以终端注册消息(`0x0001`) 为例来观察一下分包的过程。

::: tip 暂时调整日志级别以便观察分包请求处理

```yaml
logging:
  level.root: info
  # 将默认解码器的日志级别调整到debug 来观察请求分包消息
  level.jt-808.request.decoder: debug
  # 将请求分包暂存器(默认为基于Caffeine的实现)的日志级别调整的debug 来观察分包请求的处理过程
  level.io.github.hylexus.jt.jt808.support.codec.impl.CaffeineJt808RequestSubPackageStorage: debug
```

:::

发送下面三条分包报文：

```shell
7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E
7E010060240100000000013912344329000100030002383837363534333231494430303030313233343536373831323334353637383837363534357E
7E0100600E010000000001391234432900020003000333323101B8CA4A2D313233343539347E
```

发送三条报文之后，可以观察到类似如下日志：

<p class="">
    <img :src="$withBase('/img/v2/basic/Jt808RequestBody-sub-package.png.png')">
</p> 

上面日志解释如下：

```shell
# 0x0001 的第1个子包(-: 转义之前; +: 转义之后)
- >>>>>>>>>>>>>>> : 7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E
+ >>>>>>>>>>>>>>> : 7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E
+ >>>>>>>>>>>>>>> (0x0100--60) 1/3: 7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E
# 0x0001 的第2个子包(-: 转义之前; +: 转义之后)
- >>>>>>>>>>>>>>> : 7E010060240100000000013912344329000100030002383837363534333231494430303030313233343536373831323334353637383837363534357E
+ >>>>>>>>>>>>>>> : 7E010060240100000000013912344329000100030002383837363534333231494430303030313233343536373831323334353637383837363534357E
+ >>>>>>>>>>>>>>> (0x0100--60) 2/3: 7E010060240100000000013912344329000100030002383837363534333231494430303030313233343536373831323334353637383837363534357E
# 0x0001 的第3个子包(-: 转义之前; +: 转义之后)
- >>>>>>>>>>>>>>> : 7E0100600E010000000001391234432900020003000333323101B8CA4A2D313233343539347E
- + >>>>>>>>>>>>>>> : 7E0100600E010000000001391234432900020003000333323101B8CA4A2D313233343539347E
+ >>>>>>>>>>>>>>> (0x0100--38) 3/3: 7E0100600E010000000001391234432900020003000333323101B8CA4A2D313233343539347E
# 分包暂存器发现所有子包都到达了 --> 合并消息重新投递出去待处理器处理
DEBUG i.g.h.j.j.s.c.i.CaffeineJt808RequestSubPackageStorage - Redispatch mergedRequest : DefaultJt808Request{msgType=BuiltInMsgType{msgId=256(0x0100), desc='终端注册'}, header=HeaderSpec{version=VERSION_2019, terminalId='00000000013912344329', msgId=256, flowId=2, msgBodyProps=MsgBodyProps{intValue=16470, msgBodyLength=86, hasSubPackage=false, encryptionType=0}}, checkSum=0}
```

## 分包补传

这里的分包补传指的是 `0x8003` 消息。

内置的分包请求暂存器目前就只有一种基于 `Caffeine` 的实现 : `CaffeineJt808RequestSubPackageStorage`。

::: tip

暂时没想到一种比较优雅的、自动化的、可扩展的服务端分包补传处理流程。

所以服务端分包补传消息暂时不支持，后续版本升级会实现。

也就是说，**2.0.0** 版本不支持自动化的 `0x8003` 消息处理。

你可以自定义 `Jt808RequestSubPackageStorage` 的实现类来实现这个功能。

:::
