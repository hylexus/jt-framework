package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt808.session.SessionCloseReason.CHANNEL_INACTIVE;
import static io.github.hylexus.jt.jt808.session.SessionCloseReason.SERVER_EXCEPTION_OCCURRED;

/**
 * @author hylexus
 * @author lirenhao
 **/
@Slf4j(topic = "jt-808.channel.dispatcher")
@ChannelHandler.Sharable
public class Jt808DispatchChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final Jt808ProtocolVersion protocolVersion;
    private final Jt808MsgDecoder decoder;
    private final Jt808RequestMsgDispatcher msgDispatcher;
    private final Jt808SessionManager sessionManager;
    private final Jt808ExceptionHandler commonExceptionHandler;

    public Jt808DispatchChannelHandlerAdapter(
            Jt808ProtocolVersion protocolVersion, Jt808MsgDecoder decoder, Jt808SessionManager sessionManager,
            Jt808RequestMsgDispatcher msgDispatcher, Jt808ExceptionHandler commonExceptionHandler) {
        this.protocolVersion = protocolVersion;
        this.decoder = decoder;

        this.sessionManager = sessionManager;
        this.msgDispatcher = msgDispatcher;
        this.commonExceptionHandler = commonExceptionHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Jt808Session jt808Session = null;
        Jt808Request request = null;

        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }
                request = decoder.decode(protocolVersion, buf);
                final Jt808MsgHeader header = request.header();
                final String terminalId = header.terminalId();
                jt808Session = sessionManager.persistenceIfNecessary(terminalId, header.version(), ctx.channel());

                if (log.isDebugEnabled()) {
                    log.debug("[decode] : {}, terminalId={}, msg = {}", request.msgType(), terminalId, request);
                }

                this.msgDispatcher.doDispatch(request);
            } catch (Throwable throwable) {
                try {
                    // TODO exception handler ...
                    log.error("",throwable);
                    // commonExceptionHandler.handleException(null, ArgumentContext.of(request, jt808Session, new Jt808NettyException(throwable)));
                } catch (Throwable e) {
                    log.error("An error occurred while invoke ExceptionHandler", e);
                }
            }
        } else {
            ctx.fireChannelRead(msg);
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
