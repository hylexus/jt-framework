package io.github.hylexus.jt808.converter;

import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.support.OrderedComponent;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-02-02 3:19 下午
 */
public interface ResponseMsgBodyConverter extends OrderedComponent {

    boolean supportsMsgBody(Object msgBody);

    Optional<RespMsgBody> convert(Object msgBody, @Nullable Jt808Session session, @Nullable RequestMsgMetadata metadata);

    default Optional<RespMsgBody> convert(Object msgBody) {
        return this.convert(msgBody, null, null);
    }

}
