package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.request.SubPackageSupportedJt808Request;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt808SubPackage implements SubPackageSupportedJt808Request.Jt808SubPackage {
    private final int totalSubPackageCount;
    private final int currentPackageNo;
    private final ByteBuf body;

    public DefaultJt808SubPackage(int totalSubPackageCount, int currentPackageNo, ByteBuf body) {
        this.totalSubPackageCount = totalSubPackageCount;
        this.currentPackageNo = currentPackageNo;
        this.body = body;
    }

    @Override
    public int totalSubPackageCount() {
        return totalSubPackageCount;
    }

    @Override
    public int currentPackageNo() {
        return currentPackageNo;
    }

    @Override
    public ByteBuf body() {
        return body;
    }

}
