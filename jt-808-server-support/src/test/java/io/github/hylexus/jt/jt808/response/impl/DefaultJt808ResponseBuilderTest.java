//package io.github.hylexus.jt.jt808.response.impl;
//
//import io.github.hylexus.jt.config.Jt808ProtocolVersion;
//import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
//import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgBodySpec;
//import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
//import junit.framework.TestCase;
//import org.junit.Test;
//
///**
// * @author hylexus
// */
//public class DefaultJt808ResponseBuilderTest {
//
//    @Test
//    public void testBuildServerCommonReplyMsg() {
//        final DefaultJt808Response response = DefaultJt808ResponseBuilder.newBuilder()
//                .withHeader(
//                        builder -> builder.withMsgId(BuiltinJt808MsgType.SERVER_COMMON_REPLY.getMsgId())
//                                .withMsgBodyPropsSpec(
//                                        // TODO len
//                                        propsSpecBuilder -> propsSpecBuilder.withMsgBodyLength(0)
//                                                .withEncryptionType(0b000)
//                                                .withSubPackageIdentifier(false)
//                                                .withVersionIdentifier(Jt808ProtocolVersion.VERSION_2019.getVersionBit())
//                                                .withReversedBit15(1)
//                                )
//                                .withJt808ProtocolVersion(Jt808ProtocolVersion.VERSION_2019)
//                                .withTerminalId("00000000018930946552")
//                                .withFlowId(101)
//                )
//                .withBody(new DefaultJt808MsgBodySpec(new Jt808ByteBuf(null)))
//                .withCheckSum((byte) 23)
//                .build();
//    }
//}