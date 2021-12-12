package io.github.hylexus.jt.jt808.request;

import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;

/**
 * @author hylexus
 */
public interface Jt808ServerExchange {

    Jt808Request request();

    Jt808Response response();

    Jt808Session session();
}
