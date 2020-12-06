package io.github.hylexus.jt.builder.jt808;

import io.github.hylexus.jt.builder.MsgBuilder;

/**
 * @author hylexus
 * @date 2020/12/6 4:26 下午
 */
public class Jt808MsgBodyBuilder extends MsgBuilder {
    private Jt808MsgBodyBuilder() {
    }

    public static Jt808MsgBodyBuilder newBuilder() {
        return new Jt808MsgBodyBuilder();
    }
}
