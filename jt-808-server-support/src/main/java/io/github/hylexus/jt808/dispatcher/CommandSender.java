package io.github.hylexus.jt808.dispatcher;

import io.github.hylexus.jt.command.Jt808CommandKey;
import io.github.hylexus.jt808.msg.resp.CommandMsg;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 主动下发数据给终端。
 *
 * @author hylexus
 * Created At 2020-03-11 22:07
 */
public interface CommandSender {

    /**
     * 下发数据，--> 不等待 终端回复
     *
     * @param terminalId 终端ID
     * @param data       要下发的数据。注意:该方法所需数据需要 1. 手动拼装为符合808协议格式的报文, 2. 手动转义
     * @throws InterruptedException InterruptedException
     */
    void sendCommand(String terminalId, byte[] data) throws InterruptedException;

    /**
     * 下发数据，--> 不等待 终端回复
     *
     * @param commandMsg 要下发的数据。
     * @throws InterruptedException InterruptedException
     * @throws IOException          IOException
     * @see CommandMsg
     */
    void sendCommand(CommandMsg commandMsg) throws InterruptedException, IOException;

    /**
     * 下发数据，--> [[等待]] 终端回复
     *
     * @param key      {@link Jt808CommandKey}
     * @param data     要下发的数据。注意:该方法所需数据需要 1. 手动拼装为符合808协议格式的报文, 2. 手动转义
     * @param timeout  超时时间，等待终端回复的最大等待时间
     * @param timeUnit 超时时间单位
     * @return 终端回复的数据
     * @throws InterruptedException InterruptedException
     */
    Object sendCommandAndWaitingForReply(Jt808CommandKey key, byte[] data, long timeout, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 下发数据，--> [[等待]] 终端回复
     *
     * @param commandMsg 要下发的数据
     * @param timeout    超时时间，等待终端回复的最大等待时间
     * @param timeUnit   超时时间单位
     * @param withFlowId 是否有流水号
     * @return 终端回复的数据
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     */
    Object sendCommandAndWaitingForReply(CommandMsg commandMsg, boolean withFlowId, Long timeout, TimeUnit timeUnit) throws IOException, InterruptedException;

    /**
     * 下发数据，--> [[等待]] 终端回复
     *
     * @param commandMsg 要下发的数据
     * @param timeout    超时时间，等待终端回复的最大等待时间
     * @param timeUnit   超时时间单位
     * @return 终端回复的数据
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     * @see #sendCommandAndWaitingForReply(CommandMsg, boolean, Long, TimeUnit)
     */
    default Object sendCommandAndWaitingForReply(CommandMsg commandMsg, Long timeout, TimeUnit timeUnit) throws IOException, InterruptedException {
        return this.sendCommandAndWaitingForReply(commandMsg, true, timeout, timeUnit);
    }

}
