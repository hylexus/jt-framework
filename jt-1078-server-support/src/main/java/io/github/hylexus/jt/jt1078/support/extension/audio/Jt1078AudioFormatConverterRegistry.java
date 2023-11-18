package io.github.hylexus.jt.jt1078.support.extension.audio;

import java.util.Optional;

/**
 * @author hylexus
 */
// todo ...
public interface Jt1078AudioFormatConverterRegistry {

    Jt1078AudioFormatConverterRegistry register(Jt1078AudioFormat source, Jt1078AudioFormat target, Jt1078AudioFormatConverter converter);

    Optional<Jt1078AudioFormatConverter> getConverter(Jt1078AudioFormat source, Jt1078AudioFormat target);

}
