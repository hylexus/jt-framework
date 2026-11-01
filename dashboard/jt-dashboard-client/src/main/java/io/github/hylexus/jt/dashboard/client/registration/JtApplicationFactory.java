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

// Adapted for jt-framework by hylexus; see the upstream source link below.

package io.github.hylexus.jt.dashboard.client.registration;

/**
 * 本接口改编自 Spring Boot Admin 的 ApplicationFactory。
 * 实现类的来源需按具体文件核对。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/83db63e82e916357d36b1e6b4d552e1b6506ecc9/spring-boot-admin-client/src/main/java/de/codecentric/boot/admin/client/registration/ApplicationFactory.java#L25">de.codecentric.boot.admin.client.registration.ApplicationFactory</a>
 */
public interface JtApplicationFactory {

    JtApplication createApplication();

}
