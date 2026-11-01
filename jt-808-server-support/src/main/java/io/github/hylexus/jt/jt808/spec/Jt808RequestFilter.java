/*
 * Copyright 2002-2015 the original author or authors.
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
 * Spring Framework comparison source: https://github.com/spring-projects/spring-framework/blob/v5.3.27/spring-web/src/main/java/org/springframework/web/server/WebFilter.java
 * Filter API adapted for JT/T 808.
 * Local adaptations by hylexus; attribution restored 2026-10-02.
 * The comparison version is not a claim about the exact historical import.
 */

package io.github.hylexus.jt.jt808.spec;

/**
 * 从 `org.springframework.web.server.WebFilter` 借鉴(抄袭)的。
 *
 * @see "org.springframework.web.server.WebFilterChain"
 * @see "org.springframework.web.server.WebFilter"
 * @see Jt808RequestLifecycleListener
 * @see io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor
 * @see Jt808RequestFilterChain
 */
public interface Jt808RequestFilter {

    /**
     * <h3 color="red">注意</h3>
     * 该功能默认不开启，要使用 filter 需要配置 {@code jt808.plugins.request-filter.enabled = true}
     *
     * <p>
     * <p>
     * 能执行到这里就意味着: 请求整体结构已经解析完毕(分包请求也已经自动合并)。
     * <p>
     * <p>
     * {@code filter} 的逻辑执行完成之后才会执行消息体的解析。
     * <p>
     * <p>
     * 多个 {@code filter} 实例的执行顺序和 Spring 的 {@link org.springframework.core.annotation.Order @Order} 和 {@link org.springframework.core.Ordered Ordered} 一致。
     *
     * @param exchange 当前请求上下文
     * @param chain    过滤器链
     * @see org.springframework.core.annotation.Order
     * @see org.springframework.core.Ordered
     * @since 2.1.1
     */
    void filter(Jt808ServerExchange exchange, Jt808RequestFilterChain chain);

}
