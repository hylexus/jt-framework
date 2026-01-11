package io.github.hylexus.jt.jt808.adapter.xtreamcodec;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.serializer.DefaultJt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseBodyHandlerResultHandler;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt.utils.JtAnnotationUtils;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class XtreamCodecJt808ResponseBodyHandlerTest {
    XtreamCodecJt808ResponseBodyHandler handler;

    @BeforeEach
    void setUp() {
        final DefaultJt808MsgEncoder jt808MsgEncoder = new DefaultJt808MsgEncoder(
                ByteBufAllocator.DEFAULT,
                new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
                responseSubPackage -> {
                },
                Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
                Jt808MsgEncryptionHandler.NO_OPS
        );
        final Jt808RequestLifecycleListener requestLifecycleListener = new Jt808RequestLifecycleListener() {
        };
        final Jt808AnnotationBasedEncoder annotationBasedEncoder = new Jt808AnnotationBasedEncoder(new DefaultJt808FieldSerializerRegistry(true));
        final Jt808ResponseBodyHandlerResultHandler fallbackHandler = new Jt808ResponseBodyHandlerResultHandler(annotationBasedEncoder, jt808MsgEncoder);
        fallbackHandler.setRequestLifecycleListener(requestLifecycleListener);
        handler = new XtreamCodecJt808ResponseBodyHandler(
                jt808MsgEncoder,
                annotationBasedEncoder,
                EntityCodec.DEFAULT,
                fallbackHandler
        );

        handler.setRequestLifecycleListener(requestLifecycleListener);
    }

    @Test
    void testSupport() {
        assertTrue(this.handler.supports(Jt808HandlerResult.of(null, new Message8100XtreamCodec(1, (short) 0, "鉴权码 123"))));
        assertFalse(this.handler.supports(Jt808HandlerResult.of(null, new Object())));

    }

    @Test
    void testOrder() {
        assertTrue(this.handler.getOrder() <= OrderedComponent.DEFAULT_ORDER);
    }

    @Test
    void testShouldSkipResponse() {
        final Jt808HandlerResult handlerResult = Jt808HandlerResult.of(null, new Message8100XtreamCodec(1, (short) 0, "鉴权码 123"));
        final Jt808ServerExchange exchange = mockExchange(Message8100XtreamCodec.class);
        this.handler.setRequestLifecycleListener(new Jt808RequestLifecycleListener() {
            @Override
            public boolean beforeResponse(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult, ByteBuf response) {
                return false;
            }
        });
        this.handler.handleResult(exchange, handlerResult);

        final ArgumentCaptor<ByteBuf> captor = ArgumentCaptor.forClass(ByteBuf.class);
        verify(exchange.session(), never()).sendMsgToClient(captor.capture());

        assertEquals(0, exchange.response().body().refCnt());
    }

    @Test
    void testError() {
        final Jt808HandlerResult handlerResult = Jt808HandlerResult.of(null, new Message8100XtreamCodec(1, (short) 0, "鉴权码 123"));
        final Jt808ServerExchange exchange = mockExchange(Message8100XtreamCodec.class);
        exchange.response().writeByte(1);
        assertThrows(JtIllegalStateException.class, () -> handler.handleResult(exchange, handlerResult));
        exchange.response().release();
        assertEquals(0, exchange.response().body().refCnt());
    }

    @Test
    void testMessageDrivenByXtreamCodec() {
        final Jt808HandlerResult handlerResult = Jt808HandlerResult.of(null, new Message8100XtreamCodec(1, (short) 0, "鉴权码 123"));
        final Jt808ServerExchange exchange = mockExchange(Message8100XtreamCodec.class);
        this.handler.handleResult(exchange, handlerResult);

        final ArgumentCaptor<ByteBuf> captor = ArgumentCaptor.forClass(ByteBuf.class);
        verify(exchange.session()).sendMsgToClient(captor.capture());
        final ByteBuf encodedResponse = captor.getValue();

        assertEquals("7E8100400D01000000000139123443290000000100BCF8C8A8C2EB20313233A57E", FormatUtils.toHexString(encodedResponse));
        encodedResponse.release();
        assertEquals(0, encodedResponse.refCnt());
    }

    @Test
    void testRecordDrivenByXtreamCodec() {
        final Jt808HandlerResult handlerResult = Jt808HandlerResult.of(
                null,
                new RecordXtreamCodec(1, (short) 0, "鉴权码 123")
        );
        final Jt808ServerExchange exchange = mockExchange(RecordXtreamCodec.class);
        this.handler.handleResult(exchange, handlerResult);

        final ArgumentCaptor<ByteBuf> captor = ArgumentCaptor.forClass(ByteBuf.class);
        verify(exchange.session()).sendMsgToClient(captor.capture());
        final ByteBuf encodedResponse = captor.getValue();

        assertEquals("7E8100400D01000000000139123443290000000100BCF8C8A8C2EB20313233A57E", FormatUtils.toHexString(encodedResponse));
        encodedResponse.release();
        assertEquals(0, encodedResponse.refCnt());
    }

    @Test
    void testMessageDrivenByJtFramework() {
        final Jt808HandlerResult handlerResult = Jt808HandlerResult.of(null, new BuiltinMsg8100(1, (byte) 0, "鉴权码 123"));
        final Jt808ServerExchange exchange = mockExchange(BuiltinMsg8100.class);
        this.handler.handleResult(exchange, handlerResult);

        final ArgumentCaptor<ByteBuf> captor = ArgumentCaptor.forClass(ByteBuf.class);
        verify(exchange.session()).sendMsgToClient(captor.capture());
        final ByteBuf encodedResponse = captor.getValue();

        assertEquals("000100BCF8C8A8C2EB20313233", FormatUtils.toHexString(exchange.response().body()));
        assertEquals("7E8100400D01000000000139123443290000000100BCF8C8A8C2EB20313233A57E", FormatUtils.toHexString(encodedResponse));
        encodedResponse.release();
        assertEquals(0, encodedResponse.refCnt());
        assertEquals(0, exchange.response().body().refCnt());
    }

    @Test
    void testRecordDrivenByJtFramework() {
        final Jt808HandlerResult handlerResult = Jt808HandlerResult.of(null, new Record8100JtFramework(1, (short) 0, "鉴权码 123"));
        final Jt808ServerExchange exchange = mockExchange(Record8100JtFramework.class);
        this.handler.handleResult(exchange, handlerResult);

        final ArgumentCaptor<ByteBuf> captor = ArgumentCaptor.forClass(ByteBuf.class);
        verify(exchange.session()).sendMsgToClient(captor.capture());
        final ByteBuf encodedResponse = captor.getValue();

        assertEquals("000100BCF8C8A8C2EB20313233", FormatUtils.toHexString(exchange.response().body()));
        assertEquals("7E8100400D01000000000139123443290000000100BCF8C8A8C2EB20313233A57E", FormatUtils.toHexString(encodedResponse));
        encodedResponse.release();
        assertEquals(0, encodedResponse.refCnt());
        assertEquals(0, exchange.response().body().refCnt());
    }

    Jt808ServerExchange mockExchange(Class<?> entityClass) {
        final Jt808Request request = mock(Jt808Request.class);
        when(request.version()).thenReturn(Jt808ProtocolVersion.VERSION_2019);

        final Jt808ResponseBody annotation = Objects.requireNonNull(JtAnnotationUtils.getMergedAnnotation(entityClass, Jt808ResponseBody.class));
        final Jt808Response response = spy(new DefaultJt808Response(
                Jt808ProtocolVersion.VERSION_2019,
                annotation.msgId(),
                annotation.encryptionType(),
                annotation.maxPackageSize(),
                annotation.reversedBit15InHeader(),
                "00000000013912344329",
                0,
                ByteBufAllocator.DEFAULT.buffer()
        ));

        return new DefaultJt808ServerExchange(
                request,
                response,
                mock(Jt808Session.class)
        );
    }


    @Jt808ResponseBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC), msgId = 0x8100)
    public record RecordXtreamCodec(
            @Preset.JtStyle.Word(desc = "对应的终端注册消息的流水号") int clientFlowId,
            @Preset.JtStyle.Byte(desc = "注册结果") short result,
            @Preset.JtStyle.Str(condition = "result == 0", desc = "鉴权码") String authCode) {
    }

    @SuppressWarnings("unused")
    @Jt808ResponseBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC), msgId = 0x8100)
    public static class Message8100XtreamCodec {

        public Message8100XtreamCodec(int clientFlowId, short result, String authCode) {
            this.clientFlowId = clientFlowId;
            this.result = result;
            this.authCode = authCode;
        }

        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
        @Preset.JtStyle.Word(desc = "对应的终端注册消息的流水号")
        private int clientFlowId;

        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
        @Preset.JtStyle.Byte(desc = "注册结果")
        private short result;

        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
        @Preset.JtStyle.Str(condition = "result == 0", desc = "鉴权码")
        private String authCode;

        public int getClientFlowId() {
            return clientFlowId;
        }

        public Message8100XtreamCodec setClientFlowId(int clientFlowId) {
            this.clientFlowId = clientFlowId;
            return this;
        }

        public short getResult() {
            return result;
        }

        public Message8100XtreamCodec setResult(short result) {
            this.result = result;
            return this;
        }

        public String getAuthCode() {
            return authCode;
        }

        public Message8100XtreamCodec setAuthCode(String authCode) {
            this.authCode = authCode;
            return this;
        }
    }

    @Jt808ResponseBody(msgId = 0x8100)
    public record Record8100JtFramework(
            @ResponseField(order = 0, dataType = MsgDataType.WORD) int clientFlowId,
            @ResponseField(order = 1, dataType = MsgDataType.BYTE) short result,
            @ResponseField(order = 3, dataType = MsgDataType.STRING, conditionalOn = "result == 0") String authCode) {
    }

    @SuppressWarnings("unused")
    @Jt808ResponseBody(msgId = 0x8100)
    public static class BuiltinMsg8100 {
        public BuiltinMsg8100(int terminalFlowId, byte result, String authCode) {
            this.terminalFlowId = terminalFlowId;
            this.result = result;
            this.authCode = authCode;
        }

        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
        @ResponseField(order = 0, dataType = MsgDataType.WORD)
        private int terminalFlowId;
        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
        @ResponseField(order = 1, dataType = MsgDataType.BYTE)
        private byte result;
        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
        @ResponseField(order = 3, dataType = MsgDataType.STRING, conditionalOn = "result == 0")
        private String authCode;

        public int getTerminalFlowId() {
            return terminalFlowId;
        }

        public BuiltinMsg8100 setTerminalFlowId(int terminalFlowId) {
            this.terminalFlowId = terminalFlowId;
            return this;
        }

        public byte getResult() {
            return result;
        }

        public BuiltinMsg8100 setResult(byte result) {
            this.result = result;
            return this;
        }

        public String getAuthCode() {
            return authCode;
        }

        public BuiltinMsg8100 setAuthCode(String authCode) {
            this.authCode = authCode;
            return this;
        }
    }
}
