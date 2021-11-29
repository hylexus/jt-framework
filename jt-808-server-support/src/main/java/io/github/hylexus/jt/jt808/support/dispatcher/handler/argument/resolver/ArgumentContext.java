package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class ArgumentContext {
    @Nullable
    private final Throwable throwable;

    @Nullable
    private final Jt808Request request;

    @Nullable
    private final Jt808Session session;

    public static ArgumentContext of(Throwable throwable, Jt808Request request, Jt808Session session) {
        return new ArgumentContext(throwable, request, session);
    }

    public static ArgumentContext of(Throwable throwable, Jt808Request request) {
        return new ArgumentContext(throwable, request, null);
    }

    public ArgumentContext(@Nullable Throwable throwable, @Nullable Jt808Request request, @Nullable Jt808Session session) {
        this.request = request;
        this.session = session;
        this.throwable = throwable;
    }
}
