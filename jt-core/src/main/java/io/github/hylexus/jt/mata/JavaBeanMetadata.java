package io.github.hylexus.jt.mata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hylexus
 * Created At 2019-09-28 7:25 下午
 */
@Data
@Accessors(chain = true)
public class JavaBeanMetadata {
    private Class<?> originalClass;
    private List<JavaBeanFieldMetadata> fieldMetadataList;
}
