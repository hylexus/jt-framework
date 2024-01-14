package io.github.hylexus.jt.jt808.support.extension.attachment.impl;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.session.DefaultJt808Session;
import io.github.hylexus.jt.jt808.spec.session.DefaultJt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGeneratorFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808SessionManager;
import io.netty.channel.Channel;

public class DefaultAttachmentJt808SessionManager extends DefaultJt808SessionManager implements AttachmentJt808SessionManager {
    public DefaultAttachmentJt808SessionManager(Jt808FlowIdGeneratorFactory flowIdGeneratorFactory) {
        super(flowIdGeneratorFactory);
    }

    @Override
    public Jt808Session generateSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        final DefaultJt808Session session = (DefaultJt808Session) super.generateSession(terminalId, version, channel);
        session.role(Jt808Session.Role.ATTACHMENT);
        return session;
    }
}
