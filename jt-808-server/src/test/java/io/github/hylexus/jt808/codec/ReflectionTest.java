package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.annotation.msg.BasicField;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt808.server.msg.req.LocationUploadMsgBody;
import org.junit.Test;

/**
 * @author hylexus
 * Created At 2019-09-28 7:16 下午
 */
public class ReflectionTest {

    @Test
    public void test1() throws Exception {
        Class<?> cls = LocationUploadMsgBody.class;
        JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);
        for (JavaBeanFieldMetadata javaBeanFieldMetadata : beanMetadata.getFieldMetadataList()) {
            String typeName = javaBeanFieldMetadata.getFieldType().getSimpleName();
            String fieldName = javaBeanFieldMetadata.getField().getName();
            boolean annotationPresent = javaBeanFieldMetadata.isAnnotationPresent(BasicField.class);
            System.out.println(typeName + " -- "
                    + javaBeanFieldMetadata.getGenericType()
                    + " -- " + fieldName
                    + " -- " + annotationPresent
            );
        }
    }

}
