package io.github.hylexus.jt.annotation.msg.extra;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-10-01 8:39 下午
 * @see ExtraField
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtraMsgBody {

    int DEFAULT_BYTE_COUNT_OF_MSG_ID = 1;
    int DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH = 1;

    int byteCountOfMsgId() default DEFAULT_BYTE_COUNT_OF_MSG_ID;

    int byteCountOfContentLength() default DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH;
}
