package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808RequestSubPackageStorageEnabled;
import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808ResponseSubPackageStorageEnabled;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.builtin.RequestSubPackageStorageProps;
import io.github.hylexus.jt.jt808.boot.props.builtin.ResponseSubPackageStorageProps;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.*;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Slf4j
@Import({
        // request
        Jt808SubPackageAutoConfiguration.NoOpsRequestSubPackageStorageAutoConfiguration.class,
        Jt808SubPackageAutoConfiguration.CaffeineRequestSubPackageStorageAutoConfiguration.class,
        // response
        Jt808SubPackageAutoConfiguration.NoOpsResponseSubPackageStorageAutoConfiguration.class,
        Jt808SubPackageAutoConfiguration.CaffeineResponseSubPackageStorageAutoConfiguration.class,
        Jt808SubPackageAutoConfiguration.RedisResponseSubPackageStorageAutoConfiguration.class,
})
public class Jt808SubPackageAutoConfiguration {

    @ConditionalOnMissingBean(Jt808RequestSubPackageStorage.class)
    @ConditionalOnJt808RequestSubPackageStorageEnabled(type = RequestSubPackageStorageProps.Type.NONE)
    public static class NoOpsRequestSubPackageStorageAutoConfiguration {

        public NoOpsRequestSubPackageStorageAutoConfiguration() {
            log.error("\n\nI hope you know what you have configured!"
                      + "\nWhen jt808.request-sub-package-storage.type = 'NONE', it means that all Sub-Request-Message will be IGNORED!!!\n"
                      + "\nWhen jt808.request-sub-package-storage.type = 'NONE', it means that all Sub-Request-Message will be IGNORED!!!\n"
                      + "\nWhen jt808.request-sub-package-storage.type = 'NONE', it means that all Sub-Request-Message will be IGNORED!!!\n\n"
            );
        }

        @Bean
        public Jt808RequestSubPackageStorage jt808RequestSubPackageStorage() {
            return Jt808RequestSubPackageStorage.NO_OPS;
        }
    }

    @ConditionalOnMissingBean(Jt808RequestSubPackageStorage.class)
    @ConditionalOnJt808RequestSubPackageStorageEnabled(type = RequestSubPackageStorageProps.Type.CAFFEINE)
    public static class CaffeineRequestSubPackageStorageAutoConfiguration {
        private final Jt808ServerProps storageProps;

        public CaffeineRequestSubPackageStorageAutoConfiguration(Jt808ServerProps storageProps) {
            this.storageProps = storageProps;
        }

        @Bean
        public Jt808RequestSubPackageStorage jt808RequestSubPackageStorage() {
            final CaffeineJt808RequestSubPackageStorage.StorageConfig caffeineCacheConfig = storageProps.getRequestSubPackageStorage().getCaffeine();
            final CaffeineJt808RequestSubPackageStorage storage = new CaffeineJt808RequestSubPackageStorage(ByteBufAllocator.DEFAULT, caffeineCacheConfig);
            // 这里有个循环依赖
            //storage.setRequestMsgDispatcher(requestMsgDispatcher);
            return storage;
        }

        @Bean
        public Jt808RequestSubPackageStoragePropertySetter jt808RequestSubPackageStoragePropertySetter(ApplicationContext applicationContext) {
            return new Jt808RequestSubPackageStoragePropertySetter(applicationContext);
        }
    }

    @Slf4j
    static class Jt808RequestSubPackageStoragePropertySetter {
        public Jt808RequestSubPackageStoragePropertySetter(ApplicationContext applicationContext) {
            this.doBind(applicationContext);
        }

        private void doBind(ApplicationContext applicationContext) {
            final Jt808RequestSubPackageStorage singleton = applicationContext.getBean(Jt808RequestSubPackageStorage.class);
            if (singleton instanceof CaffeineJt808RequestSubPackageStorage) {
                final Jt808RequestMsgDispatcher dispatcher = applicationContext.getBean(Jt808RequestMsgDispatcher.class);
                ((CaffeineJt808RequestSubPackageStorage) singleton).setRequestMsgDispatcher(dispatcher);
                log.info("--> Binding [{}] to [{}]", dispatcher.getClass().getName(), singleton.getClass().getName());
            }
        }
    }

