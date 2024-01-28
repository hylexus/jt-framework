package io.github.hylexus.jt.jt808;

import java.nio.charset.Charset;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
public interface JtProtocolConstant {
    String DEFAULT_DATE_TIME_FORMAT = "yyMMddHHmmss";

    Charset JT_808_STRING_ENCODING = Charset.forName("GBK");
    int PACKAGE_DELIMITER = 0x7E;
    int MAX_PACKAGE_LENGTH = 1024;

    String BEAN_NAME_JT808_INTERCEPTORS = "jt808Interceptors";
    String BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP = "jt808MsgProcessorEventExecutorGroup";
    String BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP = "jt808AttachmentMsgProcessorEventExecutorGroup";
    String BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT = "Jt808NettyHeartBeatHandler";
    String BEAN_NAME_NETTY_HANDLER_NAME_ATTACHMENT_808_HEART_BEAT = "AttachmentJt808NettyHeartBeatHandler";

    String NETTY_HANDLER_NAME_808_IDLE_STATE = "Jt808NettyIdleStateHandler";
    String NETTY_HANDLER_NAME_ATTACHMENT_808_IDLE_STATE = "AttachmentJt808NettyIdleStateHandler";
    String NETTY_HANDLER_NAME_808_FRAME = "Jt808NettyHandler";
    String NETTY_HANDLER_NAME_ATTACHMENT_808_FRAME = "AttachmentJt808NettyHandler";
    String NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER = "Jt808NettyHandlerAdapter";
}
