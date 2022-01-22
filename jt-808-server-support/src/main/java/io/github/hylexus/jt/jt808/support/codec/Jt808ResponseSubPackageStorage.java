package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.Collections;

/**
 * 响应消息分包时会回调 {@link #saveSubPackage(Jt808Response.Jt808ResponseSubPackage)}
 *
 * @author hylexus
 * @see io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder#encode(Jt808Response, Jt808FlowIdGenerator)
 */
public interface Jt808ResponseSubPackageStorage {

    /**
     * 保存某个子包(响应消息分包时会回调),子包保存多久看具体实现类的实现逻辑
     *
     * @param subPackage 子包
     */
    void saveSubPackage(Jt808Response.Jt808ResponseSubPackage subPackage);

    /**
     * 分包补传(0x0005) 时可以通过这个方法获取之前发送过的子包
     *
     * @param terminalId              终端手机号
     * @param firstFlowIdOfSubPackage 分包消息中的第一个子包的流水号
     * @param packageIds              要求重传的子包ID
     * @return 编码后子包消息字节流
     */
    Collection<ByteBuf> getSubPackageMsg(String terminalId, int firstFlowIdOfSubPackage, Collection<Integer> packageIds);

    @DebugOnly
    Jt808ResponseSubPackageStorage NO_OPS_STORAGE = new Jt808ResponseSubPackageStorage() {
        @Override
        public void saveSubPackage(Jt808Response.Jt808ResponseSubPackage subPackage) {
        }

        @Override
        public Collection<ByteBuf> getSubPackageMsg(String terminalId, int firstFlowIdOfSubPackage, Collection<Integer> packageIds) {
            return Collections.emptyList();
        }
    };
}
