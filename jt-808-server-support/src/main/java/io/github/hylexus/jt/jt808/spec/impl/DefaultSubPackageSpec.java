package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;

/**
 * @author hylexus
 */
public class DefaultSubPackageSpec implements Jt808MsgHeader.SubPackageSpec {
    private final int totalSubPackageCount;
    private final int currentPackageNo;

    public DefaultSubPackageSpec(int totalSubPackageCount, int currentPackageNo) {
        this.totalSubPackageCount = totalSubPackageCount;
        this.currentPackageNo = currentPackageNo;
    }

    @Override
    public int totalSubPackageCount() {
        return totalSubPackageCount;
    }

    @Override
    public int currentPackageNo() {
        return currentPackageNo;
    }

    public static class DefaultSubPackageSpecBuilder {
        private int totalSubPackageCount;
        private int currentPackageNo;

        public DefaultSubPackageSpecBuilder withTotalSubPackageCount(int totalSubPackageCount) {
            this.totalSubPackageCount = totalSubPackageCount;
            return this;
        }

        public DefaultSubPackageSpecBuilder withCurrentPackageNo(int currentPackageNo) {
            this.currentPackageNo = currentPackageNo;
            return this;
        }

        public DefaultSubPackageSpec build() {
            return new DefaultSubPackageSpec(totalSubPackageCount, currentPackageNo);
        }
    }
}
