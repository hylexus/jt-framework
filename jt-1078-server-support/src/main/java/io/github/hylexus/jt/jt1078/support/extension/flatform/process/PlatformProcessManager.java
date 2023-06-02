package io.github.hylexus.jt.jt1078.support.extension.flatform.process;

import io.github.hylexus.jt.jt1078.support.extension.flatform.process.exception.ProcessNotFoundException;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl.DefaultPlatformProcessDescriber;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public interface PlatformProcessManager {

    PlatformProcess createProcess(String command);

    Optional<PlatformProcess> getProcess(String id);

    Stream<DefaultPlatformProcessDescriber> list();

    void destroyProcess(String id) throws ProcessNotFoundException;

    ExecutorService executor();
}
