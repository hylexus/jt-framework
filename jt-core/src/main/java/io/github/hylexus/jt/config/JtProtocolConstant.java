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
}
