package io.github.hylexus.jt.jt1078.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;

public interface Jt1078RequestHandler extends OrderedComponent {

    boolean support(Jt1078Request request);

    void handle(Jt1078Request request);

    @Override
    default int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    @Slf4j
    class LoggingJt1078RequestHandler implements Jt1078RequestHandler {

        @Override
        public boolean support(Jt1078Request request) {
            return true;
        }

        @Override
        public void handle(Jt1078Request request) {
            if (log.isDebugEnabled()) {
                final Jt1078RequestHeader header = request.header();
                log.debug("---LoggingHandler---: sim={}, PT={}, dataType={}, body={}", request.sim(), header.payloadType(), header.dataType(),
                        HexStringUtils.byteBufToString(request.body()));
            }
        }
    }

}
