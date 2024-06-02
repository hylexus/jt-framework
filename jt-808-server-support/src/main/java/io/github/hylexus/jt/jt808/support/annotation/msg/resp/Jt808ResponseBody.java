package io.github.hylexus.jt.jt808.support.annotation.msg.resp;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseBodyHandlerResultHandler;

import java.lang.annotation.*;

/**
 * Created At 2019-09-18 8:47 下午
 *
 * @author hylexus
 * @see Jt808Response
 * @see Jt808ResponseBodyHandlerResultHandler
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808ResponseBody {

    /**
     * @return 服务端响应消息的消息类型
     * @see Jt808Response#msgId()
     */
    int msgId();

    /**
     * @return 消息体加密类型
     * @see Jt808Response#encryptionType()
     * @since 2.1.4
     */
    byte encryptionType() default Jt808Response.DEFAULT_ENCRYPTION_TYPE;

    /**
     * @return 单个消息包的最大字节数, 超过该值会自动分包发送
     * @see Jt808Response#DEFAULT_MAX_PACKAGE_SIZE
     * @see Jt808Response#maxPackageSize(int)
     */
    int maxPackageSize() default Jt808Response.DEFAULT_MAX_PACKAGE_SIZE;

    /**
     * @return 消息头的消息体属性字段中保留的第15个二进制位
     * @see Jt808Response#reversedBit15InHeader()
     */
    byte reversedBit15InHeader() default 0;

    String desc() default "";
}
