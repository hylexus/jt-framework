package io.github.hylexus.jt808.samples.annotation.configuration.web;


import io.github.hylexus.jt808.samples.annotation.model.vo.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
public class ResponseWrapperResultHandler extends ResponseBodyResultHandler {
    private static final MethodParameter ACTUAL_RETURN_TYPE;

    static {
        try {
            ACTUAL_RETURN_TYPE = new MethodParameter(ResponseWrapperResultHandler.class.getDeclaredMethod("actualReturnType"), -1);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final ResponseEntityResultHandler responseEntityResultHandler;

    public ResponseWrapperResultHandler(
            List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver,
            ResponseEntityResultHandler responseEntityResultHandler) {
        super(writers, resolver);
        this.responseEntityResultHandler = responseEntityResultHandler;
    }

    private static Mono<Resp<Object>> actualReturnType() {
        return null;
    }

    @Override
    public boolean supports(@NonNull HandlerResult result) {
        return super.supports(result) && !(responseEntityResultHandler.supports(result));
    }

    @Override
    @NonNull
    public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, @NonNull HandlerResult result) {

        final MethodParameter originalType = result.getReturnTypeSource();
        final Object returnValue = result.getReturnValue();
        if (returnValue == null) {
            return this.writeBodyInternal(Resp.empty(), ACTUAL_RETURN_TYPE, exchange);
        }

        if (returnValue instanceof Resp) {
            return this.writeBodyInternal((Resp<?>) returnValue, originalType, exchange);
        } else if (returnValue instanceof Mono) {
            @SuppressWarnings("unchecked") final Mono<Object> mono = ((Mono<Object>) returnValue)
                    .switchIfEmpty(Mono.defer(() -> Mono.just(Resp.empty())));

            return mono.flatMap(value -> {
                if (value instanceof Resp) {
                    return this.writeBodyInternal((Resp<?>) value, originalType, exchange);
                }
                return wrapBodyAndThenWrite(value, exchange);
            });
        } else if (returnValue instanceof Flux) {
            return ((Flux<?>) returnValue).collectList()
                    .flatMap(value -> wrapBodyAndThenWrite(value, exchange));
        } else {
            return wrapBodyAndThenWrite(returnValue, exchange);
        }
    }


    private Mono<Void> wrapBodyAndThenWrite(Object value, ServerWebExchange exchange) {
        Resp<Object> wrappedBody = Resp.success(value);
        return writeBodyInternal(wrappedBody, ACTUAL_RETURN_TYPE, exchange);
    }

    private Mono<Void> writeBodyInternal(Resp<?> body, MethodParameter returnType, ServerWebExchange exchange) {
        return super.writeBody(Mono.just(body), returnType, exchange);
    }

}