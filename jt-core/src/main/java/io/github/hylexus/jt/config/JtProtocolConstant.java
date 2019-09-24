package io.github.hylexus.jt.config;

import java.nio.charset.Charset;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
public interface JtProtocolConstant {
    Charset JT_808_STRING_ENCODING = Charset.forName("GBK");
    int PACKAGE_DELIMITER = 0x7E;
    int MAX_PACKAGE_LENGTH = 1024;

    int FIELD_LENGTH_UNKNOWN = -1;

    String BEAN_NAME_JT808_REQ_MSG_QUEUE = "jt808RequestMsgQueue";
    String BEAN_NAME_JT808_REQ_MSG_QUEUE_LISTENER = "jt808RequestMsgQueueListener";
    String BEAN_NAME_JT808_REQ_MSG_DISPATCHER = "jt808RequestMsgDispatcher";
    String BEAN_NAME_JT808_REQ_MSG_TYPE_PARSER = "jt808RequestMsgTypeParser";

    String BEAN_NAME_JT808_NETTY_TCP_SERVER = "jt808NettyTcpServer";

    String NETTY_HANDLER_NAME_808_HEART_BEAT = "Jt808NettyHeartBeatHandler";
    String NETTY_HANDLER_NAME_808_IDLE_STATE = "Jt808NettyIdleStateHandler";
    String NETTY_HANDLER_NAME_808_FRAME = "Jt808NettyHandler";
    String NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER = "Jt808NettyHandlerAdapter";
}
