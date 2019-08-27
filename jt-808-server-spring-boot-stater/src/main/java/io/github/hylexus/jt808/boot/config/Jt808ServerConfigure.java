package io.github.hylexus.jt808.boot.config;

import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;

/**
 * @author hylexus
 * Created At 2019-08-27 16:53
 */
public interface Jt808ServerConfigure {

    default void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
    }

    default void configureMsgConverterMapping(MsgConverterMapping mapping) {
    }

    class BuiltinNoOpsConfigure implements Jt808ServerConfigure {
    }
}
