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

    public static ArgumentContext of(Jt808Request request, Jt808Session session, Throwable throwable) {
        return new ArgumentContext(request, session, throwable);
    }

    public static ArgumentContext of(Jt808Request request, Throwable throwable) {
        return new ArgumentContext(request, null, throwable);
    }

    private ArgumentContext(@Nullable Jt808Request request, @Nullable Jt808Session session, @Nullable Throwable throwable) {
        this.request = request;
        this.session = session;
        this.throwable = throwable;
    }
}
