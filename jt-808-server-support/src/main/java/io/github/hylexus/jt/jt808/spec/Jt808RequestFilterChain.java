package io.github.hylexus.jt.jt808.spec;

/**
 * 从 `org.springframework.web.server.WebFilterChain` 借鉴(抄袭)的。
 *
 * @see "org.springframework.web.server.WebFilterChain"
 * @see Jt808RequestFilter
 */
public interface Jt808RequestFilterChain {

    void filter(Jt808ServerExchange exchange);

}
