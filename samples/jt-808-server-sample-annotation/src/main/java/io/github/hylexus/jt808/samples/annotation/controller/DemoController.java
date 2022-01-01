package io.github.hylexus.jt808.samples.annotation.controller;

import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt808.samples.annotation.msg.resp.RespTerminalSettings;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 */
@Slf4j
@RestController
@RequestMapping("/demo01")
public class DemoController {

    private final Jt808CommandSender commandSender;
    private final Jt808SessionManager sessionManager;

    public DemoController(Jt808CommandSender commandSender, Jt808SessionManager sessionManager) {
        this.commandSender = commandSender;
        this.sessionManager = sessionManager;
    }

    // 7E00010005013912344323007B0000810300837E
    // 7E00010005013912344323007B0001810300827E
    @RequestMapping("/set-terminal-params")
    public Object sendMsg(
            @RequestParam(name = "terminalId", required = false, defaultValue = "013912344323") String terminalId) throws InterruptedException {

        final RespTerminalSettings param = new RespTerminalSettings();
        final List<RespTerminalSettings.ParamItem> paramList = List.of(
                new RespTerminalSettings.ParamItem(0x0029, ByteBufAllocator.DEFAULT.buffer().writeInt(100)),
                new RespTerminalSettings.ParamItem(0x0030, ByteBufAllocator.DEFAULT.buffer().writeInt(211))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        final Jt808Session session = sessionManager.findByTerminalId(terminalId).orElseThrow();
        final int nextFlowId = session.nextFlowId();
        // 1. 生成Key(收到终端回复时会根据这个Key来匹配)
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, nextFlowId);
        final Object resp = commandSender.sendCommandAndWaitingForReply(commandKey, param, 20L, TimeUnit.SECONDS);
        log.info("RESP::::::: {}", resp);
        return resp;
    }

    @RequestMapping("/send-some-msg")
    public void sendMsgBySession(@RequestParam(name = "terminalId", required = false, defaultValue = "013912344323") String terminalId) {

        this.sessionManager.findByTerminalId(terminalId)
                .orElseThrow(() -> new IllegalArgumentException("No terminal found with terminalId " + terminalId))
                .sendMsgToClient(ByteBufAllocator.DEFAULT.buffer().writeBytes("data will be sent to client".getBytes(Charset.forName("GBK"))));
    }
}
