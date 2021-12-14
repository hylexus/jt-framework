package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.MsgType;

import java.util.Set;

/**
 * @author hylexus
 */
public interface MultipleVersionSupport extends ReplaceableComponent {

    default Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersionAutoDetection();
    }

    Set<MsgType> getSupportedMsgTypes();
}
