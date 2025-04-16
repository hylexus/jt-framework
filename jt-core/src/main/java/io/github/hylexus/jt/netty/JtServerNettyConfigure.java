package io.github.hylexus.jt.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;

/**
 * @author hylexus
 */
public interface JtServerNettyConfigure {

    interface ConfigurationProvider {
        <T> T getConfiguration(String key, Class<T> targetType, T defaultValue);
    }

    /**
     * @param configProvider 可以从中读取配置项(环境变量、系统属性、application.yaml、...)
     * @return 返回入参中的 {@code serverBootstrap} 或 返回一个新的 {@link ServerBootstrap ServerBootstrap} 实例
     */
    ServerBootstrap configureServerBootstrap(ConfigurationProvider configProvider, ServerBootstrap serverBootstrap);

    void configureSocketChannel(ConfigurationProvider configProvider, SocketChannel ch);

}
