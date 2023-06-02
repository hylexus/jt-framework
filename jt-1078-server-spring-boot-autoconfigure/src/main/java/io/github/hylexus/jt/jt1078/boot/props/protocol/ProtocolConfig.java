package io.github.hylexus.jt.jt1078.boot.props.protocol;

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
    private int maxFrameLength = 4096;
}
