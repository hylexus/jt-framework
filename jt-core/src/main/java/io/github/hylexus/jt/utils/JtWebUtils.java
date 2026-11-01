/*
 * Copyright 2026 the original author or authors.
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

package io.github.hylexus.jt.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 按调用方指定的顺序提取代理请求头中的客户端地址候选值。
 * 仅处理请求头文字，不验证 IP 格式或代理信任关系；没有候选值时，由调用方回退到连接地址。
 * 请求头应由可信代理设置或覆盖，不能将任意客户端提供的值直接用于身份或权限判断。
 */
public class JtWebUtils {

    @FunctionalInterface
    public interface HttpRequestHeaderProvider {
        String get(String name);
    }

    /**
     * 依次读取 {@code X-Forwarded-For} 和 {@code X-Real-IP}。
     * 其他代理头需通过 {@link #getClientIp(HttpRequestHeaderProvider, String...)} 显式指定。
     *
     * @param headerProvider 按名称读取请求头的函数
     * @return 第一个可用的地址候选值；均不可用时为空
     */
    public static Optional<String> getClientIp(HttpRequestHeaderProvider headerProvider) {
        return getClientIp(headerProvider, "X-Forwarded-For", "X-Real-IP");
    }

    /**
     * 只读取指定的请求头，按参数顺序选择第一个可用的地址候选值。
     * 每个头只取第一个逗号之前的部分，并去除两端空白；空值、空文字和
     * {@code unknown}（忽略大小写）会被跳过。不会改取同一个头中的后续地址。
     *
     * @param headerProvider 按名称读取请求头的函数
     * @param headerNames    待读取的头名称及优先级；空数组表示不读取任何头
     * @return 第一个可用的地址候选值；均不可用时为空
     * @throws NullPointerException provider、头数组或实际读取的头名称为 null
     */
    public static Optional<String> getClientIp(HttpRequestHeaderProvider headerProvider, String... headerNames) {
        Objects.requireNonNull(headerProvider, "headerProvider");
        Objects.requireNonNull(headerNames, "headerNames");
        return Stream.of(headerNames)
                .map(name -> headerProvider.get(Objects.requireNonNull(name, "headerName")))
                .filter(Objects::nonNull)
                .map(value -> value.split(",", 2)[0].trim())
                .filter(value -> !value.isEmpty() && !"unknown".equalsIgnoreCase(value))
                .findFirst();
    }

}
