package io.github.hylexus.jt808.converter;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.support.OrderedComponent;

import java.util.Optional;
import java.util.Set;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
public interface RequestMsgBodyConverter<E extends RequestMsgBody> extends OrderedComponent {

    Optional<E> convert2Entity(RequestMsgMetadata metadata);

    default Set<Jt808ProtocolVersion> getSupportedProtocolVersion() {
        return Jt808ProtocolVersion.unmodifiableSetVersionAutoDetection();
    }

}
