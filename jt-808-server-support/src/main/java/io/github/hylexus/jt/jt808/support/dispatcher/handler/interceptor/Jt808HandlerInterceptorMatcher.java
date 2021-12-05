
package io.github.hylexus.jt.jt808.support.dispatcher.handler.interceptor;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;

import java.util.function.BiPredicate;

/**
 * @author hylexus
 */
public interface Jt808HandlerInterceptorMatcher extends BiPredicate<Jt808Request, Jt808Session> {

}
