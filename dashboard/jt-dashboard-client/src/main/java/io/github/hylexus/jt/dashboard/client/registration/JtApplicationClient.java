package io.github.hylexus.jt.dashboard.client.registration;


/**
 * 这个类(及其实现类)是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类(及其实现类)是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类(及其实现类)是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/83db63e82e916357d36b1e6b4d552e1b6506ecc9/spring-boot-admin-client/src/main/java/de/codecentric/boot/admin/client/registration/RegistrationClient.java#L19">de.codecentric.boot.admin.client.registration.RegistrationClient</a>
 */
public interface JtApplicationClient {

    String register(String dashboardUrl, JtApplication application);

    void deregister(String dashboardUrl, String id);

}
