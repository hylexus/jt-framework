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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
        public Jt808RequestSubPackageStorage jt808RequestSubPackageStorage(Jt808RequestMsgDispatcher requestMsgDispatcher) {
            final CaffeineJt808RequestSubPackageStorage.StorageConfig caffeineCacheConfig = storageProps.getRequestSubPackageStorage().getCaffeine();
            return new CaffeineJt808RequestSubPackageStorage(ByteBufAllocator.DEFAULT, requestMsgDispatcher, caffeineCacheConfig);
        }
    }

    @ConditionalOnMissingBean(Jt808ResponseSubPackageStorage.class)
    @ConditionalOnJt808ResponseSubPackageStorageEnabled(type = ResponseSubPackageStorageProps.Type.NONE)
    public static class NoOpsResponseSubPackageStorageAutoConfiguration {
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

            final var packageStorageProps = storageProps.getResponseSubPackageStorage().getRedis();
            return new RedisJt808ResponseSubPackageStorage(ByteBufAllocator.DEFAULT, packageStorageProps, redisTemplate);
        }

        @Bean(name = "builtinRedisJt808ResponseSubPackageStorage")
        @ConditionalOnMissingBean(name = "builtinRedisJt808ResponseSubPackageStorage")
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            final RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);

            final var jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(RedisJt808ResponseSubPackageCacheItem.class);
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
