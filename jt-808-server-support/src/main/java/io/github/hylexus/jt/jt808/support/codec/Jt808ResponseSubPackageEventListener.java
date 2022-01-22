package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Response;

/**
 * 当某个响应消息超过 {@link Jt808Response#maxPackageSize()} 阈值，导致自动分包之后会回调该接口.
 *
 * @author hylexus
 * @see io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType#CLIENT_RETRANSMISSION
 * @see io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType#SERVER_RETRANSMISSION
 */
@FunctionalInterface
public interface Jt808ResponseSubPackageEventListener extends OrderedComponent {

    /**
     * @param responseSubPackage 分包消息 实现类不应该直接消费消息,而是 copy() 一份
     */
    void onSubPackage(Jt808Response.Jt808ResponseSubPackage responseSubPackage);

}
