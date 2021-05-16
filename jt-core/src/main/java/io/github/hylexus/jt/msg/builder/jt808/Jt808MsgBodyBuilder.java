package io.github.hylexus.jt.msg.builder.jt808;

import io.github.hylexus.jt.msg.builder.MsgBuilder;

/**
 * @author hylexus
 * @date 2020/12/6 4:26 下午
 */
public class Jt808MsgBodyBuilder extends MsgBuilder {
    protected Jt808MsgBodyBuilder() {
        super();
    }

    protected Jt808MsgBodyBuilder(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }

    public static Jt808MsgBodyBuilder newBuilder() {
        return new Jt808MsgBodyBuilder();
    }

    public static Jt808MsgBodyBuilder newBuilder(int initialCapacity, int maxCapacity) {
        return new Jt808MsgBodyBuilder(initialCapacity, maxCapacity);
    }
}
