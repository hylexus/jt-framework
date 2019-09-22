package io.github.hylexus.jt.annotation;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.DataTypeConverter;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-09-18 8:48 下午
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808Field {

    int startIndex() default 0;

    MsgDataType dataType();

    /**
     * 当 {@link #dataType()} == 0 时生效;
     * <p>
     * 优先使用 {@link #dataType()}
     *
     * @return 该字段的字节数
     * @see MsgDataType#getByteCount()
     */
    int length() default 0;

    Class<? extends DataTypeConverter> customerDataTypeConverterClass() default DataTypeConverter.NoOpsConverter.class;

}
