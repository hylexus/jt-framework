package io.github.hylexus.jt.annotation.msg.req.extra;

import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.data.MsgDataType;

import java.lang.annotation.*;

/**
 * {@link ExtraMsgBody} 和 该注解的解析思路受这位仁兄的启发 : <a>https://github.com/qianhongtang</a>
 *
 * @author hylexus
 * Created At 2019-09-28 9:07 下午
 * @see ExtraMsgBody
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtraField {

    int DEFAULT_BYTE_COUNT_OF_MSG_ID = 1;
    int DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH = 1;

    /**
     * @return 赋值顺序，值越小优先级越高
     */
    int order() default Integer.MIN_VALUE + 1000;

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

    /**
     * 计划在 1.0.4-RELEASE中删除该属性，请使用 {@link ExtraMsgBody#byteCountOfMsgId() } 代替。
     * 详情见 https://github.com/hylexus/jt-framework/issues/8
     *
     * @return 附加消息中表示消息ID的字段字节数，例如BYTE --> 1, WORD --> 2 (808文档中默认为BYTE，即1字节)
     * @see ExtraMsgBody#byteCountOfMsgId()
     */
    @Deprecated
    int byteCountOfMsgId() default DEFAULT_BYTE_COUNT_OF_MSG_ID;

    /**
     * 计划在 1.0.4-RELEASE中删除该属性，请使用 {@link ExtraMsgBody#byteCountOfContentLength()} () } 代替。
     * 详情见 https://github.com/hylexus/jt-framework/issues/8
     *
     * @return 附加消息中表示消息长度的字段的字节数，例如BYTE --> 1, WORD --> 2 (808文档中默认为BYTE，即1字节)
     * @see ExtraMsgBody#byteCountOfContentLength()
     */
    @Deprecated
    int byteCountOfContentLength() default DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface NestedFieldMapping {

        /**
         * @return 附加消息的id
         */
        int msgId();

        /**
         * @return 附加消息对应的数据类型
         */
        MsgDataType dataType() default MsgDataType.BYTES;

        /**
         * @return 是不是嵌套的附加消息，如果是嵌套类型，那么嵌套类型应该被 {@link ExtraMsgBody} 注解修饰
         */
        boolean isNestedExtraField() default false;
    }
}
