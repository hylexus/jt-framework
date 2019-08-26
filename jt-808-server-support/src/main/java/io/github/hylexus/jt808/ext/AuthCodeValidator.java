package io.github.hylexus.jt808.ext;

import io.github.hylexus.jt808.msg.req.AuthRequestMsg;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-26 8:19 下午
 */
@FunctionalInterface
public interface AuthCodeValidator {

    boolean validateAuthCode(AuthRequestMsg msg);

    @Slf4j
    class BuiltinAuthCodeValidatorForDebugging implements AuthCodeValidator {
        @Override
        public boolean validateAuthCode(AuthRequestMsg msg) {
            log.debug(">>|<< Always return true, authCode : {}", msg.getAuthCode());
            return true;
        }
    }

}
