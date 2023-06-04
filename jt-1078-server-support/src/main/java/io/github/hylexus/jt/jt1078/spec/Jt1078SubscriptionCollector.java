package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.PassThroughJt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.RawJt1078Subscription;

import java.util.Optional;

/**
 * 将 1078 的裸流转换成你自己需要的格式(FLV, MP4, ...)。
 * <p>
 * 内置了一个用来调试的实现类: {@link #RAW}, 该实现类不用做任何格式转换/封装，只会原样输出客户端的数据给订阅者。
 *
 * @param <S> 当前收集器的输出类型，实现类具体有什么属性看你自己情况定义，但是必须要实现 {@link Jt1078Subscription} 这个标记接口。
 * @author hylexus
 * @see #RAW
 * @see #PASS_THROUGH
 */
public interface Jt1078SubscriptionCollector<S extends Jt1078Subscription> {

    /**
     * 每个请求到达后都会根据订阅条件匹配，匹配到之后都会回调该方法。
     * <p>
     * 如果通过当前请求和历史请求(实现类自行缓存)能够组装成一个完整的包，就应该返回组装后的包(订阅者会收到组装后的包)。
     * <p>
     * 如果通过当前请求无法组成一个完整的包，就应该返回 {@link Optional#empty()}(一般来说应该缓存当前请求).
     * <p>
     * <h3 color="green">语义说明</h3>
     * <p>
     * 客户端的数据源源不断地上报，匹配到的数据最终都会来调用该方法。
     * <p>
     * 该方法可以实现流式编程中的以下几个算子的语义:
     * <ol>
     *     <li>
     *         map() 语义: 一对一的转换
     *     </li>
     *     <li>
     *         flatMap() 语义: 一个输入多个输出
     *     </li>
     *     <li>
     *         aggregate() 语义: 多个输入一个输出
     *     </li>
     *     <li>
     *         filter() 语义: 不符合条件的输入，直接返回 {@link Optional#empty()}
     *     </li>
     *     <li>
     *         ...
     *     </li>
     * </ol>
     * <p>
     * <h3 color="red">给实现类的建议</h3>
     * <ol>
     * <li>
     *     不满足条件(比如通过当前请求还无法组成一个完整的 Flv 包)时不要返回 {@code null}, 应该返回 {@link Optional#empty()}
     * </li>
     * <li>
     *     如果你的转换逻辑在另一个线程里，你应该从当前请求中克隆一份你需要的数据，并通过 {@link Jt1078Request#release()} 释放掉当前请求
     * </li>
     * </ol>
     *
     * @param request 当前请求
     * @return 组装后的音视频流
     * @see #RAW
     * @see Jt1078Publisher
     */
    Optional<S> collect(Jt1078Request request);

    /**
     * 原样输出 {@link Jt1078Request#body()} (不包括 {@link Jt1078Request#header()}) 部分，不做任何格式转换
     */
    Jt1078SubscriptionCollector<RawJt1078Subscription> RAW = request -> Optional.of(new RawJt1078Subscription(request));
    /**
     * 原样输出 {@link Jt1078Request#rawByteBuf()}(包括 {@link Jt1078Request#header()})，不做任何格式转换
     */
    Jt1078SubscriptionCollector<PassThroughJt1078Subscription> PASS_THROUGH = request -> Optional.of(new PassThroughJt1078Subscription(request));

}
