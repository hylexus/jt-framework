package io.github.hylexus.jt808.samples.customized.controller;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.data.resp.DwordBytesValueWrapper;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt808.dispatcher.CommandSender;
import io.github.hylexus.jt808.msg.resp.CommandMsg;
import io.github.hylexus.jt808.samples.customized.msg.resp.RespTerminalSettings;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.session.Jt808SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt808.samples.customized.config.Jt808MsgType.*;

/**
 * @author hylexus
 * Created At 2019-10-06 9:12 下午
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private Jt808SessionManager jt808SessionManager;
    @Autowired
    private CommandSender commandSender;

    @GetMapping("/send-msg")
    public Object sendMsg(
            @RequestParam(required = false, name = "terminalId", defaultValue = "13717861955") String terminalId,
            @RequestParam(required = false, name = "timeout", defaultValue = "5") Long timeout) throws Exception {

        RespTerminalSettings param = new RespTerminalSettings();
        List<RespTerminalSettings.ParamItem> paramList = Lists.newArrayList(
                new RespTerminalSettings.ParamItem(0x0029, DwordBytesValueWrapper.of(100))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        // simulatePutResultByAnotherThread(commandKey);

        // 【下发消息】的消息类型为: RESP_TERMINAL_PARAM_SETTINGS (0x8103)  --> RespTerminalSettings的类注解上指定了下发类型
        // 客户端对该【下发消息】的回复消息类型为: CLIENT_COMMON_REPLY (0x0001)
        CommandMsg commandMsg = CommandMsg.of(terminalId, CLIENT_COMMON_REPLY, param);
        final Object resp = commandSender.sendCommandAndWaitingForReply(commandMsg, timeout, TimeUnit.SECONDS);
        log.info("resp: {}", resp);
        return resp;
    }

    @GetMapping("/query-terminal-properties")
    public Object queryTerminalProperties(
            @RequestParam(required = false, name = "terminalId", defaultValue = "13717861955") String terminalId,
            @RequestParam(required = false, name = "timeout", defaultValue = "5") Long timeout) throws Exception {

        // 【下发消息】的消息体为空
        // 【下发消息】消息的类型为: RESP_QUERY_TERMINAL_PROPERTIES (0x8107)
        // 客户端对该【下发消息】的回复消息类型为: CLIENT_QUERY_TERMINAL_PROPERTIES_REPLY (0x0107)
        CommandMsg commandMsg = CommandMsg.emptyRespMsgBody(terminalId, CLIENT_QUERY_TERMINAL_PROPERTIES_REPLY, RESP_QUERY_TERMINAL_PROPERTIES);
        return commandSender.sendCommandAndWaitingForReply(commandMsg, timeout, TimeUnit.SECONDS);
    }

    private Jt808Session getSession(String terminalId) {
        return jt808SessionManager.findByTerminalId(terminalId)
                .orElseThrow(() -> new JtSessionNotFoundException(terminalId));
    }

}
