package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.jt808.spec.session.Jt808Session;

public interface MutableJt808Request extends Jt808Request {
    Jt808Request session(Jt808Session session);
}
