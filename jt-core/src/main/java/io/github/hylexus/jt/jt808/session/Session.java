package io.github.hylexus.jt.jt808.session;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Session {
    //Session的唯一标识
    private String id;
    //和Session相关的channel,通过它向客户端回送数据
    private Channel channel = null;
    private String terminalId;
    // 消息流水号 word(16) 按发送顺序从 0 开始循环累加
    private int currentFlowId = 0;
    //上次通信时间
    private long lastCommunicateTimeStamp = 0L;

    public static String generateSessionId(Channel channel) {
        return channel.id().asLongText();
    }

    public static Session buildSession(Channel channel, String terminalPhone) {
        Session session = new Session();
        session.setChannel(channel);

        //使用netty生成的类似UUID的字符串,来标识一个session
        session.setId(generateSessionId(channel));
        session.setTerminalId(terminalPhone);
        session.setLastCommunicateTimeStamp(System.currentTimeMillis());
        return session;
    }

    public int getCurrentFlowId() {
        return currentFlowId();
    }

    private synchronized int currentFlowId() {
        if (currentFlowId >= 0xffff) {
            currentFlowId = 0;
        }
        return currentFlowId++;
    }
}
