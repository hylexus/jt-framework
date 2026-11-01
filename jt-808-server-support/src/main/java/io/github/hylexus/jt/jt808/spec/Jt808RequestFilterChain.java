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
 * Spring Framework comparison source: https://github.com/spring-projects/spring-framework/blob/v5.3.27/spring-web/src/main/java/org/springframework/web/server/WebFilterChain.java
 * Filter-chain API adapted for JT/T 808.
 * Local adaptations by hylexus; attribution restored 2026-10-02.
 * The comparison version is not a claim about the exact historical import.
 */

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
