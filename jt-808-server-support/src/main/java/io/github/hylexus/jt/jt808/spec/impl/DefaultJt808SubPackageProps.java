package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true, fluent = true)
public class DefaultJt808SubPackageProps implements Jt808RequestHeader.Jt808SubPackageProps {
    private int totalSubPackageCount;
    private int currentPackageNo;

    public DefaultJt808SubPackageProps() {
    }

    public DefaultJt808SubPackageProps(int totalSubPackageCount, int currentPackageNo) {
        this.totalSubPackageCount = totalSubPackageCount;
        this.currentPackageNo = currentPackageNo;
    }

    @Override
    public int totalSubPackageCount() {
        return this.totalSubPackageCount;
    }

    @Override
    public int currentPackageNo() {
        return this.currentPackageNo;
    }

}
