package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.jt808.spec.session.Jt808Session;

/**
 * @author hylexus
 */
public interface Jt808ServerExchange {

    Jt808Request request();

    Jt808Response response();

    Jt808Session session();
}
