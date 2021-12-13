package io.github.hylexus.jt.jt808.spec;

import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808SubPackageRequest extends Jt808Request {

    Jt808SubPackage subPackage();

    interface Jt808SubPackage {

        String terminalId();

        int msgId();

        int totalSubPackageCount();

        int currentPackageNo();

        ByteBuf body();

        String toString();
    }

}
