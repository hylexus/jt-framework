package io.github.hylexus.jt808.support.netty;

import io.github.hylexus.jt808.ext.TerminalValidator;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.config.JtProtocolConstant.NETTY_HANDLER_NAME_808_TERMINAL_VALIDATOR;

/**
 * @author lirenhao
 * date: 2020/7/12 4:42 下午
 */
@Slf4j(topic = "jt-808.channel.handler.adapter")
@ChannelHandler.Sharable
public class TerminalValidatorHandler extends ChannelInboundHandlerAdapter {

    private final TerminalValidator terminalValidator;

    public TerminalValidatorHandler(TerminalValidator terminalValidator) {
        this.terminalValidator = terminalValidator;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestMsgMetadata) {
            final RequestMsgMetadata metadata = (RequestMsgMetadata) msg;

            if (!terminalValidator.needValidate(metadata, metadata.getHeader().getMsgId())) {
                ctx.fireChannelRead(msg);
                return;
            }

            if (terminalValidator.validateTerminal(metadata)) {
                log.debug("[TerminalValidator] Verification success");
                ctx.pipeline().remove(NETTY_HANDLER_NAME_808_TERMINAL_VALIDATOR);
                ctx.fireChannelRead(msg);
            } else {
                log.debug("[TerminalValidator] Verification failed");
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
