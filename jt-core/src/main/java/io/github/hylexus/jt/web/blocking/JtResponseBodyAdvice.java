package io.github.hylexus.jt.web.blocking;

import io.github.hylexus.jt.core.model.value.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class JtResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        if (body == null) {
            return Resp.notFound();
        } else if (body instanceof Resp) {
            return body;
        } else {
            return Resp.success(body);
        }
    }
}
