package io.github.hylexus.jt.jt808.issues.issue90;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JT808Decoder extends ByteToMessageDecoder {

    private static final byte DELIMITER = (byte) 0x7E;
    private static final byte ESCAPE_CHAR = (byte) 0x7D;
    // 0x7D -> 0x7D 0x01
    private static final byte ESCAPE_7D = (byte) 0x01;
    // 0x7E -> 0x7D 0x02
    private static final byte ESCAPE_7E = (byte) 0x02;
    private static final Logger log = LoggerFactory.getLogger(JT808Decoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 寻找起始标志 0x7E
        int startIdx = -1;
        for (int i = in.readerIndex(); i < in.writerIndex(); i++) {
            if (in.getByte(i) == DELIMITER) {
                startIdx = i;
                break;
            }
        }

        if (startIdx == -1) {
            // 没有找到起始标志，丢弃所有数据??? 还是 保留 ???
            in.clear();
            return;
        }

        // 跳过起始标志前的数据
        if (startIdx > in.readerIndex()) {
            in.readerIndex(startIdx);
        }

        // 至少需要两个字节：0x7E ... 0x7E
        if (in.readableBytes() < 2) {
            return;
        }

        // 查找结束标志（从 startIdx + 1 开始）
        int endIdx = -1;
        for (int i = startIdx + 1; i < in.writerIndex(); i++) {
            if (in.getByte(i) == DELIMITER) {
                endIdx = i;
                break;
            }
        }

        if (endIdx == -1) {
            // 没有找到结束标志，等待更多数据
            return;
        }

        // 提取原始帧（包括头尾 0x7E）
        final int frameLength = endIdx - startIdx + 1;
        final ByteBuf frameBuf = in.readSlice(frameLength);

        // 跳过起始 0x7E 和结束 0x7E，提取中间内容
        final ByteBuf content = frameBuf.slice(1, frameBuf.readableBytes() - 2);

        // 存放去转义后的数据
        final ByteBuf unescaped = ctx.alloc().buffer(content.readableBytes());

        boolean escaped = false;
        for (int i = 0; i < content.readableBytes(); i++) {
            byte b = content.getByte(i);
            if (escaped) {
                if (b == ESCAPE_7E) {
                    unescaped.writeByte(DELIMITER);
                } else if (b == ESCAPE_7D) {
                    unescaped.writeByte(ESCAPE_CHAR);
                } else {
                    unescaped.writeByte(ESCAPE_CHAR);
                    unescaped.writeByte(b);
                }
                escaped = false;
            } else if (b == ESCAPE_CHAR) {
                escaped = true;
            } else {
                unescaped.writeByte(b);
            }
        }

        // 最后一个字节是转义符但没有后续???
        if (escaped) {
            unescaped.writeByte(ESCAPE_CHAR);
        }

        mergePackage(out, unescaped);

        // 继续递归尝试解码剩余数据（可能有多个帧）???
        // decode(ctx, in, out);
    }

    /**
     * @param input 不含 7e 分隔符
     */
    void mergePackage(List<Object> out, ByteBuf input) {
        // byte[0-2)    消息ID word(16)
        // byte[2-4)    消息体属性 word(16)
        //       bit[0-10)    消息体长度
        //       bit[10-13)    数据加密方式
        //                   此三位都为 0,表示消息体不加密
        //                   第 10 位为 1,表示消息体经过 RSA 算法加密
        //                   其它保留
        //       bit[13]        分包
        //                   1: 消息体卫长消息,进行分包发送处理,具体分包信息由消息包封装项决定
        //                   0: 则消息头中无消息包封装项字段
        //       bit[14-15]    保留
        // byte[4-10)    终端手机号或设备ID bcd[6]
        // byte[10-12) 消息流水号 word(16)
        // byte[12-16)    消息包封装项
        final ByteBuf merged = JT808PackageMerger.mergePackage(input);
        if (merged != null) {
            out.add(merged);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[exceptionCaught]", cause);
        ctx.close();
    }
}
