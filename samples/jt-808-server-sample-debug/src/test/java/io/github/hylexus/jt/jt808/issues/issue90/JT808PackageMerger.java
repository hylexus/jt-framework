package io.github.hylexus.jt.jt808.issues.issue90;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgBodyProps;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.FormatUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JT808PackageMerger {

    // 缓存 key: terminalId_msgId_flowId
    private static final Map<String, PendingPackage> pendingPackages = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(JT808PackageMerger.class);

    /**
     * 合并分包（如果需要），否则直接返回 input
     *
     * @param input 不含 0x7E 的原始消息体（已去转义）
     * @return 完整消息体（不含分包字段），或 null（表示尚未收齐）
     */
    public static ByteBuf mergePackage(ByteBuf input) {
        if (input.readableBytes() < 12) {
            // 至少要有消息头（12字节）
            return input;
        }

        final int msgId = JtProtocolUtils.getWord(input, 0);

        final int msgBodyPropsIntValue = JtProtocolUtils.getWord(input, 2);
        final Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps = new DefaultJt808MsgBodyProps(msgBodyPropsIntValue);
        // 这里示例性的解析一下 2019 版的
        // 其他版本抛异常
        if (msgBodyProps.versionIdentifier() != 1) {
            throw new UnsupportedOperationException("暂不支持非JT/T808-2019协议");
        }
        final boolean hasSubPackage = msgBodyProps.hasSubPackage();

        if (!hasSubPackage) {
            // 非分包，直接返回
            return input;
        }

        if (input.readableBytes() < 16) {
            return null;
        }

        final String terminalId = JtProtocolUtils.getBcd(input, 5, 10);

        final int flowId = JtProtocolUtils.getWord(input, 15);

        final int totalPackageCount = JtProtocolUtils.getWord(input, 17);
        final int currentPackageNo = JtProtocolUtils.getWord(input, 19);

        // 构造缓存 key
        final String key = terminalId + "_" + msgId + "_" + totalPackageCount;

        // 实际消息体片段
        final int bodyStart = msgBodyStartIndex(msgBodyProps.versionIdentifier(), true);
        final int bodyLength = msgBodyProps.msgBodyLength();
        final ByteBuf msgBody = input.slice(bodyStart, bodyLength)
                // 这里 retain 一下(放缓存)
                .retain();

        // 更新/创建 pending package
        final PendingPackage pending = pendingPackages.computeIfAbsent(key, k -> new PendingPackage(totalPackageCount));

        synchronized (pending) {
            if (currentPackageNo >= 1 && currentPackageNo <= totalPackageCount) {
                if (pending.fragments[currentPackageNo - 1] != null) {
                    // 已收到该子包，释放旧的（防重复）
                    ReferenceCountUtil.safeRelease(pending.fragments[currentPackageNo - 1]);
                }
                log.info("收到分包：{}/{} key={}, hex={}", currentPackageNo, totalPackageCount, key, FormatUtils.toHexString(input));
                pending.fragments[currentPackageNo - 1] = msgBody;
            } else {
                // 无效序号，丢弃
                ReferenceCountUtil.safeRelease(msgBody);
                return null;
            }

            // 检查是否收齐
            boolean complete = true;
            for (ByteBuf f : pending.fragments) {
                if (f == null) {
                    complete = false;
                    break;
                }
            }

            if (complete) {
                // 示例性代码 只处理 2019 版
                log.info("分包已收齐：key={}", key);
                final Jt808ProtocolVersion protocolVersion = Jt808ProtocolVersion.VERSION_2019;
                // 合并所有分片
                final CompositeByteBuf compositeByteBuf = doMerge(pending, protocolVersion, msgId, terminalId, flowId);
                // 清理缓存
                pendingPackages.remove(key);
                return compositeByteBuf;
            } else {
                // 尚未收齐，返回 null 表示暂不处理
                return null;
            }
        }
    }

    /**
     * 所有子包的消息体拼接到一起； 重新构造一个消息头； 添加校验码
     * <p>
     * N 个子包 合并成一个包
     */
    private static CompositeByteBuf doMerge(PendingPackage pending, Jt808ProtocolVersion protocolVersion, int msgId, String terminalId, int flowId) {
        // TODO: 这里只处理了 2019 版
        final ByteBuf mergedBody = Unpooled.buffer();
        for (ByteBuf f : pending.fragments) {
            mergedBody.writeBytes(f);
            ReferenceCountUtil.safeRelease(f);
        }

        final ByteBuf header = ByteBufAllocator.DEFAULT.buffer();
        // bytes[0-2) 消息ID Word
        JtProtocolUtils.writeWord(header, msgId);

        // bytes[2-4) 消息体属性 Word
        final int newMsgBodyProps = JtProtocolUtils.generateMsgBodyPropsForJt808(
                mergedBody.readableBytes(),
                0,
                false,
                protocolVersion,
                0
        );
        JtProtocolUtils.writeWord(header, newMsgBodyProps);
        if (protocolVersion == Jt808ProtocolVersion.VERSION_2019) {
            header.writeByte(protocolVersion.getVersionBit());
        }
        // bytes[5-14) 终端手机号 BCD[10]
        // bytes[4-10) 终端手机号 BCD[6]
        JtProtocolUtils.writeBcd(header, terminalId);
        // bytes[15-17) 消息流水号  Word
        JtProtocolUtils.writeWord(header, flowId);

        final CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        compositeByteBuf.addComponent(true, header);
        compositeByteBuf.addComponent(true, mergedBody);
        // 随便写了个校验码
        compositeByteBuf.addComponent(true, Unpooled.buffer().writeByte(0));
        return compositeByteBuf;
    }

    // 内部类：待合并的包
    private static class PendingPackage {
        private final ByteBuf[] fragments;

        public PendingPackage(int total) {
            this.fragments = new ByteBuf[total];
        }
    }

    static int msgBodyStartIndex(int version, boolean hasSubPackage) {
        // 2011, 2013
        if (version == Jt808ProtocolVersion.VERSION_2013.getVersionBit()) {
            return hasSubPackage ? 16 : 12;
        }
        // 2019
        if (version == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return hasSubPackage ? 21 : 17;
        }
        throw new JtIllegalStateException("未知版本,version=" + version);
    }
}
