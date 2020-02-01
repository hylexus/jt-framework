package io.github.hylexus.jt.spring.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hylexus
 * Created At 2019-09-22 5:49 下午
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class ClassScanner {

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    public Set<Class> doScan(Set<String> packagesToScan, Predicate<Class<?>> classFilter) throws IOException {
        String[] strings = packagesToScan.toArray(new String[packagesToScan.size()]);
        return doScan(strings, classFilter);
    }

    public Set<Class> doScan(String[] packagesToScan, Predicate<Class<?>> classFilter) throws IOException {
        Set<Class> ret = new HashSet<>();
        for (String packageName : packagesToScan) {
            if (StringUtils.isEmpty(packageName)) {
                continue;
            }
            Set<Class> classSet = this.doScan(packageName, classFilter);
            ret.addAll(classSet);
        }
        return ret;
    }

    public Set<Class> doScan(String basePackage, Predicate<Class<?>> classFilter) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                + "/**/*.class";

        Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);

        return Stream.of(resources)
                .filter(Resource::isReadable)
                .map(this::getMetadataReader)
                .filter(Objects::nonNull)
                .map(this::getClassName)
                .filter(Objects::nonNull)
                .filter(classFilter)
                .collect(Collectors.toSet());
    }

    private Class<?> getClassName(MetadataReader reader) {
        try {
            return Class.forName(reader.getClassMetadata().getClassName());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private MetadataReader getMetadataReader(Resource resource) {
        try {
            return this.metadataReaderFactory.getMetadataReader(resource);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
