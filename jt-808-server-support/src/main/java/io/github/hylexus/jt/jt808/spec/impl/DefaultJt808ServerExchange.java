package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
public class DefaultJt808ServerExchange implements Jt808ServerExchange {

    private final Jt808Request request;
    private final Jt808Response response;
    private final Jt808Session session;
    private final Map<String, Object> attributes;

    public DefaultJt808ServerExchange(Jt808Request request, Jt808Response response, Jt808Session session) {
        this.request = request;
        this.response = response;
        this.session = session;
        this.attributes = new ConcurrentHashMap<>();
    }

    @Override
    public Jt808Request request() {
        return this.request;
    }

    @Override
    public Jt808Response response() {
        return this.response;
    }

    @Override
    public Jt808Session session() {
        return this.session;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
