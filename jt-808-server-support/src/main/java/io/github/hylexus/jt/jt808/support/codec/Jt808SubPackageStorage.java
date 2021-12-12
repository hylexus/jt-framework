package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.SubPackageSupportedJt808Request;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808SubPackageStorage {

    boolean isAllSubPackagesArrived(SubPackageSupportedJt808Request request);

    void storeSubPackage(SubPackageSupportedJt808Request request);

    ByteBuf getAllSubPackages(SubPackageSupportedJt808Request request);

}
