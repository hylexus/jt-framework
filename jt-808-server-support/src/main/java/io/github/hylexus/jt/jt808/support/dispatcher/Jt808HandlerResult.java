package io.github.hylexus.jt.jt808.support.dispatcher;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.function.Function;

@Setter
@Getter
@Accessors(chain = true)
public class Jt808HandlerResult {

    private Object handler;

    @Nullable
    private Object returnValue;

    private boolean requestProcessed = false;

    private Function<Throwable, Jt808HandlerResult> exceptionHandler;

    public Jt808HandlerResult handleException(Throwable throwable) {
        if (this.exceptionHandler != null) {
            return exceptionHandler.apply(throwable);
        }
        throw new RuntimeException(throwable);
    }
}