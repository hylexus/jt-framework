package io.github.hylexus.jt808.dispatcher.impl;

import io.github.hylexus.jt.command.CommandWaitingPool;
import io.github.hylexus.jt.command.Jt808CommandKey;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt808.dispatcher.CommandSender;
import io.github.hylexus.jt808.msg.resp.CommandMsg;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.session.Jt808SessionManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2020-03-11 7:30 下午
 */
public abstract class AbstractCommandSender implements CommandSender {

    private final CommandWaitingPool commandWaitingPool = CommandWaitingPool.getInstance();
    private final Jt808SessionManager sessionManager;

    protected AbstractCommandSender(Jt808SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Object sendCommandAndWaitingForReply(CommandMsg commandMsg, boolean withFlowId, Long timeout, TimeUnit timeUnit)
            throws IOException, InterruptedException {

        final String terminalId = commandMsg.getTerminalId();
        final MsgType expectedReplyMsgType = commandMsg.getExpectedReplyMsgType();

        final Jt808Session session = getSession(terminalId);
        final int serverFlowId = session.getCurrentFlowId();

        final byte[] bytes = this.encode(commandMsg, terminalId, serverFlowId);
        final Jt808CommandKey commandKey = withFlowId
                ? Jt808CommandKey.of(terminalId, expectedReplyMsgType, serverFlowId)
                : Jt808CommandKey.of(terminalId, expectedReplyMsgType);

        this.sendCommand(terminalId, bytes);
        return commandWaitingPool.waitingForKey(commandKey, timeout, timeUnit);
    }

    @Override
    public Object sendCommandAndWaitingForReply(Jt808CommandKey key, byte[] data, long timeout, TimeUnit timeUnit) throws InterruptedException {
        this.sendCommand(key.getTerminalId(), data);
        return commandWaitingPool.waitingForKey(key, timeout, timeUnit);
    }

    @Override
    public void sendCommand(String terminalId, byte[] data) throws InterruptedException {
        final Jt808Session session = getSession(terminalId);
        session.sendMsgToClient(data);
    }

    @Override
    public void sendCommand(CommandMsg commandMsg) throws InterruptedException, IOException {
        final String terminalId = commandMsg.getTerminalId();
        final Jt808Session session = getSession(terminalId);
        final byte[] bytes = this.encode(commandMsg, terminalId, session.getCurrentFlowId());
        session.sendMsgToClient(bytes);
    }

    private Jt808Session getSession(String terminalId) {
        return sessionManager.findByTerminalId(terminalId)
                .orElseThrow(() -> new JtSessionNotFoundException("Session Not Found with TerminalId : [" + terminalId + " ]", terminalId));
    }

    protected abstract byte[] encode(CommandMsg object, String terminalId, int flowId) throws IOException;
}
