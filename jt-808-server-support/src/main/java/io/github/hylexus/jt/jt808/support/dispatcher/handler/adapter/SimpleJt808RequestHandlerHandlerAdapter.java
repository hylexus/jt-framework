package io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.MultipleVersionSupport;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808RequestHandlerReporter;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.SimpleJt808RequestHandlerHandlerMapping;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public class SimpleJt808RequestHandlerHandlerAdapter
        implements Jt808HandlerAdapter, Jt808RequestHandlerReporter, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Override
    public boolean supports(Object handler) {
        return handler instanceof SimpleJt808RequestHandler;
    }

    @Override
    public Jt808HandlerResult handle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        final SimpleJt808RequestHandler<?> reqMsgHandler = (SimpleJt808RequestHandler<?>) handler;
        final Object result = reqMsgHandler.handleMsg(exchange);
        if (result == null) {
            return Jt808HandlerResult.empty();
        }
        return Jt808HandlerResult.of(handler, result);
    }

    @Override
    public int getOrder() {
        return SimpleJt808RequestHandlerHandlerMapping.ORDER;
    }

    @Override
    public Stream<RequestMappingReporter> report() {
        return applicationContext
                .getBeansOfType(SimpleJt808RequestHandler.class)
                .values()
                .stream()
                .flatMap(handler -> ((MultipleVersionSupport) handler).getSupportedMsgTypes()
                        .stream()
                        .flatMap(msgType -> ((MultipleVersionSupport) handler).getSupportedVersions()
                                .stream()
                                .map(version -> {
                                    final Method method;
                                    try {
                                        method = handler.getClass().getMethod("handleMsg", Jt808ServerExchange.class);
                                    } catch (NoSuchMethodException e) {
                                        throw new JtIllegalStateException(e);
                                    }
                                    return new RequestMappingReporter(msgType, version, handler, method, handler.getOrder());
                                })
                        )
                );
    }

}
