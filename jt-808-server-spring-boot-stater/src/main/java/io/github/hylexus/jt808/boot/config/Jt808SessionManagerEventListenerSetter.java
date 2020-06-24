package io.github.hylexus.jt808.boot.config;

import io.github.hylexus.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt808.session.Jt808SessionManagerEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

/**
 * 将 {@link Jt808SessionManagerEventListener} 实例绑定到 {@link Jt808SessionManager}
 *
 * @author hylexus
 * @since 1.0.7
 */
@Slf4j
@Order(200)
public class Jt808SessionManagerEventListenerSetter implements CommandLineRunner {

    @Autowired
    private final Jt808SessionManager jt808SessionManager;

    @Autowired
    private final Jt808SessionManagerEventListener listener;

    public Jt808SessionManagerEventListenerSetter(Jt808SessionManager sessionManager, Jt808SessionManagerEventListener listener) {
        this.jt808SessionManager = sessionManager;
        this.listener = listener;
    }

    @Override
    public void run(String... args) throws Exception {
        jt808SessionManager.setEventListener(listener);
        log.info("Binding [{}] to [{}]", listener.getClass(), jt808SessionManager.getClass());
    }
}