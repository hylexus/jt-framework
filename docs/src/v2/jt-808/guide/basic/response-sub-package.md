---
icon: object
---

# 响应消息分包

## 分包方式

`Jt808MsgEncoder` 的默认实现类 `DefaultJt808MsgEncoder` 会将较大的消息自动分包，分包的阈值可以手动指定。 默认值是 `1024` 字节。

也就是说：`DefaultJt808MsgEncoder` 在编码消息时(**转义之前**)，发现消息包的大小超过了**阈值**，会自动将消息分包发送给客户端。

内置的支持自动分包的返回类型有两种: `Jt808Response` 和被 `@Jt808ResponseBody` 标记的类型。

::: tip

可以将 `jt-808.response.encoder` 日志的级别调整为 `debug` 级别，这样就可以看到分包消息的详情了：

```yaml
logging:
  # 编码器日志
  level.jt-808.response.encoder: debug
```

:::

### Jt808Response

`Jt808Response` 有个属性 `maxPackageSize` 用来控制单个消息包的最大大小。默认 **1024** 字节。

如果响应消息的大小(**转义之前**)超过 `maxPackageSize()` 的阈值，会自动将消息拆分为多个子包发送。

```java{10-12,20}
public interface Jt808Response extends Jt808ByteWriter {

    int DEFAULT_MAX_PACKAGE_SIZE = 1024;

    /**
     * 响应消息大小超过该值(默认 {@value #DEFAULT_MAX_PACKAGE_SIZE})会自动分包发送(转义之前)
     *
     * @return 响应消息最大字节数
     */
    default int maxPackageSize() {
        return DEFAULT_MAX_PACKAGE_SIZE;
    }

    /**
     * 指定单个消息包的最大大小(转义之前)
     *
     * @param size 消息包最大大小
     * @return 单个消息包的最大大小
     */
    Jt808Response maxPackageSize(int size);

    // ...
}
```

### @Jt808ResponseBody

`@Jt808ResponseBody` 注解也有个 `maxPackageSize` 属性用来控制单个消息包的最大大小(**转义之前**)。

```java{8}
public @interface Jt808ResponseBody {

    /**
     * @return 单个消息包的最大字节数, 超过该值会自动分包发送
     * @see Jt808Response#DEFAULT_MAX_PACKAGE_SIZE
     * @see Jt808Response#maxPackageSize(int)
     */
    int maxPackageSize() default Jt808Response.DEFAULT_MAX_PACKAGE_SIZE;

    // ...
}
```

### 示例

::: code-tabs

@tab:active @Jt808ResponseBody示例

```java

@Slf4j
@Component
@Jt808RequestHandler
public class CommonHandler {

    // terminalId: 013912344329
    // 7E010040560100000000013912344329007B000B00026964393837363534333231747970653030313233343536373831323334353637383837363534333231494
    // 43030303031323334353637383132333435363738383736353433323101B8CA4A2D313233343539257E
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public TerminalRegisterReplyMsg clientRegisterV2019(Jt808RequestEntity<BuiltinMsg0100V2019> request) {
        final BuiltinMsg0100V2019 body = request.body();
        log.info("client register v2019 : {}", body);
        return new TerminalRegisterReplyMsg().setFlowId(request.flowId()).setResult((byte) 0).setAuthCode("AuthCode2019----");
    }

    @Data
    @Accessors(chain = true)
    // 这里用来测试分包消息(指定单个消息包最大大小为33字节)
    @Jt808ResponseBody(msgId = 0x8100, maxPackageSize = 33)
    public static class TerminalRegisterReplyMsg {
        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
        @ResponseField(order = 0, dataType = MsgDataType.WORD)
        private int flowId;
        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
        @ResponseField(order = 1, dataType = MsgDataType.BYTE)
        private byte result;
        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
        @ResponseField(order = 3, dataType = MsgDataType.STRING, conditionalOn = "result == 0")
        private String authCode;
    }
}

```

@tab Jt808Response#maxPackageSize()示例

```java

@Slf4j
@Component
public class TerminalRegisterMsgHandlerV2019 implements SimpleJt808RequestHandler<Jt808Response> {

    /**
     * 处理 [终端注册] 消息
     */
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Set.of(BuiltinJt808MsgType.CLIENT_REGISTER);
    }

    /**
     * 处理 [V2019] 版的消息
     */
    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2019();
    }

    // 7E010040560100000000013912344329007B000B0002696439383736353433323174797065303031323334353637383132333
    // 435363738383736353433323149443030303031323334353637383132333435363738383736353433323101B8CA4A2D313233343539257E
    @Override
    public Jt808Response handleMsg(Jt808ServerExchange exchange) {
        final BuiltinMsg0100V2019 body = new BuiltinMsg0100V2019();
        exchange.request()
                .bodyAsReader()
                // 1. [0-2) WORD 省域ID
                .readUnsignedWord(body::setProvinceId)
                // 2. [2-4) WORD 省域ID
                .readUnsignedWord(body::setCityId)
                // 3. [4-15) BYTE[11] 制造商ID
                .readString(11, body::setManufacturerId)
                // 4. [15-45) BYTE[30] 终端型号
                .readString(30, body::setTerminalType)
                // 5. [45-75) BYTE[30] 终端ID
                .readString(30, body::setTerminalId)
                // 6. [75]   BYTE    车牌颜色
                .readByte(body::setColor)
                // 7. [76,n)   String    车辆标识
                .readString(exchange.request().msgBodyLength() - 76, body::setCarIdentifier);
        log.info("body:{}", body);

        return exchange.response()
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                // 这里手动指定单个包最大大小为 33 字节
                .maxPackageSize(33)
                // 1. byte[0,2) WORD 对应的终端注册消息的流水号
                .writeWord(exchange.request().flowId())
                // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
                .writeByte(0)
                // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
                .writeString("AuthCode2019----");
    }
}

```

