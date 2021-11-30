package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeaderSpec;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.exception.netty.Jt808NettyException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt808.session.SessionCloseReason.CHANNEL_INACTIVE;
import static io.github.hylexus.jt.jt808.session.SessionCloseReason.SERVER_EXCEPTION_OCCURRED;
import static io.netty.util.ReferenceCountUtil.release;

/**
 * @author hylexus
 * @author lirenhao
 * @see Jt808DecodeChannelHandlerAdapter
 **/
@Slf4j(topic = "jt-808.channel.dispatcher")
@ChannelHandler.Sharable
public class Jt808DispatchChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final RequestMsgDispatcher msgDispatcher;
    private final Jt808SessionManager sessionManager;
    private final Jt808ExceptionHandler commonExceptionHandler;

    public Jt808DispatchChannelHandlerAdapter(
            Jt808SessionManager sessionManager,
            RequestMsgDispatcher msgDispatcher, Jt808ExceptionHandler commonExceptionHandler) {

        this.sessionManager = sessionManager;
        this.msgDispatcher = msgDispatcher;
        this.commonExceptionHandler = commonExceptionHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Jt808Session jt808Session = null;
        Jt808Request request = null;
        try {
            if (!(msg instanceof Jt808Request)) {
                return;
            }

            request = (Jt808Request) msg;
            final Jt808MsgHeaderSpec header = request.header();
            final String terminalId = header.terminalId();
            jt808Session = sessionManager.persistenceIfNecessary(terminalId, header.version(), ctx.channel());
            log.debug("[decode] : {}, terminalId={}, msg = {}", request.msgType(), terminalId, request);

            this.msgDispatcher.doDispatch(request);
        } catch (Throwable e) {
            try {
                commonExceptionHandler.handleException(null, ArgumentContext.of(request, jt808Session, new Jt808NettyException(e)));
            } catch (Throwable ex) {
                log.error("An error occurred while invoke ExceptionHandler", ex);
            }
        } finally {
//            release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), SERVER_EXCEPTION_OCCURRED);
        log.error("[exceptionCaught]", cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), CHANNEL_INACTIVE);
    }

}
