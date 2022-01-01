package io.github.hylexus.jt.jt808.spec;

import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;

/**
 * @author hylexus
 */
public interface Jt808SubPackageRequest extends Jt808Request {

    Jt808SubPackage subPackage();

    interface Jt808SubPackage {

        String terminalId();

        int msgId();

        int flowId();

        int totalSubPackageCount();

        int currentPackageNo();

        ByteBuf body();

        String toString();

        LocalDateTime createdAt();
    }

}
