package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt.utils.JtCryptoUtil;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

/**
 * @author hylexus
 * @see <a href="https://github.com/hylexus/jt-framework/issues/82">https://github.com/hylexus/jt-framework/issues/82</a>
 */
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
