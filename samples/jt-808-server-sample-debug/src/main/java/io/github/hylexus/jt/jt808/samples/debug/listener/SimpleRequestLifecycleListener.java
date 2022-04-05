package io.github.hylexus.jt.jt808.samples.debug.listener;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hylexus
 */
@Component
@Slf4j
public class SimpleRequestLifecycleListener implements Jt808RequestLifecycleListener {

    private static final String LOG_PREFIX = "<><><><> ";

    @Override
    public boolean beforeDecode(ByteBuf request, Channel channel) {
        log.info("{} 1. [beforeDecode], hexString = [{}], channelId = {}", LOG_PREFIX, HexStringUtils.byteBufToString(request), channel.id());
        return true;
    }

    @Override
    public boolean beforeDispatch(Jt808Request request) {
        log.info("{} 2. [beforeDispatch], header = [{}]", LOG_PREFIX, request.header());
        return true;
    }

    @Override
    public boolean beforeDispatch(Jt808ServerExchange exchange) {
        log.info("{} 3. [beforeDispatch], session = {}", LOG_PREFIX, exchange.session());
        return true;
    }

    @Override
    public boolean beforeHandle(Jt808ServerExchange exchange, Object handler) {
        log.info("{} 4. [beforeHandle], handler = [{}]", LOG_PREFIX, handler);
        return true;
    }

    @Override
    public boolean beforeEncode(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult) {
        log.info("{} 5. [beforeEncode], returnValue = [{}]", LOG_PREFIX, handlerResult.getReturnValue());
        return true;
    }

    @Override
    public boolean beforeResponse(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult, ByteBuf response) {
        log.info("{} 6. [beforeResponse], hexString = [{}]", LOG_PREFIX, HexStringUtils.byteBufToString(response));
        return true;
    }
}
