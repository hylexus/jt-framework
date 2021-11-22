package io.github.hylexus.jt.annotation.msg.req;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;

import java.lang.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-28 9:07 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DebugOnly
public @interface AdditionalField {

    Set<Class<?>> SUPPORTED_TARGET_CLASS = Collections.singleton(List.class);
    int ROOT_GROUP_MSG_ID = -1;
    int DEFAULT_BYTE_COUNT_OF_MSG_ID = 1;
    int DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH = 1;

    /**
     * @see BasicField#startIndex()
     */
    int startIndex();

    /**
     * @see BasicField#length()
     */
    int length() default 0;

    /**
     * @see BasicField#byteCountMethod()
     */
    String byteCountMethod() default "";

    MsgTypeMapping[] msgTypeMappings();

    @interface MsgTypeMapping {

        int groupMsgId() default ROOT_GROUP_MSG_ID;

        boolean isNestedAdditionalField() default false;

        int byteCountOfMsgId() default DEFAULT_BYTE_COUNT_OF_MSG_ID;

        int byteCountOfContentLength() default DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH;

    }
}
