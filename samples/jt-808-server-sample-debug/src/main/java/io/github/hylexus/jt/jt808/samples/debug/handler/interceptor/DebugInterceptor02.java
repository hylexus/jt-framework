package io.github.hylexus.jt.jt808.samples.debug.handler.interceptor;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.interceptor.Jt808HandlerInterceptorMatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.interceptor.MatchableHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * @author hylexus
 */
//@Component
@Slf4j
public class DebugInterceptor02 implements MatchableHandlerInterceptor {

    @Override
    public boolean preHandle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        final Jt808Request request = exchange.request();
        log.info("---> preHandle: terminalId={}, msgId={}", request.header().terminalId(), request.msgType());
        return true;
    }

    @Override
    public void postHandle(Jt808ServerExchange exchange, Object handler, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
        final Jt808Request request = exchange.request();
        log.info("---> postHandle: terminalId={}, msgId={}, result={}",
                request.header().terminalId(), request.msgType(),
                handlerResult != null ? handlerResult.getReturnValue() : null);
    }

    @Override
    public void afterCompletion(Jt808ServerExchange exchange, Object handler, @Nullable Throwable exception) throws Throwable {
        final Jt808Request request = exchange.request();
        log.info("---> afterCompletion: terminalId={}, msgId={}, exception={}", request.header().terminalId(), request.msgType(), exception);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public Jt808HandlerInterceptorMatcher requestMatcher() {
        return ((request, session) -> request.version() == Jt808ProtocolVersion.VERSION_2019);
    }
}
