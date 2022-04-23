package io.github.hylexus.jt.jt1078.spec;

/**
 * @author hylexus
 */
public interface Jt1078MediaType {
    short value();

    /**
     * 标识位,确定是否是完整数据帧的边界
     */
    default boolean mark() {
        return ((this.value() >> 7) & 0b01) == 1;
    }

    default byte pt() {
        return (byte) (this.value() & 0b0111_1111);
    }

}
