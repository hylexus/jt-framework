package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 * @see <a href="https://github.com/hylexus/jt-framework/issues/82">https://github.com/hylexus/jt-framework/issues/82</a>
 * @since 2.1.4
 */
public interface Jt808MsgEncryptionHandler {

    /**
     * @param header 请求头
     * @param body   请求体；可能是明文也可能是密文，根据 `header` 判断
     * @return 解密之后的明文 或者 原样返回 `body`
     * @see Jt808RequestHeader#msgBodyProps()
     * @see Jt808RequestHeader.Jt808MsgBodyProps#encryptionType()
     * @see Jt808RequestHeader.Jt808MsgBodyProps#dataEncryptionType()
     */
    ByteBuf decryptRequestBody(Jt808RequestHeader header, ByteBuf body);

    /**
     * @param response      本次响应的其他信息
     * @param plaintextBody 明文数据；可能是完整包，也可能是一个子包
     * @return 返回密文 或者 原样返回`plaintextBody`
     * @see Jt808ResponseBody#encryptionType()
     * @see Jt808Response#encryptionType(int)
     * @see Jt808Response#encryptionType(Jt808MsgEncryptionType)
     * @see Jt808MsgBuilder#encryptionType(int)
     * @see Jt808MsgBuilder#encryptionType(Jt808MsgEncryptionType)
     */
    ByteBuf encryptResponseBody(Jt808Response response, ByteBuf plaintextBody);

    Jt808MsgEncryptionHandler NO_OPS = new NoOps();

    @BuiltinComponent
    class NoOps implements Jt808MsgEncryptionHandler {

        @Override
        public ByteBuf decryptRequestBody(Jt808RequestHeader header, ByteBuf body) {
            return body;
        }

        @Override
        public ByteBuf encryptResponseBody(Jt808Response response, ByteBuf plaintextBody) {
            return plaintextBody;
        }
    }
}
