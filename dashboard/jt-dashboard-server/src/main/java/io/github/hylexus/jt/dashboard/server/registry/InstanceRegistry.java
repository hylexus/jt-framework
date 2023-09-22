package io.github.hylexus.jt.dashboard.server.registry;

import io.github.hylexus.jt.core.registry.RegistryStore;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.proxy.InstanceIdGenerator;
import io.github.hylexus.jt.dashboard.server.service.JtInstanceStatusConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
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
@Slf4j
public abstract class InstanceRegistry<E extends JtInstance> {
    private final InstanceIdGenerator generator;
    private final RegistryStore<String, E> store;
    private final JtInstanceStatusConverter instanceStatusConverter;

    public InstanceRegistry(InstanceIdGenerator generator, RegistryStore<String, E> store, JtInstanceStatusConverter instanceStatusConverter) {
        this.generator = generator;
        this.store = store;
        this.instanceStatusConverter = instanceStatusConverter;
    }

    protected abstract E createInstance(String id, JtRegistration jtRegistration, LocalDateTime now);

    public String register(JtRegistration registration) {
        Assert.notNull(registration, "'registration' must not be null");
        final String id = generator.generateId(registration);
        Assert.notNull(id, "'id' must not be null");
        return this.store.compute(id, (key, instance) -> {
            final LocalDateTime now = LocalDateTime.now();
            if (instance == null) {
                instance = createInstance(id, registration, now);
            }
            instance.setStatus(instanceStatusConverter.convert(instance));
            instance.getRegistration().setUpdatedAt(now);
            log.debug("Compute instance {} ", instance);
            return instance;
        }).getInstanceId();
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
            log.debug("Instance deregister, instanceId = {}", id);
            return id;
        }
        return null;
    }
}
