package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collections;

/**
 * @author hylexus
 * @see io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinJt1078SessionCloseListener#onSessionClose(Jt1078Session, Jt1078SessionCloseReason)
 */
public interface Jt1078Publisher {

    Jt1078TerminalIdConverter terminalIdConverter();

    /**
     * 当客户端断开时会给下游发送 {@link Jt1078SessionDestroyException} 异常信号, 订阅者应该处理这个异常
     *
     * @see io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinJt1078SessionCloseListener#onSessionClose(Jt1078Session, Jt1078SessionCloseReason)
     */
    default <S extends Jt1078Subscription> Flux<S> subscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(cls, sim, channelNumber, timeout).dataStream();
    }

    default <S extends Jt1078Subscription> Flux<S> subscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        return this.doSubscribe(cls, creator).dataStream();
    }

    /**
     * 当客户端断开时会给下游发送 {@link Jt1078SessionDestroyException} 异常信号, 订阅者应该处理这个异常
     *
     * @see io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinJt1078SessionCloseListener#onSessionClose(Jt1078Session, Jt1078SessionCloseReason)
     */
    default <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(cls, Jt1078SubscriberCreator.builder().sim(sim).channelNumber(channelNumber).timeout(timeout).metadata(Collections.emptyMap()).build());
    }

    <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator);

    /**
     * @param id {@link Jt1078Subscriber#id()}
     * @see #doSubscribe(Class, String, short, Duration)
     */
    default void unsubscribe(String id) {
        this.unsubscribe(id, null);
    }

    /**
     * @param id     {@link Jt1078Subscriber#id()}
     * @param reason 如果 {@code reason} 为空，发送 {@link reactor.core.publisher.SignalType#ON_COMPLETE ON_COMPLETE} 信号以表示当前订阅终止。
     *               否则发送 {@link reactor.core.publisher.SignalType#ON_ERROR ON_ERROR} 信号以表示当前订阅终止。
     * @see #doSubscribe(Class, String, short, Duration)
     */
    void unsubscribe(String id, @Nullable Jt1078SubscriberCloseException reason);

    /**
     * @param sim {@link Jt1078Request#sim()}
     */
    default void unsubscribeWithSim(String sim) {
        this.unsubscribeWithSim(terminalIdConverter().convert(sim), null);
    }

    /**
     * @param sim    {@link Jt1078Request#sim()}
     * @param reason 如果 {@code reason} 为空，发送 {@link reactor.core.publisher.SignalType#ON_COMPLETE ON_COMPLETE} 信号以表示当前订阅终止。
     *               否则发送 {@link reactor.core.publisher.SignalType#ON_ERROR ON_ERROR} 信号以表示当前订阅终止。
     */
    void unsubscribeWithSim(String sim, @Nullable Jt1078SubscriberCloseException reason);

    default void unsubscribeWithSimAndChannelNumber(String sim, short channelNumber) {
        this.unsubscribeWithSimAndChannelNumber(terminalIdConverter().convert(sim), channelNumber, null);
    }

    void unsubscribeWithSimAndChannelNumber(String sim, short channelNumber, @Nullable Jt1078SubscriberCloseException reason);

}
