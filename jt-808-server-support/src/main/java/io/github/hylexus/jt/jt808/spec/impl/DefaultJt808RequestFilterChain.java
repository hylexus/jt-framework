/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Spring Framework comparison source: https://github.com/spring-projects/spring-framework/blob/v5.3.27/spring-web/src/main/java/org/springframework/web/server/handler/DefaultWebFilterChain.java
 * Filter-chain construction adapted to synchronous JT/T 808 handlers.
 * Local adaptations by hylexus; attribution restored 2026-10-02.
 * The comparison version is not a claim about the exact historical import.
 */

package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.Jt808RequestFilter;
import io.github.hylexus.jt.jt808.spec.Jt808RequestFilterChain;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class DefaultJt808RequestFilterChain implements Jt808RequestFilterChain {
    private final List<Jt808RequestFilter> filterList;
    private final Jt808RequestFilter currentFilter;
    private final DefaultJt808RequestFilterChain chain;
    private final Jt808DispatcherHandler handler;

    public DefaultJt808RequestFilterChain(List<Jt808RequestFilter> filters, Jt808DispatcherHandler handler) {
        this.handler = handler;
        this.filterList = Collections.unmodifiableList(filters);
        final DefaultJt808RequestFilterChain filterChain = initFilterChain(filters, handler);
        this.currentFilter = filterChain.currentFilter;
        this.chain = filterChain.chain;
    }

    private DefaultJt808RequestFilterChain(
            List<Jt808RequestFilter> filters, Jt808DispatcherHandler handler,
            @Nullable Jt808RequestFilter currentFilter, @Nullable DefaultJt808RequestFilterChain chain) {
        this.currentFilter = currentFilter;
        this.handler = handler;
        this.filterList = filters;
        this.chain = chain;
    }

    static DefaultJt808RequestFilterChain initFilterChain(List<Jt808RequestFilter> filters, Jt808DispatcherHandler handler) {
        DefaultJt808RequestFilterChain chain = new DefaultJt808RequestFilterChain(filters, handler, null, null);
        final ListIterator<Jt808RequestFilter> iterator = filters.listIterator(filters.size());
        while (iterator.hasPrevious()) {
            chain = new DefaultJt808RequestFilterChain(filters, handler, iterator.previous(), chain);
        }
        return chain;
    }

    @Override
    public void filter(Jt808ServerExchange exchange) {
        if (this.currentFilter != null && this.chain != null) {
            // invokeFilter(this.currentFilter, this.chain, exchange);
            this.currentFilter.filter(exchange, this.chain);
        } else {
            this.handler.handleRequest(exchange);
        }
    }

    private void invokeFilter(Jt808RequestFilter current, DefaultJt808RequestFilterChain chain, Jt808ServerExchange exchange) {
        current.filter(exchange, chain);
        this.currentFilter.filter(exchange, this.chain);
    }

    public List<Jt808RequestFilter> getFilterList() {
        return filterList;
    }
}
