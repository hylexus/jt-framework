package io.github.hylexus.jt808.support.netty;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.codec.Decoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RequestMsgWrapper;
import io.github.hylexus.jt808.session.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static io.github.hylexus.jt808.session.Session.generateSessionId;
import static io.github.hylexus.jt808.session.SessionCloseReason.CHANNEL_INACTIVE;
import static io.github.hylexus.jt808.session.SessionCloseReason.SERVER_EXCEPTION_OCCURRED;
import static io.netty.util.ReferenceCountUtil.release;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Slf4j
@ChannelHandler.Sharable
public class Jt808ChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final Decoder decoder;
    private final RequestMsgDispatcher msgDispatcher;
    private final MsgTypeParser msgTypeParser;
    private final BytesEncoder bytesEncoder;

    public Jt808ChannelHandlerAdapter(RequestMsgDispatcher msgDispatcher, MsgTypeParser msgTypeParser, BytesEncoder bytesEncoder) {
        this.decoder = new Decoder();
        this.msgDispatcher = msgDispatcher;
        this.msgTypeParser = msgTypeParser;
        this.bytesEncoder = bytesEncoder;
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
            log.debug("\nreceive msg:");
            log.debug(">>>>>>>>>>>>>>> : {}", HexStringUtils.bytes2HexString(unescaped));
            // final byte[] escaped = ProtocolUtils.doEscape4ReceiveJt808Msg(unescaped, 0, unescaped.length);
            final byte[] escaped = this.bytesEncoder.doEscapeForReceive(unescaped, 0, unescaped.length);
            log.debug("[escaped] : {}", HexStringUtils.bytes2HexString(escaped));

            final RequestMsgMetadata metadata = decoder.parseMsgMetadata(escaped);
            final RequestMsgHeader header = metadata.getHeader();
            final int msgId = header.getMsgId();
            final Optional<MsgType> msgType = this.msgTypeParser.parseMsgType(msgId);
            if (!msgType.isPresent()) {
                log.warn("received unknown msg, msgId = {}({}). ignore.", msgId, HexStringUtils.int2HexString(msgId, 4));
                return;
            }
            metadata.setMsgType(msgType.get());

            final String terminalId = header.getTerminalId();
            SessionManager.getInstance().persistenceIfNecessary(terminalId, ctx.channel());
            log.debug("[decode] : {}, terminalId={}, msg = {}", msgType.get(), terminalId, metadata);

            RequestMsgWrapper requestMsgWrapper = new RequestMsgWrapper().setMetadata(metadata);
            this.msgDispatcher.doDispatch(requestMsgWrapper);
        } finally {
            release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SessionManager.getInstance().removeBySessionIdAndClose(generateSessionId(ctx.channel()), SERVER_EXCEPTION_OCCURRED);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("remove session, channelInactive [Jt808ChannelHandlerAdapter]");
        SessionManager.getInstance().removeBySessionIdAndClose(generateSessionId(ctx.channel()), CHANNEL_INACTIVE);
        super.channelInactive(ctx);
    }

}
