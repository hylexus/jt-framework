/*
 * Copyright 2014-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.jt.dashboard.server.proxy;

import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/1433ca5b8343247075ff775d558c9a82341e2ac6/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/web/HttpHeaderFilter.java#L35">de.codecentric.boot.admin.server.web.HttpHeaderFilter</a>
 */
public class HttpHeaderFilter {

    private static final String[] HOP_BY_HOP_HEADERS = new String[]{
            "Host", "Connection", "Keep-Alive",
            "Proxy-Authenticate", "Proxy-Authorization",
            "TE", "Trailer", "Transfer-Encoding", "Upgrade",
            "X-Application-Context"
    };

    private final Set<String> ignoredHeaders;

    public HttpHeaderFilter(Set<String> ignoredHeaders) {
        this.ignoredHeaders = Stream.concat(ignoredHeaders.stream(), Arrays.stream(HOP_BY_HOP_HEADERS))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public HttpHeaders filterHeaders(HttpHeaders headers) {
        HttpHeaders filtered = new HttpHeaders();
        filtered.putAll(headers.entrySet()
                .stream()
                .filter((e) -> this.includeHeader(e.getKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)));
        return filtered;
    }

    private boolean includeHeader(String header) {
        return !this.ignoredHeaders.contains(header.toLowerCase());
    }

}
