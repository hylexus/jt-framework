package io.github.hylexus.jt808.samples.annotation.config;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.MsgEscapeException;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import static io.github.hylexus.jt.config.JtProtocolConstant.BEAN_NAME_JT808_BYTES_ENCODER;

/**
 * @author hylexus
 * Created At 2020-02-02 1:22 下午
 */
@Configuration
public class Jt808Config extends Jt808ServerConfigure {

    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return msgType -> {
            Optional<MsgType> type = Jt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
            return type.isPresent()
                    ? type
                    // 自定义解析器无法解析,使用内置解析器
                    : BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
        };
    }

    // https://github.com/hylexus/jt-framework/issues/17
    @Bean(BEAN_NAME_JT808_BYTES_ENCODER)
    public BytesEncoder bytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder() {
            @Override
            public byte[] doEscapeForReceive(byte[] bs, int start, int end) throws MsgEscapeException {
                if (start < 0 || end > bs.length) {
                    throw new ArrayIndexOutOfBoundsException("doEscape4Receive error : index out of bounds(start=" + start
                            + ",end=" + end + ",bytes length=" + bs.length + ")");
                }
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    for (int i = 0; i < start; i++) {
                        outputStream.write(bs[i]);
                    }
                    for (int i = start; i < end - 2; i++) {
                        if (bs[i] == 0x7d && bs[i + 1] == 0x01) {
                            outputStream.write(0x7d);
                            i++;
                        } else if (bs[i] == 0x7d && bs[i + 1] == 0x02) {
                            outputStream.write(0x7e);
                            i++;
                        } else {
                            outputStream.write(bs[i]);
                        }
                    }
                    if (bs[end - 2] == 0x7d && bs[end - 1] == 0x01) {
                        outputStream.write(0x7d);
                    } else if (bs[end - 2] == 0x7d && bs[end - 1] == 0x02) {
                        outputStream.write(0x7e);
                    } else {
                        for (int i = end - 2; i < bs.length; i++) {
                            outputStream.write(bs[i]);
                        }
                    }
                    return outputStream.toByteArray();
                } catch (IOException e) {
                    throw new MsgEscapeException(e);
                }
            }

            @Override
            public byte[] doEscapeForSend(byte[] bs, int start, int end) throws MsgEscapeException {
                if (start < 0 || end > bs.length) {
                    throw new ArrayIndexOutOfBoundsException("doEscape4Send error : index out of bounds(start=" + start
                            + ",end=" + end + ",bytes length=" + bs.length + ")");
                }
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    for (int i = 0; i < start; i++) {
                        outputStream.write(bs[i]);
                    }
                    for (int i = start; i <= end; i++) {
                        if (bs[i] == 0x7e) {
                            outputStream.write(0x7d);
                            outputStream.write(0x02);
                        } else {
                            outputStream.write(bs[i]);
                        }
                    }
                    for (int i = end + 1; i < bs.length; i++) {
                        outputStream.write(bs[i]);
                    }
                    return outputStream.toByteArray();
                } catch (IOException e) {
                    throw new MsgEscapeException(e);
                }
            }
        };

    }
}
