package io.github.hylexus.jt808.samples.annotation.handler.interceptor;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * @author hylexus
 */
@Slf4j
@Component
public class DemoInterceptor02 implements Jt808HandlerInterceptor {

    @Override
    public boolean preHandle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        log.info("DemoInterceptor02#preHandle ... terminalId={}, msgType={}", exchange.request().terminalId(), exchange.request().msgType());
        return true;
    }

    @Override
    public void postHandle(Jt808ServerExchange exchange, Object handler, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
        log.info("DemoInterceptor01#postHandle ... terminalId={}, msgType={}, result={}",
                exchange.request().terminalId(), exchange.request().msgType(), handlerResult != null ? handlerResult.getReturnValue() : null);
    }

    @Override
    public void afterCompletion(Jt808ServerExchange exchange, Object handler, @Nullable Throwable exception) throws Throwable {
        log.info("DemoInterceptor01#afterCompletion ... terminalId={}, msgType={}, exception={}",
                exchange.request().terminalId(), exchange.request().msgType(), exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
