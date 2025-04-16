package io.github.hylexus.jt.netty;

import org.springframework.core.env.Environment;

public class SpringEnvironmentConfigurationProvider implements JtServerNettyConfigure.ConfigurationProvider {
    private final Environment environment;

    public SpringEnvironmentConfigurationProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <T> T getConfiguration(String key, Class<T> targetType, T defaultValue) {
        return this.environment.getProperty(key, targetType, defaultValue);
    }

}
