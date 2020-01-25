package io.github.hylexus.jt.mata;

import io.github.hylexus.jt.annotation.msg.req.slice.SlicedFrom;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-28 7:25 下午
 */
@Data
@Accessors(chain = true)
public class JavaBeanMetadata {
    private Class<?> originalClass;
    /**
     * fieldMetadata
     */
    private List<JavaBeanFieldMetadata> fieldMetadataList;
    /**
     * fieldName --> fieldMetadata
     */
    private Map<String, JavaBeanFieldMetadata> fieldMapping;
    /**
     * Used by {@link SlicedFrom} annotation
     */
    private List<JavaBeanFieldMetadata> sliceFromSupportedFieldList;

    public Optional<JavaBeanFieldMetadata> findFieldMedataByName(String name) {
        return Optional.ofNullable(fieldMapping.get(name));
    }

}
