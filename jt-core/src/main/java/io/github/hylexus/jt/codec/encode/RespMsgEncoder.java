package io.github.hylexus.jt.codec.encode;

import com.google.common.collect.Lists;
import io.github.hylexus.oaks.utils.Bytes;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author hylexus
 * Created At 2020-02-02 6:56 下午
 */
@Slf4j
public class RespMsgEncoder {

    private CommonFieldEncoder commonFieldEncoder;

    public RespMsgEncoder() {
        this.commonFieldEncoder = new CommonFieldEncoder();
    }

    public byte[] encodeRespMsgBody(Object msgBody) throws InstantiationException, IllegalAccessException {
        final List<byte[]> result = Lists.newArrayList();
        // TODO 其他类型注解的处理
        return Bytes.concatAll(commonFieldEncoder.encodeMsgBodyRecursively(msgBody, result));
    }
}
