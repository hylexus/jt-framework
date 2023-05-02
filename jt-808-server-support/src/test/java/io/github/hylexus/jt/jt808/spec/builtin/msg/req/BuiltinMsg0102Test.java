package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.Padding;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinMsg0102Test extends BaseReqRespMsgTest {

    @Test
    void test2011() {
        final String hex = "7E0102000C013912344321007B61646D696E2D3132333435364D7E";
        final BuiltinMsg0102V2011 msg = decode(hex, BuiltinMsg0102V2011.class);
        Assertions.assertEquals("admin-123456", msg.getAuthCode());
    }

    @Test
    void test2011Alias() {
        final String hex = "7E0102000C013912344321007B61646D696E2D3132333435364D7E";
        final BuiltinMsg0102V2011Alias msg = decode(hex, BuiltinMsg0102V2011Alias.class);
        Assertions.assertEquals("admin-123456", msg.getAuthCode());
    }

    @Test
    void test2013() {
        final String hex = "7E0102000C013912344321007B61646D696E2D3132333435364D7E";
        final BuiltinMsg0102V2013 msg = decode(hex, BuiltinMsg0102V2013.class);
        Assertions.assertEquals("admin-123456", msg.getAuthCode());
    }

    @Test
    void test2013Alias() {
        final String hex = "7E0102000C013912344321007B61646D696E2D3132333435364D7E";
        final BuiltinMsg0102V2013Alias msg = decode(hex, BuiltinMsg0102V2013Alias.class);
        Assertions.assertEquals("admin-123456", msg.getAuthCode());
    }

    @Test
    void test2019() {
        final String authCode = "这不是Bug,这是Feature!!!";
        final String hex = this.buildMsg2019(authCode);
        final BuiltinMsg0102V2019 msg = decode(hex, BuiltinMsg0102V2019.class);

        Assertions.assertEquals(authCode, msg.getAuthCode());
        Assertions.assertEquals("123456789011111", msg.getImei());
        Assertions.assertEquals("v1.2.300000000000000", msg.getSoftwareVersion());
    }

    @Test
    void test2019Alias() {
        final String authCode = "这不是Bug,这是Feature!!!";
        final String hex = this.buildMsg2019(authCode);
        final BuiltinMsg0102V2019Alias msg = decode(hex, BuiltinMsg0102V2019Alias.class);

        Assertions.assertEquals(msg.getAuthCodeLength(), msg.getAuthCode().getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length);
        Assertions.assertEquals(authCode, msg.getAuthCode());
        Assertions.assertEquals("123456789011111", msg.getImei());
        Assertions.assertEquals("v1.2.300000000000000", msg.getSoftwareVersion());
    }

    @Test
    void testEmptyAuthCode() {
        // 测试空鉴权码
        final String hex = this.buildMsg2019("");
        final BuiltinMsg0102V2019 msg = decode(hex, BuiltinMsg0102V2019.class);
        Assertions.assertEquals("", msg.getAuthCode());
        Assertions.assertEquals("123456789011111", msg.getImei());
        Assertions.assertEquals("v1.2.300000000000000", msg.getSoftwareVersion());
    }

    String buildMsg2019(String authCode) {
        @Data
        @Accessors(chain = true)
        @Jt808ResponseBody(msgId = -1)
        class Msg0102V2019Placeholder {
            @ResponseFieldAlias.Byte(order = 1)
            private byte authCodeLength;
            @ResponseFieldAlias.String(order = 2)
            private String authCode;
            // bytes 15
            @ResponseFieldAlias.Bytes(order = 3)
            private String imei;
            // bytes 20

            @ResponseFieldAlias.Bytes(order = 4, charset = "GBK", paddingRight = @Padding(minLength = 20))
            private String softwareVersion;

            //@ResponseField(order = 4, dataType = MsgDataType.BYTES)
            // private byte[] softwareVersion;
        }

        final int authCodeLength = authCode.getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length;

        final Msg0102V2019Placeholder msg = new Msg0102V2019Placeholder()
                .setAuthCodeLength((byte) authCodeLength)
                .setAuthCode(authCode)
                .setImei("123456789011111")
                //  .setSoftwareVersion("v1.2.3".getBytes());
                // .setSoftwareVersion("v1.2.300000000000000");
                .setSoftwareVersion("v1.2.30");

        return encode(msg, builder -> builder.msgId(BuiltinJt808MsgType.CLIENT_AUTH)
                .version(Jt808ProtocolVersion.VERSION_2019)
                .terminalId(terminalId2019));

    }


}