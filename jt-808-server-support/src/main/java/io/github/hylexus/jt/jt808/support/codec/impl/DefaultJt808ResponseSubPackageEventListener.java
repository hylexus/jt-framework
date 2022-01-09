package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;

/**
 * @author hylexus
 */
public class DefaultJt808ResponseSubPackageEventListener implements Jt808ResponseSubPackageEventListener {

    private final Jt808ResponseSubPackageStorage subPackageStorage;

    public DefaultJt808ResponseSubPackageEventListener(Jt808ResponseSubPackageStorage subPackageStorage) {
        this.subPackageStorage = subPackageStorage;
    }

    @Override
    public void onSubPackage(Jt808Response.Jt808ResponseSubPackage responseSubPackage) {
        this.subPackageStorage.saveSubPackage(responseSubPackage);
    }
}
