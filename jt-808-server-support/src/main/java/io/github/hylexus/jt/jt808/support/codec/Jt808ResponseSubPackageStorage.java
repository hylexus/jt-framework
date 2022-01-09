package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public interface Jt808ResponseSubPackageStorage {

    void saveSubPackage(Jt808Response.Jt808ResponseSubPackage subPackage);

    Collection<ByteBuf> getSubPackageMsg(String terminalId, int firstFlowIdOfSubPackage, Collection<Integer> packageIds);

}
