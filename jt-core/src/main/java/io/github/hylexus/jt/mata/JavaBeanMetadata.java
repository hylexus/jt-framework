package io.github.hylexus.jt.mata;

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
    private List<JavaBeanFieldMetadata> fieldMetadataList;
    private Map<String, JavaBeanFieldMetadata> fieldMapping;

    public Optional<JavaBeanFieldMetadata> getFieldMedataByName(String name) {
        return Optional.ofNullable(fieldMapping.get(name));
    }

}
