package io.github.hylexus.jt.annotation.msg;

import com.google.common.collect.Sets;

import java.lang.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-28 9:07 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdditionalField {

    Set<Class<?>> SUPPORTED_TARGET_CLASS = Sets.newHashSet(List.class);
    int ROOT_GROUP_MSG_ID = -1;
    int DEFAULT_BYTE_COUNT_OF_MSG_ID = 1;
    int DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH = 1;

    int startIndex();

    int length() default 0;

    String byteCountMethod() default "";

    MsgTypeMapping[] msgTypeMappings();

    @interface MsgTypeMapping {

        int groupMsgId() default ROOT_GROUP_MSG_ID;

        boolean isNestedAdditionalField() default false;

        int byteCountOfMsgId() default DEFAULT_BYTE_COUNT_OF_MSG_ID;

        int byteCountOfContentLength() default DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH;

    }
}
