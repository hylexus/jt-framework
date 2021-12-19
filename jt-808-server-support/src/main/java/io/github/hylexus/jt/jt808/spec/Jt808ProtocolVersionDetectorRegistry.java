package io.github.hylexus.jt.jt808.spec;

/**
 * @author hylexus
 */
public interface Jt808ProtocolVersionDetectorRegistry {

    Jt808ProtocolVersionDetector getJt808ProtocolVersionDetector(int msgId);

    Jt808ProtocolVersionDetectorRegistry register(int msgId, Jt808ProtocolVersionDetector detector);

    default Jt808ProtocolVersionDetectorRegistry register(MsgType msgType, Jt808ProtocolVersionDetector detector) {
        return this.register(msgType.getMsgId(), detector);
    }

    Jt808ProtocolVersionDetectorRegistry clear();

    Jt808ProtocolVersionDetector defaultDetector();

    Jt808ProtocolVersionDetectorRegistry defaultDetector(Jt808ProtocolVersionDetector detector);

}
