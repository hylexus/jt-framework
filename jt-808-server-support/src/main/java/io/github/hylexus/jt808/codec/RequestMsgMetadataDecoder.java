package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;

public interface RequestMsgMetadataDecoder {

    RequestMsgMetadata parseMsgMetadata(Jt808ProtocolVersion version, byte[] bytes);

}
