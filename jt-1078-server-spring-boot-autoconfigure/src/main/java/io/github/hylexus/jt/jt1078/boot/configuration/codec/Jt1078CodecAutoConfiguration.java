package io.github.hylexus.jt.jt1078.boot.configuration.codec;


import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinJt1078SessionCloseListener;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.codec.impl.DefaultJt1078MsgDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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
    @ConditionalOnMissingBean
    public Jt1078SessionManager jt1078SessionManager(Jt1078Publisher publisher) {
        return new DefaultJt1078SessionManager(manager -> manager.addListener(new BuiltinJt1078SessionCloseListener(publisher)));
    }

}
