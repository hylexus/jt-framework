package io.github.hylexus.jt808.ext;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgBody;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-26 8:19 下午
 */
@FunctionalInterface
public interface AuthCodeValidator {

    boolean validateAuthCode(Jt808Session session, RequestMsgMetadata requestMsgMetadata, BuiltinAuthRequestMsgBody authRequestMsgBody);

    @Slf4j
    @DebugOnly
    class BuiltinAuthCodeValidatorForDebugging implements AuthCodeValidator {
        @Override
        public boolean validateAuthCode(Jt808Session session, RequestMsgMetadata metadata, BuiltinAuthRequestMsgBody body) {
            log.info("[AuthCodeValidator] Always return true, authCode : {}", body.getAuthCode());
            return true;
        }
    }

}
