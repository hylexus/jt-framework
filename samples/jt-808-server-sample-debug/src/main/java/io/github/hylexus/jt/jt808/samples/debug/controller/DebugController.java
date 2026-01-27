package io.github.hylexus.jt.jt808.samples.debug.controller;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt.jt808.samples.debug.entity.resp.RespTerminalSettings;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg8300Alias;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 */
@Slf4j
@RestController
@RequestMapping("/debug")
public class DebugController {

    private final Jt808CommandSender commandSender;
    private final Jt808SessionManager sessionManager;

    public DebugController(Jt808CommandSender commandSender, Jt808SessionManager sessionManager) {
        this.commandSender = commandSender;
        this.sessionManager = sessionManager;
    }

    @RequestMapping("/set-terminal-params")
    public Object sendMsg(
            @RequestParam(name = "terminalId", required = false, defaultValue = "13912344321") String terminalId) throws InterruptedException {

        final RespTerminalSettings param = new RespTerminalSettings();
        final List<RespTerminalSettings.ParamItem> paramList = Lists.newArrayList(
                new RespTerminalSettings.ParamItem(0x0029, ByteBufAllocator.DEFAULT.buffer().writeInt(100)),
                new RespTerminalSettings.ParamItem(0x0030, ByteBufAllocator.DEFAULT.buffer().writeInt(211))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        final Jt808Session session = sessionManager.findByTerminalId(terminalId).orElseThrow(() -> new JtSessionNotFoundException(terminalId));
        final int nextFlowId = session.nextFlowId();
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, nextFlowId);
        final Object resp = commandSender.sendCommandAndWaitingForReply(commandKey, param, 15L, TimeUnit.SECONDS);
        log.info("RESP::::::: {}", resp);
        return resp;
    }

    @PostMapping("/send-command-8300")
    public Object sendCommand8300(@RequestBody @Validated Dto8300 dto8300) throws InterruptedException {
        final String terminalId = dto8300.getTerminalId();
        final Jt808Session session = sessionManager.findByTerminalId(terminalId).orElseThrow(() -> new JtSessionNotFoundException(terminalId));
        final int nextFlowId = session.nextFlowId();

        final Jt808CommandKey commandKey = Jt808CommandKey
                .of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, nextFlowId);

        log.info("[指令下发]: nextFlowId: {}, commandKey: {}", nextFlowId, commandKey);

        final BuiltinMsg8300Alias entity = new BuiltinMsg8300Alias()
                .setFlag(dto8300.getFlag())
                .setMessage(dto8300.getMessage());
        final Object resp = commandSender.sendCommandAndWaitingForReply(commandKey, entity, 15L, TimeUnit.SECONDS);
        log.info("[指令下发] 响应(8300): {}", resp);
        return resp;
    }

    @Data
    public static class Dto8300 {
        @NotNull(message = "terminalId is null")
        @NotEmpty(message = "terminalId is empty")
        private String terminalId;

        private byte flag;

        @NotNull(message = "message is null")
        @NotEmpty(message = "message is empty")
        private String message;
    }

}
