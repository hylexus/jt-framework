package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.BuiltinRedisJt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.CompositeJt808ResponseSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808ResponseSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.impl.RedisJt808ResponseSubPackageCacheItem;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
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
public class Jt808SubPackageAutoConfiguration {

    //@Bean
    //@ConditionalOnMissingBean
    //public Jt808ResponseSubPackageStorage jt808ResponseSubPackageStorage() {
    //    return new DefaultJt808ResponseSubPackageStorage(1024, Duration.ofMinutes(10));
    //}

    @Bean(name = "builtinRedisJt808ResponseSubPackageStorage")
    @ConditionalOnMissingBean(name = "builtinRedisJt808ResponseSubPackageStorage")
    public RedisTemplate<String, RedisJt808ResponseSubPackageCacheItem> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, RedisJt808ResponseSubPackageCacheItem> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setHashKeySerializer(stringRedisSerializer);

        final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808ResponseSubPackageStorage jt808ResponseSubPackageStorage(
            @Autowired @Qualifier("builtinRedisJt808ResponseSubPackageStorage") RedisTemplate<String, RedisJt808ResponseSubPackageCacheItem> redisTemplate) {
        return new BuiltinRedisJt808ResponseSubPackageStorage(redisTemplate);
    }

    @Bean(name = "defaultJt808ResponseSubPackageEventListener")
    @ConditionalOnMissingBean(name = "defaultJt808ResponseSubPackageEventListener")
    public DefaultJt808ResponseSubPackageEventListener defaultJt808ResponseSubPackageEventListener(Jt808ResponseSubPackageStorage subPackageStorage) {
        return new DefaultJt808ResponseSubPackageEventListener(subPackageStorage);
    }

    @Bean
    @Primary
    public Jt808ResponseSubPackageEventListener jt808ResponseSubPackageEventListener(
            ObjectProvider<Jt808ResponseSubPackageEventListener> listeners) {
        final List<Jt808ResponseSubPackageEventListener> list = listeners.stream()
                .filter(it -> it.getClass() != CompositeJt808ResponseSubPackageEventListener.class)
                .sorted(Comparator.comparing(OrderedComponent::getOrder))
                .collect(Collectors.toList());
        return new CompositeJt808ResponseSubPackageEventListener(list);
    }

}
