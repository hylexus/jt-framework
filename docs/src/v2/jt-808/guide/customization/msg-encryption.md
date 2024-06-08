---
icon: lock
---

# 消息加密(v2.1.4)

## 相关资料

- [#issues-82](https://github.com/hylexus/jt-framework/issues/82)

## 加密方式的判断

收到消息，解析之后，消息体**可能**是密文。具体通过消息头中的消息体属性字段判断。

消息体属性格式如下：

- v2011/v2013

```
消息体属性 word(16)
    bit[0-10)	消息体长度
    bit[10-13)	数据加密方式
                    此三位都为 0,表示消息体不加密
                    第 10 位为 1,表示消息体经过 RSA 算法加密
                    其它保留
    bit[13]		分包
                    1: 消息体卫长消息,进行分包发送处理,具体分包信息由消息包封装项决定
                    0: 则消息头中无消息包封装项字段
    bit[14-15]	保留
```

- v2019

```
消息体属性 word(16)
    bit[0-10)	消息体长度
    bit[10-13)	数据加密方式
                    此三位都为 0,表示消息体不加密
                    第 10 位为 1,表示消息体经过 RSA 算法加密
                    其它保留
    bit[13]		分包
                    1: 消息体卫长消息,进行分包发送处理,具体分包信息由消息包封装项决定
                    0: 则消息头中无消息包封装项字段
    bit[14]	    版本标识
    bit[15]	保留
```

尽管消息体属性有所不同，但是其中的 `数据加密方式位` 都是相同的：消息体属性中的 `bit10`, `bit11` 和 `bit12` 三个位。

这三个二进制位具体含义，应该和具体的硬件实现相关。

## 加密/解密报文

可以通过自定义 `Jt808MsgEncryptionHandler` 实现报文的加密/解密。也就是给 **spring** 容器中加入一个 `Jt808MsgEncryptionHandler` 实现类即可。

```java

@Component
public class Jt808MsgEncryptionHandlerDemo01 implements Jt808MsgEncryptionHandler {

    @Override
    public ByteBuf decryptRequestBody(Jt808RequestHeader header, ByteBuf body) {
        final int encryptionType = header.msgBodyProps().encryptionType();
        if (encryptionType == 0) {
            return body;
        }
        // @see https://github.com/hylexus/jt-framework/issues/82
        // 消息属性中的 第10位，11位，12位 为 010 时，表示消息体经过SM4算法加密
        if (encryptionType == 0b010) {
            try {
                return JtCryptoUtil.SM4.ecbDecrypt(getSecretKey(), body);
            } finally {
                JtProtocolUtils.release(body);
            }
        }
        throw new NotImplementedException("不支持的加密类型: 0b" + FormatUtils.toBinaryString(encryptionType, 3));
    }

    @Override
    public ByteBuf encryptResponseBody(Jt808Response response, ByteBuf plaintextBody) {
        // response.encryptionType(010);
        final int encryptionType = response.encryptionType();
        if (encryptionType == 0) {
            return plaintextBody;
        }

        // @see https://github.com/hylexus/jt-framework/issues/82
        // 消息属性中的 第10位，11位，12位 为 010 时，表示消息体经过SM4算法加密
        if (encryptionType == 0b010) {
            try {
                return JtCryptoUtil.SM4.ecbEncrypt(getSecretKey(), plaintextBody);
            } finally {
                JtProtocolUtils.release(plaintextBody);
            }
        }
        throw new NotImplementedException("不支持的加密类型: 0b" + FormatUtils.toBinaryString(encryptionType, 3));
    }

    private byte[] getSecretKey() {
        // 从其他配置中获取密钥
        return HexStringUtils.hexString2Bytes("8e47374be6b8d114cb47be6a9a128a37");
    }
}
```