:::

发送下面这条测试报文

```shell
7E010040560100000000013912344329007B000B0002696439383736353433323174797065303031323334353637383132333435363738383736353433323149443030303031323334353637383132333435363738383736353433323101B8CA4A2D313233343539257E
```

然后观察 `debug` 日志的输出:

<p class="">
    <img :src="$withBase('/img/v2/basic/Jt808ResponseBody-sub-package.png.png')">
</p> 

上面日志解释如下：

```sh
# 0x8100 的第1个子包(-: 转义之前; +: 转义之后)
- <<<<<<<<<<<<<<< (0x8100--33) 1/3: 7E810060090100000000013912344329000000030001007B0041757468436FE07E
+ <<<<<<<<<<<<<<< (0x8100--33) 1/3: 7E810060090100000000013912344329000000030001007B0041757468436FE07E
# 0x8100 的第2个子包(-: 转义之前; +: 转义之后)
- <<<<<<<<<<<<<<< (0x8100--33) 2/3: 7E8100600901000000000139123443290001000300026465323031392D2D2DBB7E
+ <<<<<<<<<<<<<<< (0x8100--33) 2/3: 7E8100600901000000000139123443290001000300026465323031392D2D2DBB7E
# 0x8100 的第3个子包(-: 转义之前; +: 转义之后)
- <<<<<<<<<<<<<<< (0x8100--25) 3/3: 7E8100600101000000000139123443290002000300032DBA7E
+ <<<<<<<<<<<<<<< (0x8100--25) 3/3: 7E8100600101000000000139123443290002000300032DBA7E
```

## 分包补传

这里指的是 `0x0005` 的 **终端补传分包请求**。

当服务端将消息分包发送给终端时，如果终端没有收到某些子包，会发送 `0x0005` 请求要求服务端重传指定的子包。

### 分包暂存器

服务端将消息分包发送时，会将子包保存在 `Jt808ResponseSubPackageStorage` 中,该接口定义如下：

```java
public interface Jt808ResponseSubPackageStorage {

    /**
     * 保存某个子包(响应消息分包时会回调),子包保存多久看具体实现类的实现逻辑
     *
     * @param subPackage 子包
     */
    void saveSubPackage(Jt808Response.Jt808ResponseSubPackage subPackage);

    /**
     * 分包补传(0x0005) 时可以通过这个方法获取之前发送过的子包
     *
     * @param terminalId              终端手机号
     * @param firstFlowIdOfSubPackage 分包消息中的第一个子包的流水号
     * @param packageIds              要求重传的子包ID
     * @return 编码后子包消息字节流
     */
    Collection<ByteBuf> getSubPackageMsg(String terminalId, int firstFlowIdOfSubPackage, Collection<Integer> packageIds);
}
```

遇到分包补传请求 `0x0005` 时，可以从 Jt808ResponseSubPackageStorage 中获取子包信息。

### 内置分包暂存器

`Jt808ResponseSubPackageStorage` 内置了三个实现类:

- `CaffeineJt808ResponseSubPackageStorage`
    - 基于 caffeine 的实现
    - 配置项 `jt808.response-sub-package-storage.type=caffeine` 时启用
- `RedisJt808ResponseSubPackageStorage`
    - 基于 redis 的实现
    - 配置项 `jt808.response-sub-package-storage.type=redis` 时启用
- `Jt808ResponseSubPackageStorage.NO_OPS_STORAGE`
    - 空的实现
    - 配置项 `jt808.response-sub-package-storage.type=none` 时启用

### 示例

```java{27}
@Slf4j
@Component
@Jt808RequestHandler
public class RetransmissionHandler {

    private final Jt808ResponseSubPackageStorage responseSubPackageStorage;
    private final Jt808SessionManager sessionManager;

    public RetransmissionHandler(Jt808ResponseSubPackageStorage responseSubPackageStorage, Jt808SessionManager sessionManager) {
        this.responseSubPackageStorage = responseSubPackageStorage;
        this.sessionManager = sessionManager;
    }

    // 7E00054008010000000001391234432900010000000200010002387E
    // 7E000540080100000000013912344329000100030002000100023B7E
    @Jt808RequestHandlerMapping(msgType = 0x0005, desc = "终端补传分包请求")
    public void processRetransmissionMsg(Jt808RequestEntity<BuiltinMsg0005> request) {
        final Jt808Session session = this.sessionManager.findByTerminalId(request.terminalId()).orElseThrow();

        final BuiltinMsg0005 body = request.body();
        // 分包消息中第一包的流水号
        final int firstSubPackageFlowId = body.getFirstSubPackageFlowId();
        // 需要重传的子包ID
        final List<Integer> packageIdList = body.getPackageIdList().stream().map(BuiltinMsg0005.PackageId::getValue).collect(Collectors.toList());

        // 获取子包
        final Collection<ByteBuf> subPackageMsgList = responseSubPackageStorage.getSubPackageMsg(request.terminalId(), firstSubPackageFlowId, packageIdList);

        subPackageMsgList
                .stream()
                .peek(subPackageMsg -> log.info("re-send msg to client {} : {}", request.terminalId(), HexStringUtils.byteBufToString(subPackageMsg)))
                // 将子包发送给终端
                .forEach(session::sendMsgToClient);
    }
}
```