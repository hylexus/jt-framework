package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.msg.RequestMsgCommonProps;
import io.github.hylexus.jt808.msg.req.AuthRequestMsgBody;
import org.junit.Test;

/**
 * @author hylexus
 * Created At 2019-09-22 9:22 下午
 */
public class DecoderTest {

    Decoder decoder = new Decoder();

    @Test
    public void testDecodeAuthMsg() throws Exception {
        String hex = "0102000676890100565000325756494C5A4CDD";
        RequestMsgCommonProps props = decoder.parseAbstractMsg(HexStringUtils.hexString2Bytes(hex));
        byte[] bodyBytes = props.getBodyBytes();
        AuthRequestMsgBody authRequestMsgBody = decoder.decodeRequestMsgBody(AuthRequestMsgBody.class, bodyBytes, props);
        System.out.println(authRequestMsgBody);
    }
}
