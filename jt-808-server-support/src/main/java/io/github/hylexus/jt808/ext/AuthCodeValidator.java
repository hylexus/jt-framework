package io.github.hylexus.jt808.ext;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt808.msg.RequestMsgCommonProps;
import io.github.hylexus.jt808.msg.req.AuthRequestMsgBody;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-26 8:19 下午
 */
@FunctionalInterface
public interface AuthCodeValidator {

    boolean validateAuthCode(Session session, RequestMsgCommonProps props, AuthRequestMsgBody body);

    @Slf4j
    @DebugOnly
    class BuiltinAuthCodeValidatorForDebugging implements AuthCodeValidator {
        @Override
        public boolean validateAuthCode(Session session, RequestMsgCommonProps props, AuthRequestMsgBody body) {
            log.info("[AuthCodeValidator] Always return true, authCode : {}", body.getAuthCode());
            return true;
        }
    }

}
