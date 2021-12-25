package io.github.hylexus.jt808.samples.customized.session;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.session.*;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public class MySessionManager implements Jt808SessionManager {

    private final Jt808FlowIdGeneratorFactory flowIdGeneratorFactory;
    private static volatile MySessionManager instance;
    private final Jt808SessionManager delegate;

    public static Jt808SessionManager getInstance(Jt808FlowIdGeneratorFactory flowIdGeneratorFactory) {
        if (instance == null) {
            synchronized (MySessionManager.class) {
                if (instance == null) {
                    instance = new MySessionManager(flowIdGeneratorFactory);
                }
            }
        }
        return instance;
    }

    private MySessionManager(Jt808FlowIdGeneratorFactory flowIdGeneratorFactory) {
        this.flowIdGeneratorFactory = flowIdGeneratorFactory;
        this.delegate = DefaultJt808SessionManager.getInstance(flowIdGeneratorFactory);
    }

    protected MySession buildSession(String terminalId, Jt808ProtocolVersion version, Channel channel, Jt808FlowIdGenerator flowIdGenerator) {
        MySession session = new MySession(flowIdGenerator);
        session.channel(channel);
        session.id(generateSessionId(channel));
        session.terminalId(terminalId);
        session.lastCommunicateTimestamp(System.currentTimeMillis());
        session.protocolVersion(version);
        Object key = channel.attr(AttributeKey.valueOf("key")).get();
        session.setSomeField(key == null ? "" : key.toString());
        return session;
    }

    // 这里替换为自定义的Session
    @Override
    public Jt808Session generateSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        final Jt808FlowIdGenerator flowIdGenerator = this.flowIdGeneratorFactory.create();
        return buildSession(terminalId, version, channel, flowIdGenerator);
    }

    @Override
    public Stream<Jt808Session> list() {
        return delegate.list();
    }

    @Override
    public List<Jt808Session> list(int page, int pageSize) {
        return delegate.list(page, pageSize);
    }

    @Override
    public List<Jt808Session> list(int page, int pageSize, Predicate<Jt808Session> filter) {
        return delegate.list(page, pageSize, filter);
    }

    @Override
    public <T> List<T> list(int page, int pageSize, Predicate<Jt808Session> filter, Comparator<Jt808Session> sorter, Function<Jt808Session, T> converter) {
        return delegate.list(page, pageSize, filter, sorter, converter);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public String generateSessionId(Channel channel) {
        return delegate.generateSessionId(channel);
    }

    @Override
    public Jt808Session persistenceIfNecessary(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        return delegate.persistenceIfNecessary(terminalId, version, channel);
    }

    @Override
    public Jt808Session persistenceIfNecessary(String terminalId, Jt808ProtocolVersion version, Channel channel, boolean updateLastCommunicateTime) {
        return delegate.persistenceIfNecessary(terminalId, version, channel, updateLastCommunicateTime);
    }

    @Override
    public void persistence(Jt808Session session) {
        delegate.persistence(session);
    }

    @Override
    @Nullable
    public Jt808Session removeBySessionId(String sessionId) {
        return delegate.removeBySessionId(sessionId);
    }

    @Override
    public void removeBySessionIdAndClose(String sessionId, SessionCloseReason reason) {
        delegate.removeBySessionIdAndClose(sessionId, reason);
    }

    @Override
    public Optional<Jt808Session> findBySessionId(String sessionId) {
        return delegate.findBySessionId(sessionId);
    }

    @Override
    public Optional<Jt808Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime) {
        return delegate.findByTerminalId(terminalId, updateLastCommunicateTime);
    }

    @Override
    public Optional<Jt808Session> findByTerminalId(String terminalId) {
        return delegate.findByTerminalId(terminalId);
    }

    @Override
    public void sendBytesToClient(Jt808Session session, byte[] bytes) throws InterruptedException {
        delegate.sendBytesToClient(session, bytes);
    }

    @Override
    public Jt808SessionManager addListener(Jt808SessionEventListener listener) {
        return this.delegate.addListener(listener);
    }

    @Override
    public List<Jt808SessionEventListener> getListeners() {
        return this.delegate.getListeners();
    }
}
