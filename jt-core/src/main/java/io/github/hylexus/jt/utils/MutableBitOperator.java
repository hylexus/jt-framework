
package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.annotation.Internal;

import java.util.function.Function;

@Internal
class MutableBitOperator implements BitOperator {
    private long value;

    MutableBitOperator(long value) {
        this.value = value;
    }

    @Override
    public long value() {
        return this.value;
    }

    @Override
    public BitOperator map(Function<Long, Long> mapper) {
        this.value = mapper.apply(this.value);
        return this;
    }
}