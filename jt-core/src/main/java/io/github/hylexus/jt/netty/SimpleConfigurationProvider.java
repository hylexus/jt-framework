package io.github.hylexus.jt.netty;

import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

public class SimpleConfigurationProvider implements JtServerNettyConfigure.ConfigurationProvider {
    private final Environment environment;

    public SimpleConfigurationProvider() {
        this.environment = new StandardEnvironment();
    }

    @Override
    public <T> T getConfiguration(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

}
