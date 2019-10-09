package io.github.hylexus.jt808.server.controller;

import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.session.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author hylexus
 * Created At 2019-10-06 9:12 下午
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class TestController {

    @GetMapping("/send-to")
    public void sendTo(@RequestParam("terminalId") String terminalId) throws InterruptedException, TimeoutException, ExecutionException {
        Session session = SessionManager.getInstance().findByTerminalId(terminalId).orElseThrow(() -> new JtSessionNotFoundException(terminalId));
        ByteBuf byteBuf = Unpooled.wrappedBuffer(HexStringUtils.hexString2Bytes("1234"));
        ChannelFuture future = session.getChannel().writeAndFlush(byteBuf).sync();
        if (!future.isSuccess()) {
            log.error("", future.cause());
        }
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
