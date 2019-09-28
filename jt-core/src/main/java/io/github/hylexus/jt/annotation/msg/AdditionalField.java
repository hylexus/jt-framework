package io.github.hylexus.jt.annotation.msg;

import com.google.common.collect.Sets;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import io.github.hylexus.jt.data.msg.AdditionalInfo;
import io.github.hylexus.jt.data.msg.BuiltinAdditionalInfo;

import java.lang.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-28 9:07 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdditionalField {

    Set<Class<?>> SUPPORTED_TARGET_CLASS = Sets.newHashSet(List.class, Map.class);

    int startIndex();

    int length() default 0;

    String byteCountMethod() default "";

    Class<? extends DataTypeConverter> customerDataTypeConverterClass() default DataTypeConverter.NoOpsConverter.class;

    Class<? extends AdditionalInfo> entityClass() default BuiltinAdditionalInfo.class;
}
