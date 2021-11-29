package io.github.hylexus.jt.jt808.boot.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * @author hylexus
 */
public class Jt808DefaultEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String DEFAULT_JT_808_SERVER_CONFIG_PROPERTY_SOURCE_NAME = "default-jt808-server-config";

    private static final String resourcePattern = "classpath*:META-INF/default-jt808-server-config.*";
    private final YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
    private final PropertiesPropertySourceLoader propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();

    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        final ResourceLoader resourceLoader = getResourceLoader(application);

        try {
            processPropertySources(environment, resourceLoader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load environment by resource pattern : " + resourcePattern, e);
        }
    }

    public void processPropertySources(ConfigurableEnvironment environment, ResourceLoader resourceLoader) throws IOException {

        final PropertySource<?> propertySource = buildPropertySource(resourceLoader);

        environment.getPropertySources().addLast(propertySource);
    }

    private PropertySource<?> buildPropertySource(ResourceLoader resourceLoader) throws IOException {
        final CompositePropertySource compositePropertySource = new CompositePropertySource(DEFAULT_JT_808_SERVER_CONFIG_PROPERTY_SOURCE_NAME);
        final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        final Resource[] resources = resolver.getResources(resourcePattern);

        for (Resource resource : resources) {
            if (!resource.exists()) {
                continue;
            }
            final List<PropertySource<?>> parsedPropertySourceList = parseProperty(resource);
            if (parsedPropertySourceList != null) {
                parsedPropertySourceList.forEach(compositePropertySource::addPropertySource);
            }
        }

        return compositePropertySource;
    }

    private List<PropertySource<?>> parseProperty(Resource resource) throws IOException {
        final String filename = resource.getURI().toString().toLowerCase();
        if (filename.endsWith(".properties")) {
            return propertiesPropertySourceLoader.load(filename, resource);
        } else if (filename.endsWith(".yml") || filename.endsWith(".yaml")) {
            return yamlPropertySourceLoader.load(filename, resource);
        }
        return null;
    }

    private ResourceLoader getResourceLoader(SpringApplication application) {
        final ResourceLoader resourceLoader = application.getResourceLoader();
        if (resourceLoader != null) {
            return resourceLoader;
        }

        return new DefaultResourceLoader(application.getClassLoader());
    }
}
