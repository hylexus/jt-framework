package io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin;

import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0005;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.codec.impl.CaffeineJt808ResponseSubPackageStorage;
import io.github.hylexus.jt.utils.Jdk8Adapter;

import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Jt808RequestHandler
public class BuiltinRetransmissionHandler {

    private final Jt808SessionManager sessionManager;

    private final CaffeineJt808ResponseSubPackageStorage subPackageEventConsumer;

    public BuiltinRetransmissionHandler(Jt808SessionManager sessionManager, CaffeineJt808ResponseSubPackageStorage subPackageEventConsumer) {
        this.sessionManager = sessionManager;
        this.subPackageEventConsumer = subPackageEventConsumer;
    }

    @Jt808RequestHandlerMapping(msgType = 0x0005)
    public void processRetransmissionMsg(Jt808RequestEntity<BuiltinMsg0005> request) {
        final Jt808Session session = this.sessionManager.findByTerminalId(request.terminalId()).orElseThrow(Jdk8Adapter::optionalOrElseThrowEx);
        final BuiltinMsg0005 body = request.body();
        this.subPackageEventConsumer.getSubPackageMsg(
                request.terminalId(),
                body.getFirstSubPackageFlowId(),
                body.getPackageIdList().stream().map(BuiltinMsg0005.PackageId::getValue).collect(Collectors.toSet())
        ).forEach(session::sendMsgToClient);
    }
}
