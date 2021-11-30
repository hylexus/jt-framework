package io.github.hylexus.jt.core;

public interface ReplaceableComponent extends OrderedComponent {

    default boolean shouldBeReplacedBy(OrderedComponent another) {
        // 数字越小优先级越高
        // 数字小的覆盖数字大的
        return this.getOrder() > another.getOrder();
    }

}
