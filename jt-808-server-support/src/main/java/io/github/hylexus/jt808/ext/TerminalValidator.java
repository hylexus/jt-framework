package io.github.hylexus.jt808.ext;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lirenhao
 * date: 2020/7/12 5:15 下午
 */
public interface TerminalValidator {

    /**
     * 校验终端请求参数
     *
     * @param metadata 终端请求参数
     * @return 是否校验通过
     */
    boolean validateTerminal(RequestMsgMetadata metadata);


    /**
     * 是否需要校验
     *
     * @param metadata 终端请求参数
     * @param msgId    msgId
     * @return 是否需要校验
     */
    boolean needValidate(RequestMsgMetadata metadata, Integer msgId);

    @Slf4j
    @DebugOnly
    class BuiltinTerminalValidatorForDebugging implements TerminalValidator {

        @Override
        public boolean validateTerminal(RequestMsgMetadata metadata) {
            log.debug("[TerminalValidator] Always return true");
            return true;
        }

        @Override
        public boolean needValidate(RequestMsgMetadata metadata, Integer msgId) {
            log.debug("[TerminalValidator] No verification required");
            return false;
        }
    }
}

