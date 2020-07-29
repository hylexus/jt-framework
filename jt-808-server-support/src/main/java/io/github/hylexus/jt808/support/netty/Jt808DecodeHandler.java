package io.github.hylexus.jt808.support.netty;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.codec.Decoder;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lirenhao
 * date: 2020/7/10 5:37 下午
 */
@Slf4j(topic = "jt-808.channel.handler.adapter")
@ChannelHandler.Sharable
public class Jt808DecodeHandler extends ChannelInboundHandlerAdapter {

    private final Jt808ProtocolVersion protocolVersion;
    private final Decoder decoder;
    private final BytesEncoder bytesEncoder;

    public Jt808DecodeHandler(Jt808ProtocolVersion protocolVersion, BytesEncoder bytesEncoder, Decoder decoder) {
        this.protocolVersion = protocolVersion;
        this.bytesEncoder = bytesEncoder;
        this.decoder = decoder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }

                final byte[] unescaped = new byte[buf.readableBytes()];
                buf.readBytes(unescaped);
                log.debug("\nreceive msg:");
                log.debug(">>>>>>>>>>>>>>> : {}", HexStringUtils.bytes2HexString(unescaped));
                final byte[] escaped = this.bytesEncoder.doEscapeForReceive(unescaped, 0, unescaped.length);
                log.debug("[escaped] : {}", HexStringUtils.bytes2HexString(escaped));

                final RequestMsgMetadata metadata = decoder.parseMsgMetadata(protocolVersion, escaped);
                ctx.fireChannelRead(metadata);
            } finally {
                buf.release();
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
