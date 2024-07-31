package io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl;

import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcess;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class DefaultPlatformProcessDescriber {

    private String id;

    private long pid;

    private boolean running;

    private ProcessInfo info;

    public static DefaultPlatformProcessDescriber of(PlatformProcess it) {
        final DefaultPlatformProcessDescriber describer = new DefaultPlatformProcessDescriber();
        describer.setId(it.uuid());
        // describer.setPid(it.process().pid());
        describer.setRunning(it.running());
        // final ProcessHandle.Info processInfo = it.process().info();
        // final DefaultPlatformProcessDescriber.ProcessInfo info = new DefaultPlatformProcessDescriber.ProcessInfo()
        //         .setCommand(processInfo.command().orElse(null))
        //         .setCommandLine(processInfo.commandLine().orElse(null))
        //         .setArguments(processInfo.arguments().orElse(null))
        //         .setStartInstant(processInfo.startInstant().orElse(null))
        //         .setTotalCpuDuration(processInfo.totalCpuDuration().orElse(null))
        //         .setUser(processInfo.user().orElse(null));
        // describer.setInfo(info);
        return describer;
    }

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class ProcessInfo {

        private String command;
        private String commandLine;
        private String[] arguments;
        private Instant startInstant;
        private Duration totalCpuDuration;
        private String user;
    }
}