    @ConditionalOnMissingBean(Jt808ResponseSubPackageStorage.class)
    @ConditionalOnJt808ResponseSubPackageStorageEnabled(type = ResponseSubPackageStorageProps.Type.NONE)
    public static class NoOpsResponseSubPackageStorageAutoConfiguration {
        public NoOpsResponseSubPackageStorageAutoConfiguration() {
            log.error("\n\nI hope you know what you have configured."
                      + "\nWhen jt808.response-sub-package-storage.type = 'NONE', it means that the server-side will not temporarily store any "
                      + "Response-Sub-Package, "
                      + "and you may NOT be able to process message with id = 0X0005.\n\n");
        }

        @Bean
        public Jt808ResponseSubPackageStorage jt808ResponseSubPackageStorage() {
            return Jt808ResponseSubPackageStorage.NO_OPS_STORAGE;
        }
    }

    @ConditionalOnMissingBean(Jt808ResponseSubPackageStorage.class)
    @ConditionalOnJt808ResponseSubPackageStorageEnabled(type = ResponseSubPackageStorageProps.Type.CAFFEINE)
    public static class CaffeineResponseSubPackageStorageAutoConfiguration {
        @Bean
        public Jt808ResponseSubPackageStorage jt808ResponseSubPackageStorage(Jt808ServerProps serverProps) {
            return new CaffeineJt808ResponseSubPackageStorage(serverProps.getResponseSubPackageStorage().getCaffeine());
        }
    }

    @ConditionalOnMissingBean(Jt808ResponseSubPackageStorage.class)
    @ConditionalOnJt808ResponseSubPackageStorageEnabled(type = ResponseSubPackageStorageProps.Type.REDIS)
    public static class RedisResponseSubPackageStorageAutoConfiguration {
        @Bean
        public Jt808ResponseSubPackageStorage jt808ResponseSubPackageStorage(
                Jt808ServerProps storageProps,
                @Autowired @Qualifier("builtinRedisJt808ResponseSubPackageStorage") RedisTemplate<String, Object> redisTemplate) {

            final RedisJt808ResponseSubPackageStorage.StorageConfig packageStorageProps = storageProps.getResponseSubPackageStorage().getRedis();
            return new RedisJt808ResponseSubPackageStorage(ByteBufAllocator.DEFAULT, packageStorageProps, redisTemplate);
        }

        @Bean(name = "builtinRedisJt808ResponseSubPackageStorage")
        @ConditionalOnMissingBean(name = "builtinRedisJt808ResponseSubPackageStorage")
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            final RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);

            final Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(RedisJt808ResponseSubPackageCacheItem.class);
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
            final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            template.setKeySerializer(stringRedisSerializer);
            template.setHashKeySerializer(stringRedisSerializer);
            template.setHashValueSerializer(jackson2JsonRedisSerializer);
            return template;
        }
    }

    @Bean
    @Primary
    public Jt808RequestSubPackageEventListener jt808RequestSubPackageEventListener(List<Jt808RequestSubPackageEventListener> listeners) {
        final List<Jt808RequestSubPackageEventListener> list = listeners.stream()
                .filter(it -> it.getClass() != CompositeJt808RequestSubPackageEventListener.class)
                .sorted(Comparator.comparing(OrderedComponent::getOrder))
                .collect(Collectors.toList());
        return new CompositeJt808RequestSubPackageEventListener(list);
    }

    @Bean
    @Primary
    public Jt808ResponseSubPackageEventListener jt808ResponseSubPackageEventListener(List<Jt808ResponseSubPackageEventListener> listeners) {
        final List<Jt808ResponseSubPackageEventListener> list = listeners.stream()
                .filter(it -> it.getClass() != CompositeJt808ResponseSubPackageEventListener.class)
                .sorted(Comparator.comparing(OrderedComponent::getOrder))
                .collect(Collectors.toList());
        return new CompositeJt808ResponseSubPackageEventListener(list);
    }
}
