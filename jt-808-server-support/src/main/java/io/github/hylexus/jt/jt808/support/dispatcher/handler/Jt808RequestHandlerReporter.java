package io.github.hylexus.jt.jt808.support.dispatcher.handler;

import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.MsgType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public interface Jt808RequestHandlerReporter {

    Stream<RequestMappingReporter> report();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RequestMappingReporter implements ReplaceableComponent {
        private MsgType msgType;
        private Jt808ProtocolVersion version;
        private Object handler;
        private Method handlerMethod;
        private int order;
    }
}
