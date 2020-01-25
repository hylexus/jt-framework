package io.github.hylexus.jt808.server.controller;

import io.github.hylexus.jt.command.CommandWaitingPool;
import io.github.hylexus.jt.command.Jt808CommandKey;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.session.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2019-10-06 9:12 下午
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class TestController {

    @GetMapping("/send-to")
    public void sendTo(@RequestParam("terminalId") String terminalId) throws InterruptedException {
        Session session = getSession(terminalId);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(HexStringUtils.hexString2Bytes("1234"));
        session.sendMsgToClient(byteBuf);
    }

    private Session getSession(@RequestParam("terminalId") String terminalId) {
        return SessionManager.getInstance().findByTerminalId(terminalId).orElseThrow(() -> new JtSessionNotFoundException(terminalId));
    }

    @GetMapping("/send-msg")
    public Object sendMsg(
            @RequestParam(required = false, name = "terminalId", defaultValue = "13717861955") String terminalId,
            @RequestParam(required = false, name = "msg", defaultValue = "1234") String msg,
            @RequestParam(required = false, name = "timeout", defaultValue = "2") Long timeout) throws InterruptedException {
        final Session session = getSession(terminalId);
        byte[] bytes = HexStringUtils.hexString2Bytes(msg);
        session.sendMsgToClient(bytes);

        final Jt808CommandKey commandKey = Jt808CommandKey.of(BuiltinJt808MsgType.CLIENT_AUTH, terminalId);

        // 模拟2秒后终端有数据回复
        simulatePutResultByAnotherThread(commandKey);

        return CommandWaitingPool.getInstance().waitingForKey(commandKey, timeout, TimeUnit.SECONDS);
    }

    private void simulatePutResultByAnotherThread(Jt808CommandKey commandKey) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                CommandWaitingPool.getInstance().putIfNecessary(commandKey, "result for " + commandKey.getKeyAsString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @GetMapping("/deferred-result")
    public DeferredResult<String> testDeferredResult() throws InterruptedException {
        DeferredResult<String> deferredResult = new DeferredResult<>();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deferredResult.setResult("abc-ddd");
        }).start();
        return deferredResult;
    }
}
