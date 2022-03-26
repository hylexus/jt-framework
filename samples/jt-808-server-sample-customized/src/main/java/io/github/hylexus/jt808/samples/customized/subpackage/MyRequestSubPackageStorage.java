package io.github.hylexus.jt808.samples.customized.subpackage;

import io.github.hylexus.jt.jt808.support.codec.impl.CaffeineJt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author hylexus
 */
public class MyRequestSubPackageStorage extends CaffeineJt808RequestSubPackageStorage {
    public MyRequestSubPackageStorage(ByteBufAllocator allocator, StorageConfig subPackageStorageConfig) {
        super(allocator, subPackageStorageConfig);
    }
}
