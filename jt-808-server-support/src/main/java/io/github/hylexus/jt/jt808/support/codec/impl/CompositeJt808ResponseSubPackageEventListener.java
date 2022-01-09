package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author hylexus
 */
@Slf4j
public class CompositeJt808ResponseSubPackageEventListener implements Jt808ResponseSubPackageEventListener {

    private final List<Jt808ResponseSubPackageEventListener> delegates;

    public CompositeJt808ResponseSubPackageEventListener(List<Jt808ResponseSubPackageEventListener> delegates) {
        this.delegates = delegates;
    }

    @Override
    public void onSubPackage(Jt808Response.Jt808ResponseSubPackage responseSubPackage) {
        for (Jt808ResponseSubPackageEventListener it : this.delegates) {
            try {
                it.onSubPackage(responseSubPackage);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
