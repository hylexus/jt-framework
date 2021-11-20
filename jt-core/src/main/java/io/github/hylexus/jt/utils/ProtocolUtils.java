package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.exception.MsgEncodingException;
import io.github.hylexus.jt.exception.MsgEscapeException;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
@Slf4j
public class ProtocolUtils {

    public static byte[] generateMsgHeaderForJt808RespMsg(int msgId, int bodyProps, Jt808ProtocolVersion version, String terminalId, int flowId) {
        if (version == Jt808ProtocolVersion.VERSION_2011) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                // 1. byte[0-1] 消息ID word(16)
                baos.write(IntBitOps.intTo2Bytes(msgId));
                // 2. byte[2-3] 消息体属性 word(16)
                baos.write(IntBitOps.intTo2Bytes(bodyProps));
                // 3. byte[4-9] 终端手机号 bcd[6]
                baos.write(BcdOps.bcdString2bytes(terminalId));
                // 4. byte[10-11] 消息流水号 word(16),按发送顺序从 0 开始循环累加
                baos.write(IntBitOps.intTo2Bytes(flowId));
                // 消息包封装项 此处不予考虑
                return baos.toByteArray();
            } catch (IOException e) {
                throw new MsgEncodingException(e);
            }
        }
        if (version == Jt808ProtocolVersion.VERSION_2019) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                // 1. byte[0-1] 消息ID word(16)
                baos.write(IntBitOps.intTo2Bytes(msgId));
                // 2. byte[2-3] 消息体属性 word(16)
                baos.write(IntBitOps.intTo2Bytes(bodyProps));
                // 3. byte[4]
                baos.write(version.getVersionBit());
                // 4. byte[5-14] 终端手机号 bcd[10]
                baos.write(BcdOps.bcdString2bytes(terminalId));
                // 5. byte[15-16] 消息流水号 word(16),按发送顺序从 0 开始循环累加
                baos.write(IntBitOps.intTo2Bytes(flowId));
                // 消息包封装项 此处不予考虑
                return baos.toByteArray();
            } catch (IOException e) {
                throw new MsgEncodingException(e);
            }
        }
        throw new JtIllegalArgumentException("不支持的版本" + version);
    }

    public static int generateMsgBodyPropsForJt808(int msgBodySize, int encryptionType, boolean isSubPackage, Jt808ProtocolVersion version, int reversedBit15) {
        // [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
        int props = (msgBodySize & 0x3FF)
                    // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
                    | ((encryptionType << 10) & 0x1C00)
                    // [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
                    | (((isSubPackage ? 1 : 0) << 13) & 0x2000)
                    // [14_ ]  0100,0000,0000,0000(4000)(保留位)
                    | ((version.getVersionBit() << 14) & 0x4000)
                    // [15_ ]  1000,0000,0000,0000(8000)(保留位)
                    | ((reversedBit15 << 15) & 0x8000);
        return props & 0xFFFF;
    }

    public static byte calculateCheckSum4Jt808(byte[] bs, int start, int end) {
        byte sum = bs[start];
        for (int i = start + 1; i < end; i++) {
            sum ^= bs[i];
        }
        return sum;
    }

    public static byte[] doEscape4ReceiveJt808Msg(byte[] bs, int start, int end) throws Exception {
        if (start < 0 || end > bs.length) {
            throw new ArrayIndexOutOfBoundsException("doEscape4Receive error : index out of bounds(start=" + start
                                                     + ",end=" + end + ",bytes length=" + bs.length + ")");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int i = 0;
            for (; i < start; i++) {
                outputStream.write(bs[i]);
            }
            // 最后一个字节不应该是7d，否则就是编码错误
            for (; i < end - 1; i++) {
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

            // https://github.com/hylexus/jt-framework/issues/17
            for (; i < bs.length; i++) {
                outputStream.write(bs[i]);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new MsgEscapeException(e);
        }
    }

    public static byte[] doEscape4SendJt808Msg(byte[] bs, int start, int end) {
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
                } else if (bs[i] == 0x7d) {
                    outputStream.write(0x7d);
                    outputStream.write(0x01);
                } else {
                    outputStream.write(bs[i]);
                }
            }
            // https://github.com/hylexus/jt-framework/issues/17
            for (int i = end + 1; i < bs.length; i++) {
                outputStream.write(bs[i]);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new MsgEscapeException(e);
        }
    }

    public static String bytes2String(byte[] bytes, int start, int length) {
        return bytes2String(bytes, start, length, null);
    }

    public static String bytes2String(byte[] bytes, int start, int length, String defaultVal) {
        try {
            return new String(Bytes.subSequence(bytes, start, length), JtProtocolConstant.JT_808_STRING_ENCODING);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return defaultVal;
        }
    }

    public static byte[] byteBufToByteArray(ByteBuf buf, int length) {
        byte[] tmp = new byte[length];
        buf.readBytes(tmp);
        return tmp;
    }
}
