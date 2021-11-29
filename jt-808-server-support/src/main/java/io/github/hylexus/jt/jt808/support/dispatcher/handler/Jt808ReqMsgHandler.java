package io.github.hylexus.jt.jt808.support.dispatcher.handler;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808MultipleVersionSupport;

/**
 * @author hylexus
 */
public interface Jt808ReqMsgHandler<T> extends Jt808MultipleVersionSupport {

    T handleMsg(Jt808Request request, Jt808Session session);

}
