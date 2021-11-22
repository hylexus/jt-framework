package io.github.hylexus.jt808.ext;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgV2011;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgV2019;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-26 8:19 下午
 */
public interface AuthCodeValidator {

    default boolean validateAuthCode(Jt808Session session, RequestMsgMetadata metadata, BuiltinAuthRequestMsgV2011 msgV2011) {
        final BuiltinAuthRequestMsgV2019 msgV2019 = new BuiltinAuthRequestMsgV2019();
        msgV2019.setAuthCode(msgV2011.getAuthCode());
        return this.validateAuthCode(session, metadata, msgV2019);
    }

    boolean validateAuthCode(Jt808Session session, RequestMsgMetadata metadata, BuiltinAuthRequestMsgV2019 authRequestMsgBody);

    @Slf4j
    @DebugOnly
    class BuiltinAuthCodeValidatorForDebugging implements AuthCodeValidator {

        @Override
        public boolean validateAuthCode(Jt808Session session, RequestMsgMetadata metadata, BuiltinAuthRequestMsgV2019 legacyMsgV2011) {
            log.debug("[AuthCodeValidator] Always return true, authCode : {}", legacyMsgV2011.getAuthCode());
            return true;
        }
    }

}
