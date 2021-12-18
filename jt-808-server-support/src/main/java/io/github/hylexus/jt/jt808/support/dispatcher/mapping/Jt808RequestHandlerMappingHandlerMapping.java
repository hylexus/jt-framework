package io.github.hylexus.jt.jt808.support.dispatcher.mapping;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerExecutionChain;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808RequestHandlerReporter;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author hylexus
 * @see Jt808RequestHandlerMapping
 */
public class Jt808RequestHandlerMappingHandlerMapping
        extends AbstractJt808HandlerMapping
        implements Jt808RequestHandlerReporter {

    private final MsgTypeParser msgTypeParser;
    private final ComponentMapping<HandlerMethod> msgHandlerComponentMapping;

    public Jt808RequestHandlerMappingHandlerMapping(
            ComponentMapping<HandlerMethod> msgHandlerComponentMapping,
            List<Jt808HandlerInterceptor> interceptors, MsgTypeParser msgTypeParser) {

        super(interceptors);
        this.msgHandlerComponentMapping = msgHandlerComponentMapping;
        this.msgTypeParser = msgTypeParser;
    }

    @Override
    public Optional<Jt808HandlerExecutionChain> getHandler(Jt808ServerExchange exchange) {
        final Jt808Request request = exchange.request();
        final Jt808Session session = exchange.session();
        return msgHandlerComponentMapping.getComponent(request.msgType(), request.header().version())
                .map(handler -> super.buildHandlerExecutionChain(request, session, handler));
    }

    @Override
    public Stream<RequestMappingReporter> report() {
        return this.msgHandlerComponentMapping.getMappings().entrySet().stream().flatMap(entry -> {
            final Integer msgId = entry.getKey();
            final MsgType msgType = msgTypeParser.parseMsgType(msgId).orElseThrow();
            return entry.getValue().entrySet().stream().map(it -> new RequestMappingReporter(msgType, it.getKey(), it.getValue(), it.getValue().getMethod()));
        });
    }
}