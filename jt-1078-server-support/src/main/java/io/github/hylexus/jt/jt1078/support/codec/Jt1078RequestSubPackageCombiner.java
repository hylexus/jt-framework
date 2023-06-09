package io.github.hylexus.jt.jt1078.support.codec;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;

import java.util.Optional;

/**
 * 子包合并器
 *
 * @see io.github.hylexus.jt.jt1078.support.codec.impl.CaffeineJt1078RequestSubPackageCombiner
 * @see #NO_OPS
 * @see io.github.hylexus.jt.jt1078.support.dispatcher.impl.DefaultPublisherBasedJt1078RequestHandler#handle(Jt1078Request)
 */
public interface Jt1078RequestSubPackageCombiner {

    /**
     * 尝试合并子包
     *
     * <ol>
     *     <li>
     *         如果能通过当前请求组装出一个完整的包，就返回组装之后的完整的包
     *     </li>
     *     <li>
     *         否则就返回 {@link Optional#empty()}, <span color="red">不要</span>返回 {@code null}
     *     </li>
     * </ol>
     *
     * @param request 当前请求(有可能是原子包，也有可能是子包)
     * @return 合并之后的新请求
     * @see io.github.hylexus.jt.jt1078.support.codec.impl.CaffeineJt1078RequestSubPackageCombiner
     */
    Optional<Jt1078Request> tryCombine(Jt1078Request request);

    /**
     * 空实现: 分包请求不会自动合并(意味着下游可能会收到尚未合并的子包)
     *
     * @see io.github.hylexus.jt.jt1078.support.dispatcher.impl.DefaultPublisherBasedJt1078RequestHandler#handle(Jt1078Request)
     */
    Jt1078RequestSubPackageCombiner NO_OPS = request -> {
        request.retain();
        return Optional.of(request);
    };
}
