package io.github.hylexus.jt.jt808.boot.props.protocol;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
public class ProtocolConfig {
    private Jt808ProtocolVersion version = Jt808ProtocolVersion.AUTO_DETECTION;
    private int maxFrameLength = JtProtocolConstant.MAX_PACKAGE_LENGTH;
}
