package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;

/**
 * 遇到分包请求时会回调 {@link #saveSubPackage(Jt808Request)} 将分包暂存起来。
 * <p>
 * 实现类至少应该实现下面几个功能:
 *
 * <ol>
 *     <li>当所有子包都到达后，实现类应该负责将消息合并 &amp;&amp; 将合并后的完整消息使用 {@link Jt808RequestMsgDispatcher#doDispatch(Jt808Request)} 投递出去</li>
 *     <li>某些子包丢失未到达时应该自动发送 `0x8003` 消息给终端，要求终端重传某些子包</li>
 *     <li>长时间未到达服务端的子包应该及时回收掉, 最长暂存多久由具体实现类自行决定</li>
 * </ol>
 *
 * @author hylexus
 * @see Jt808RequestMsgDispatcher#doDispatch(Jt808Request)
 */
public interface Jt808RequestSubPackageStorage {

    /**
     * 分包请求到达时回调该方法，暂存分包请求。
     * <p>
     * 如有必要，实现类应该自行回收掉 {@link Jt808Request#body()} 和 {@link Jt808Request#rawByteBuf()}
     *
     * @param subPackage 分包请求
     */
    void saveSubPackage(Jt808Request subPackage);

    Jt808RequestSubPackageStorage NO_OPS = subPackage -> {
    };
}
