package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author hylexus
 */
@Slf4j
public class CompositeJt808RequestSubPackageEventListener implements Jt808RequestSubPackageEventListener {

    private final List<Jt808RequestSubPackageEventListener> delegates;

    public CompositeJt808RequestSubPackageEventListener(List<Jt808RequestSubPackageEventListener> delegates) {
        this.delegates = delegates;
    }

    @Override
    public void onSubPackage(Jt808Request subPackageRequest) {
        for (Jt808RequestSubPackageEventListener delegate : this.delegates) {
            try {
                delegate.onSubPackage(subPackageRequest);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
