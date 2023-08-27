package io.github.hylexus.jt.dashboard.server.registry;

import io.github.hylexus.jt.core.registry.RegistryStore;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.proxy.InstanceIdGenerator;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 * <p>
 * 这个类是从 spring-boot-admin 中复制过来修改的。
 *
 * @see <a href="https://github.com/codecentric/spring-boot-admin">https://github.com/codecentric/spring-boot-admin</a>
 * @see <a href="https://github.com/codecentric/spring-boot-admin/blob/1433ca5b8343247075ff775d558c9a82341e2ac6/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/services/InstanceIdGenerator.java#L22">de.codecentric.boot.admin.server.services.InstanceIdGenerator</a>
 */
public abstract class InstanceRegistry<E extends JtInstance> {
    private final InstanceIdGenerator generator;
    private final RegistryStore<String, E> store;

    public InstanceRegistry(InstanceIdGenerator generator, RegistryStore<String, E> store) {
        this.generator = generator;
        this.store = store;
    }

    protected abstract E createInstance(String id, JtRegistration jtRegistration);

    public String register(JtRegistration registration) {
        Assert.notNull(registration, "'registration' must not be null");
        final String id = generator.generateId(registration);
        Assert.notNull(id, "'id' must not be null");
        store.put(id, createInstance(id, registration));
        return id;
    }

    public List<E> getInstances() {
        return store.values().collect(Collectors.toList());
    }

    public Optional<E> getInstance(String id) {
        return store.find(id);
    }

    public String deregister(String id) {
        final Optional<E> remove = store.remove(id);
        if (remove.isPresent()) {
            return id;
        }
        return null;
    }
}
