package io.github.hylexus.jt.jt808.support.dispatcher;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class Jt808HandlerResult {

    private static final Jt808HandlerResult EMPTY_RESULT = new Jt808HandlerResult();

    public static Jt808HandlerResult empty() {
        return EMPTY_RESULT;
    }

    public static boolean isEmptyResult(Jt808HandlerResult result) {
        return result == EMPTY_RESULT;
    }

    private Object handler;

    private Object returnValue;

    public static Jt808HandlerResult of(Object handler, Object returnValue) {
        return new Jt808HandlerResult().setHandler(handler).setReturnValue(returnValue);
    }

}