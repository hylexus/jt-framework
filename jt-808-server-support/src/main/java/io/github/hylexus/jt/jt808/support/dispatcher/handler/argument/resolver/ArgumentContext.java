package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author hylexus
 */
@Getter
@Setter
public class ArgumentContext {
    @Nullable
    private final Throwable throwable;

    @Nonnull
    private final Jt808ServerExchange exchange;

    public static ArgumentContext of(Jt808ServerExchange exchange, Throwable throwable) {
        return new ArgumentContext(exchange, throwable);
    }

    private ArgumentContext(@Nonnull Jt808ServerExchange exchange, @Nullable Throwable throwable) {
        this.exchange = exchange;
        this.throwable = throwable;
    }
}
