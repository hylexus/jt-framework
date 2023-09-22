package io.github.hylexus.jt.jt1078.boot.configuration.codec;


import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionEventListener;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078TerminalIdConverter;
import io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinJt1078SessionCloseListener;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.codec.impl.DefaultJt1078MsgDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Comparator;

/**
 * @author hylexus
 */
public class Jt1078CodecAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jt1078MsgDecoder jt1078MsgDecoder() {
        return new DefaultJt1078MsgDecoder();
    }


    @Bean
    public Jt1078SessionEventListener builtinJt1078SessionCloseListener(Jt1078Publisher publisher) {
        return new BuiltinJt1078SessionCloseListener(publisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078SessionManager jt1078SessionManager(ObjectProvider<Jt1078SessionEventListener> listeners, Jt1078TerminalIdConverter jt1078TerminalIdConverter) {
        final Jt1078SessionManager manager = new DefaultJt1078SessionManager(jt1078TerminalIdConverter);
        listeners.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).forEach(manager::addListener);
        return manager;
    }

}
