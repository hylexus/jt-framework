package io.github.hylexus.jt.jt1078.boot.configuration;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListener;
import io.github.hylexus.jt.jt1078.spec.impl.Jt1078RequestLifecycleListeners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Jt1078CommonAutoConfiguration {

    @Bean
    @Primary
    public Jt1078RequestLifecycleListener jt1078RequestLifecycleListener(List<Jt1078RequestLifecycleListener> listeners) {
        final List<Jt1078RequestLifecycleListener> sortedListeners = listeners.stream()
                .sorted(Comparator.comparing(OrderedComponent::getOrder))
                .collect(Collectors.toList());

        return new Jt1078RequestLifecycleListeners(sortedListeners);
    }

}
