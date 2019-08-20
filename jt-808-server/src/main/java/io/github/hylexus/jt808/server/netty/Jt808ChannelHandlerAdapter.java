package io.github.hylexus.jt808.server.netty;

import io.github.hylexus.jt.jt808.support.RequestMsgDispatcher;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static io.netty.util.ReferenceCountUtil.release;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Slf4j
@ChannelHandler.Sharable
public class Jt808ChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private RequestMsgDispatcher msgDispatcher;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            final ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                return;
            }

            final byte[] unescaped = new byte[buf.readableBytes()];
            buf.readBytes(unescaped);
            log.info("unescaped:{}", HexStringUtils.bytes2HexString(unescaped));
            byte[] escaped = ProtocolUtils.doEscape4ReceiveJt808(unescaped, 0, unescaped.length);
            log.debug("escaped : {}", HexStringUtils.bytes2HexString(escaped));

            this.msgDispatcher.doDispatch(null);
        } finally {
            release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO 异常处理
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("remove session, channelInactive [Jt808ChannelHandlerAdapter]");
        // TODO remove session
        // SessionHolder.getInstance().remove(Session.generateSessionId(ctx.channel()));
        super.channelInactive(ctx);
    }

}
