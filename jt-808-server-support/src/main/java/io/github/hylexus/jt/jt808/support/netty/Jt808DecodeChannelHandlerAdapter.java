package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.exception.netty.Jt808NettyException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * date: 2020/7/10 5:37 下午
 *
 * @author lirenhao
 * @author hylexus
 */
@Slf4j(topic = "jt-808.channel.decode")
@ChannelHandler.Sharable
public class Jt808DecodeChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final Jt808ProtocolVersion protocolVersion;
    private final Jt808MsgDecoder decoder;
    private final Jt808ExceptionHandler exceptionHandler;

    public Jt808DecodeChannelHandlerAdapter(Jt808ProtocolVersion protocolVersion, Jt808MsgDecoder decoder, Jt808ExceptionHandler exceptionHandler) {
        this.protocolVersion = protocolVersion;
        this.decoder = decoder;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            Jt808Request request = null;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }

                request = decoder.decode(protocolVersion, new Jt808ByteBuf(buf));
                ctx.fireChannelRead(request);
            } catch (Throwable throwable) {
                try {
                    this.exceptionHandler.handleException(null, ArgumentContext.of(request, new Jt808NettyException(throwable)));
                } catch (Throwable e) {
                    log.error("An error occurred while invoke ExceptionHandler", e);
                }
            } finally {
//                buf.release();
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
