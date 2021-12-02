package io.github.hylexus.jt.jt808.samples.debug.controller;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.response.Jt808CommandKey;
import io.github.hylexus.jt.jt808.response.Jt808CommandSender;
import io.github.hylexus.jt.jt808.samples.debug.entity.resp.RespTerminalSettings;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    public DebugController(Jt808CommandSender commandSender) {
        this.commandSender = commandSender;
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

        // TODO flowId
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY,1);
        final Object resp = commandSender.sendCommandAndWaitingForReply(commandKey, param, 15L, TimeUnit.SECONDS);
        log.info("RESP::::::: {}", resp);
        return resp;
    }
}
