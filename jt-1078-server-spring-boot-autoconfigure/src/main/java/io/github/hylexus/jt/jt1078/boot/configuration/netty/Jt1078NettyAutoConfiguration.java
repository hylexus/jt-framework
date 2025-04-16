package io.github.hylexus.jt.jt1078.boot.configuration.netty;

import io.github.hylexus.jt.jt1078.boot.configuration.BuiltinJt1078ServerNettyConfigure;
import io.github.hylexus.jt.jt1078.boot.props.Jt1078ServerProps;
import io.github.hylexus.jt.jt1078.boot.props.msg.processor.MsgProcessorExecutorGroupProps;
import io.github.hylexus.jt.jt1078.boot.props.server.Jt1078NettyTcpServerProps;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078NettyTcpServer;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078ServerNettyConfigure;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078TerminalHeatBeatHandler;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import static io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.BEAN_NAME_NETTY_HANDLER_NAME_1078_HEART_BEAT;

@Slf4j
public class Jt1078NettyAutoConfiguration {

    private final Jt1078ServerProps serverProps;

    public Jt1078NettyAutoConfiguration(Jt1078ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    @Bean
    @ConditionalOnMissingBean(Jt1078ServerNettyConfigure.class)
    public Jt1078ServerNettyConfigure jt1078ServerNettyConfigure(
            @Qualifier(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP) EventExecutorGroup eventExecutorGroup,
            @Qualifier(BEAN_NAME_NETTY_HANDLER_NAME_1078_HEART_BEAT) Jt1078TerminalHeatBeatHandler heatBeatHandler,
            Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler) {

        return new BuiltinJt1078ServerNettyConfigure(
                serverProps,
                heatBeatHandler, eventExecutorGroup,
                jt1078DispatcherChannelHandler
        );
    }

    @Bean(name = BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    @ConditionalOnMissingBean(name = BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    public EventExecutorGroup eventExecutorGroup() {

        final MsgProcessorExecutorGroupProps poolProps = serverProps.getMsgProcessor().getExecutorGroup();
        final DefaultThreadFactory threadFactory = new DefaultThreadFactory(poolProps.getPoolName());

        log.info("Jt1078MsgProcessorConfig = {}", poolProps);
        return new DefaultEventExecutorGroup(
                poolProps.getThreadCount(),
                threadFactory,
                poolProps.getMaxPendingTasks(),
                RejectedExecutionHandlers.reject()
        );
    }

    @Bean(BEAN_NAME_NETTY_HANDLER_NAME_1078_HEART_BEAT)
    @ConditionalOnMissingBean(name = BEAN_NAME_NETTY_HANDLER_NAME_1078_HEART_BEAT)
    public Jt1078TerminalHeatBeatHandler heatBeatHandler(Jt1078SessionManager sessionManager) {
        return new Jt1078TerminalHeatBeatHandler(sessionManager);
    }

    @Bean(initMethod = "doStart", destroyMethod = "doStop")
    @ConditionalOnMissingBean
    public Jt1078NettyTcpServer jt1078NettyTcpServer(Jt1078ServerNettyConfigure configure, JtServerNettyConfigure.ConfigurationProvider provider) {
        final Jt1078NettyTcpServer server = new Jt1078NettyTcpServer(
                "TcpServer(JT/T-1078)",
                configure,
                provider
        );

        final Jt1078NettyTcpServerProps props = this.serverProps.getServer();
        server.setPort(props.getPort());
        return server;
    }
}
