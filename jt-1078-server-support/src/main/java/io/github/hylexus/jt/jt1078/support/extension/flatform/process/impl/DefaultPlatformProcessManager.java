package io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl;

import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcess;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcessManager;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.exception.ProcessNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
public class DefaultPlatformProcessManager implements PlatformProcessManager {
    private final Map<String, DefaultPlatformProcess> instances = new HashMap<>();
    private final ExecutorService executorService;

    public DefaultPlatformProcessManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public DefaultPlatformProcess createProcess(String command) {
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        final DefaultPlatformProcess process = new DefaultPlatformProcess(uuid, command, exitedProcess -> {
            log.info("{} exited", exitedProcess);
            this.instances.remove(uuid);
            return exitedProcess;
        }, this.executor());

        this.instances.put(uuid, process);
        return process;
    }

    @Override
    public ExecutorService executor() {
        return this.executorService;
    }

    @Override
    public void destroyProcess(String id, Consumer<PlatformProcess> action) throws ProcessNotFoundException {
        final DefaultPlatformProcess process = this.instances.remove(id);
        if (process == null) {
            throw new ProcessNotFoundException("No DefaultPlatformProcess found with id " + id);
        }

        try {
            process.destroy();
        } finally {
            action.accept(process);
        }
    }

    @Override
    public Optional<PlatformProcess> getProcess(String id) {
        return Optional.ofNullable(this.instances.get(id));
    }

    @Override
    public Stream<DefaultPlatformProcessDescriber> list() {
        return this.instances.values()
                .stream()
                .map(DefaultPlatformProcessDescriber::of);
    }
}
