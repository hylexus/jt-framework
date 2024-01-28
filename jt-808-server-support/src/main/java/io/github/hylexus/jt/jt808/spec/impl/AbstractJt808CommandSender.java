package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.exception.JtCommunicationException;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt.jt808.spec.CommandWaitingPool;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.session.InternalJt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.exception.Jt808EncodeException;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.TimeUnit;

/**
 * Created At 2020-03-11 7:30 下午
 *
 * @author hylexus
 */
public abstract class AbstractJt808CommandSender implements Jt808CommandSender {

    protected final CommandWaitingPool commandWaitingPool = CommandWaitingPool.getInstance();
    protected final InternalJt808SessionManager sessionManager;

    protected AbstractJt808CommandSender(InternalJt808SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public <T> T sendCommandAndWaitingForReply(Jt808CommandKey key, Jt808Response response, Long timeout, TimeUnit timeUnit)
            throws JtCommunicationException, InterruptedException {

        final Jt808Session session = getSession(key.terminalId());
        final ByteBuf byteBuf = this.encode(session, response);
        return sendAndWait(key, session, timeout, timeUnit, byteBuf);
    }

    @Override
    public <T> T sendCommandAndWaitingForReply(Jt808CommandKey key, ByteBuf byteBuf, long timeout, TimeUnit timeUnit)
            throws JtCommunicationException, InterruptedException {

        return sendAndWait(key, getSession(key.terminalId()), timeout, timeUnit, byteBuf);
    }

    @Override
    public <T> T sendCommandAndWaitingForReply(Jt808CommandKey key, Object entity, Long timeout, TimeUnit timeUnit)
            throws JtCommunicationException, InterruptedException {

        final Jt808Session session = this.getSession(key.terminalId());
        final ByteBuf byteBuf = this.encode(session, entity, key.serverFlowId().orElse(session.nextFlowId()));
        return sendAndWait(key, session, timeout, timeUnit, byteBuf);
    }

    private <T> T sendAndWait(Jt808CommandKey key, Jt808Session session, Long timeout, TimeUnit timeUnit, ByteBuf byteBuf) throws InterruptedException {
        session.sendMsgToClient(byteBuf);

        @SuppressWarnings("unchecked") final T result = (T) commandWaitingPool.waitingForKey(key, timeout, timeUnit);
        return result;
    }

    @Override
    public void sendCommand(String terminalId, ByteBuf byteBuf) throws JtCommunicationException {
        final Jt808Session session = getSession(terminalId);
        session.sendMsgToClient(byteBuf);
    }

    @Override
    public void sendCommand(Jt808Response response) throws JtCommunicationException {
        final Jt808Session session = getSession(response.terminalId());
        final ByteBuf byteBuf = this.encode(session, response);
        session.sendMsgToClient(byteBuf);
    }

    @Override
    public void sendCommand(String terminalId, Object response) throws JtCommunicationException {
        final Jt808Session session = getSession(terminalId);
        final ByteBuf byteBuf = this.encode(session, response, session.nextFlowId());
        session.sendMsgToClient(byteBuf);
    }

    private Jt808Session getSession(String terminalId) {
        return sessionManager.findByTerminalId(terminalId)
                .orElseThrow(() -> new JtSessionNotFoundException("Session Not Found with TerminalId : [" + terminalId + "]", terminalId));
    }

    protected abstract ByteBuf encode(Jt808Session session, Jt808Response response) throws Jt808EncodeException;

    protected abstract ByteBuf encode(Jt808Session session, Object response, int serverFlowId) throws Jt808EncodeException;
}
