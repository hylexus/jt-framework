package io.github.hylexus.jt.jt808.request;

import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface SubPackageSupportedJt808Request extends Jt808Request {

    Jt808SubPackage subPackage();

    interface Jt808SubPackage {
        int totalSubPackageCount();

        int currentPackageNo();

        ByteBuf body();
    }

}
