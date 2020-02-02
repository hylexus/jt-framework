package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.samples.customized.msg.req.LocationUploadMsgBody;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-19 11:31 下午
 */
@Slf4j
public class LocationInfoUploadMsgHandler extends AbstractMsgHandler<LocationUploadMsgBody> {

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, LocationUploadMsgBody body, Session session) {

        log.info("{}", body);
        return Optional.of(commonSuccessReply(metadata, BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD));
    }
}
