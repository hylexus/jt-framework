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

    String BEAN_NAME_JT808_REQ_MSG_DISPATCHER_THREAD_POOL = "jt808RequestMsgDispatcherThreadPool";

    long JT808_REQ_MSG_DISPATCHER_THREAD_POOL_KEEP_ALIVE_TIME_IN_SECONDS = 60;
}
