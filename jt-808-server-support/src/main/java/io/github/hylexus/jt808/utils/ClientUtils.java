package io.github.hylexus.jt808.utils;

import io.github.hylexus.jt808.session.Jt808Session;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2020-02-09 2:01 下午
 */
@Slf4j
public class ClientUtils {

    public static void sendBytesToClient(Jt808Session session, byte[] bytes) throws InterruptedException {
        ChannelFuture future = session.getChannel().writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
        if (!future.isSuccess()) {
            log.error("ERROR : 'send data to client:'", future.cause());
        }
    }
}
