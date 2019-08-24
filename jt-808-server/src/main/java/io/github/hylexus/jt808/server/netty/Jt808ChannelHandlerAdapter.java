package io.github.hylexus.jt808.server.netty;

import io.github.hylexus.jt.jt808.codec.Decoder;
import io.github.hylexus.jt.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt.jt808.msg.MsgType;
import io.github.hylexus.jt.jt808.session.SessionManager;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static io.github.hylexus.jt.jt808.session.Session.generateSessionId;
import static io.github.hylexus.jt.jt808.session.SessionCloseReason.CLIENT_ABORT;
import static io.github.hylexus.jt.jt808.session.SessionCloseReason.SERVER_EXCEPTION_OCCURRED;
import static io.netty.util.ReferenceCountUtil.release;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Slf4j
@ChannelHandler.Sharable
public class Jt808ChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private Decoder decoder = new Decoder();
    private RequestMsgDispatcher msgDispatcher;
    private MsgTypeParser msgTypeParser;

    public Jt808ChannelHandlerAdapter(RequestMsgDispatcher msgDispatcher, MsgTypeParser msgTypeParser) {
        this.msgDispatcher = msgDispatcher;
        this.msgTypeParser = msgTypeParser;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            final ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                return;
            }

            final byte[] unescaped = new byte[buf.readableBytes()];
            buf.readBytes(unescaped);
            log.debug("\nreceived msg:");
            log.debug("unescaped:{}", HexStringUtils.bytes2HexString(unescaped));
            byte[] escaped = ProtocolUtils.doEscape4ReceiveJt808(unescaped, 0, unescaped.length);
            log.debug("escaped : {}", HexStringUtils.bytes2HexString(escaped));

            AbstractRequestMsg abstractMsg = decoder.parseAbstractMsg(escaped);
            int msgId = abstractMsg.getHeader().getMsgId();
            final Optional<MsgType> msgType = this.msgTypeParser.parseMsgType(msgId);
            if (!msgType.isPresent()) {
                log.warn("received unknown msg, msgId={}({}). ignore.", msgId, HexStringUtils.int2HexString(msgId, 4));
                return;
            }
            abstractMsg.setMsgType(msgType.get());
            final String terminalId = abstractMsg.getHeader().getTerminalId();

            SessionManager.getInstance().persistenceIfNecessary(terminalId, ctx.channel());

            log.debug("[HandlerAdapter] receive msg {}, terminalId={}, msg = {}", msgType.get(), terminalId, abstractMsg);
            this.msgDispatcher.doDispatch(abstractMsg);
        } finally {
            release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO 异常处理
        SessionManager.getInstance().removeBySessionIdAndClose(generateSessionId(ctx.channel()), SERVER_EXCEPTION_OCCURRED);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("remove session, channelInactive [Jt808ChannelHandlerAdapter]");
        SessionManager.getInstance().removeBySessionIdAndClose(generateSessionId(ctx.channel()), CLIENT_ABORT);
        super.channelInactive(ctx);
    }

}
