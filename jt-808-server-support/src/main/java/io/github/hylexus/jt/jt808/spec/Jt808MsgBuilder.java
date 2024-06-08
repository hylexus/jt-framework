package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.annotation.UnstableApi;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.ByteBufJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.EntityJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.RebuildableByteBufJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
@UnstableApi
public interface Jt808MsgBuilder<B, S extends Jt808MsgBuilder<B, S>> {

    /**
     * <strong color='blue'>支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre>{@code
     * final Jt808MsgEncoder encoder = new DefaultJt808MsgEncoder(
     *         ByteBufAllocator.DEFAULT,
     *         new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
     *         responseSubPackage -> {
     *         },
     *         Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
     *         Jt808MsgEncryptionHandler.NO_OPS
     * );
     * final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer();
     * try (var builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator, encoder, originalBuf)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *
     *     doSomeProcess(result);
     *
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     *
     *     final ByteBuf result2 = builder.build();
     *     Assertions.assertEquals(1, result2.refCnt());
     *
     *     doSomeProcess(result2);
     *
     *     result2.release();
     *     Assertions.assertEquals(0, result2.refCnt());
     * }
     * // try-with-resources 会自动释放 originalBuf
     * Assertions.assertEquals(0, originalBuf.refCnt());
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf body} 临时变量。
     * @see #release()
     * @see RebuildableByteBufJt808MsgBuilder#close()
     * @since 2.1.4
     */
    static RebuildableByteBufJt808MsgBuilder newRebuildableByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder, ByteBuf body) {
        return new RebuildableByteBufJt808MsgBuilder(flowIdGenerator, encoder, body);
    }

    /**
     * <strong color='blue'>支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre>{@code
     * final Jt808MsgEncoder encoder = new DefaultJt808MsgEncoder(
     *         ByteBufAllocator.DEFAULT,
     *         new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
     *         responseSubPackage -> {
     *         },
     *         Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
     *         Jt808MsgEncryptionHandler.NO_OPS
     * );
     * try (var builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator, encoder)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *
     *     doSomeProcess(result);
     *
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     *
     *     final ByteBuf result2 = builder.build();
     *     Assertions.assertEquals(1, result2.refCnt());
     *
     *     doSomeProcess(result2);
     *
     *     result2.release();
     *     Assertions.assertEquals(0, result2.refCnt());
     * }
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf} 临时变量。
     * @see #release()
     * @see RebuildableByteBufJt808MsgBuilder#close()
     * @since 2.1.4
     */
    static RebuildableByteBufJt808MsgBuilder newRebuildableByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder) {
        return new RebuildableByteBufJt808MsgBuilder(flowIdGenerator, encoder);
    }

    /**
     * <strong color='blue'>支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre>{@code
     * final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer();
     * try (var builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator, originalBuf)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *
     *     doSomeProcess(result);
     *
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     *
     *     final ByteBuf result2 = builder.build();
     *     Assertions.assertEquals(1, result2.refCnt());
     *
     *     doSomeProcess(result2);
     *
     *     result2.release();
     *     Assertions.assertEquals(0, result2.refCnt());
     * }
     * // try-with-resources 会自动释放 originalBuf
     * Assertions.assertEquals(0, originalBuf.refCnt());
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf body} 临时变量。
     * @see #release()
     * @see RebuildableByteBufJt808MsgBuilder#close()
     * @since 2.1.4
     */
    static RebuildableByteBufJt808MsgBuilder newRebuildableByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBuf body) {
        return new RebuildableByteBufJt808MsgBuilder(flowIdGenerator, body);
    }

    /**
     * <strong color='blue'>支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre>{@code
     * try (var builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *     doSomeProcess(result);
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     *
     *     final ByteBuf result2 = builder.build();
     *     Assertions.assertEquals(1, result2.refCnt());
     *     doSomeProcess(result2);
     *     result2.release();
     *     Assertions.assertEquals(0, result2.refCnt());
     * }
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf} 临时变量。
     * @see #release()
     * @see RebuildableByteBufJt808MsgBuilder#close()
     * @since 2.1.4
     */
    static RebuildableByteBufJt808MsgBuilder newRebuildableByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator) {
        return new RebuildableByteBufJt808MsgBuilder(flowIdGenerator);
    }

    /**
     * <strong color='red'>不支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre>{@code
     * final ByteBuf originalBuf = ...;
     * try (var builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, originalBuf)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *
     *     doSomeProcess(result);
     *
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     * }
     * // try-with-resources 会自动释放 originalBuf
     * Assertions.assertEquals(0, originalBuf.refCnt());
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf body} 临时变量。
     * @see #release()
     * @see ByteBufJt808MsgBuilder#close()
     */
    static ByteBufJt808MsgBuilder newByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBuf body) {
        return new ByteBufJt808MsgBuilder(flowIdGenerator, body);
    }

    /**
     * <strong color='red'>不支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre> {@code
     * try (var builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *
     *     doSomeProcess(result);
     *
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     * }
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf} 临时变量。
     * @see #release()
     * @see ByteBufJt808MsgBuilder#close()
     * @since 2.1.4
     */
    static ByteBufJt808MsgBuilder newByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator) {
        return new ByteBufJt808MsgBuilder(flowIdGenerator);
    }

    /**
     * <strong color='red'>不支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre>{@code
     * final Jt808MsgEncoder encoder = new DefaultJt808MsgEncoder(
     *         ByteBufAllocator.DEFAULT,
     *         new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
     *         responseSubPackage -> {
     *         },
     *         Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
     *         Jt808MsgEncryptionHandler.NO_OPS
     * );
     * final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer();
     * try (var builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, encoder, originalBuf)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *
     *     doSomeProcess(result);
     *
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     * }
     * // try-with-resources 会自动释放 originalBuf
     * Assertions.assertEquals(0, originalBuf.refCnt());
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf body} 临时变量。
     */
    static ByteBufJt808MsgBuilder newByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder, ByteBuf body) {
        return new ByteBufJt808MsgBuilder(flowIdGenerator, encoder, body);
    }

    /**
     * <strong color='red'>不支持</strong> 重复调用 {@link #build()} 方法
     * <p>
     * 推荐在 {@code try-with-resources} 块中使用：
     * <pre>{@code
     * final Jt808MsgEncoder encoder = new DefaultJt808MsgEncoder(
     *         ByteBufAllocator.DEFAULT,
     *         new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
     *         responseSubPackage -> {
     *         },
     *         Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
     *         Jt808MsgEncryptionHandler.NO_OPS
     * );
     * try (var builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, encoder)) {
     *     builder.version(...)
     *             .msgId(...)
     *             .terminalId(...)
     *             .body(writer -> writer.writeDWord(1)
     *                     .writeWord(2)
     *                     .writeString("abc")
     *             );
     *     final ByteBuf result = builder.build();
     *     Assertions.assertEquals(1, result.refCnt());
     *
     *     doSomeProcess(result);
     *
     *     result.release();
     *     Assertions.assertEquals(0, result.refCnt());
     * }
     * }</pre>
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='red'>需要手动调用</strong> {@link #release()} 方法以释放内部用到的 {@code ByteBuf} 临时变量。
     * @since 2.1.4
     */
    static ByteBufJt808MsgBuilder newByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder) {
        return new ByteBufJt808MsgBuilder(flowIdGenerator, encoder);
    }

    /**
     * <strong color='blue'>支持</strong> 重复调用 {@link #build()} 方法
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='blue'>无需调用</strong> {@link #release()} 方法。
     */
    static EntityJt808MsgBuilder newEntityBuilder(Jt808FlowIdGenerator flowIdGenerator) {
        return new EntityJt808MsgBuilder(flowIdGenerator);
    }

    /**
     * <strong color='blue'>支持</strong> 重复调用 {@link #build()} 方法
     *
     * @return 方法返回的构建结果需要调用方自己 <strong color='blue'>在恰当的时机释放</strong>。
     * @apiNote 这个实现类 <strong color='blue'>无需调用</strong> {@link #release()} 方法。
     */
    static EntityJt808MsgBuilder newEntityBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder) {
        return new EntityJt808MsgBuilder(flowIdGenerator, encoder);
    }

    S version(Jt808ProtocolVersion version);

    S msgId(int msgId);

    default S msgId(MsgType msgType) {
        return this.msgId(msgType.getMsgId());
    }

    S terminalId(String terminalId);

    /**
     * @since 2.1.4
     */
    S encryptionType(int encType);

    /**
     * @since 2.1.4
     */
    default S encryptionType(Jt808MsgEncryptionType encType) {
        return this.encryptionType(encType.intValue());
    }

    S body(B body);

    B body();

    S maxPackageSize(int maxPackageSize);

    S reversedBit15InHeader(byte reversedBit15InHeader);

    S release();

    ByteBuf build();

    default String toHexString() {
        ByteBuf byteBuf = null;
        try {
            byteBuf = this.build();
            return HexStringUtils.byteBufToString(byteBuf);
        } catch (Exception e) {
            if (byteBuf != null) {
                JtProtocolUtils.release(byteBuf);
            }
            throw e;
        } finally {
            JtProtocolUtils.release(byteBuf);
        }
    }
}
