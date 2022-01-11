package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808SubPackageRequest;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;

/**
 * @author hylexus
 */
public class DefaultJt808RequestSubPackageEventListener implements Jt808RequestSubPackageEventListener {

    private final Jt808RequestSubPackageStorage subPackageStorage;

    public DefaultJt808RequestSubPackageEventListener(Jt808RequestSubPackageStorage subPackageStorage) {
        this.subPackageStorage = subPackageStorage;
    }

    @Override
    public void onSubPackage(Jt808SubPackageRequest subPackageRequest) {
        this.subPackageStorage.saveSubPackage(subPackageRequest);
    }
}
