package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.samples.customized.msg.req.LocationUploadRequestMsgBody;
import io.github.hylexus.jt808.samples.customized.service.TestService;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-19 11:31 下午
 */
@Slf4j
@Component
@Jt808RequestMsgHandler(msgType = 0x0200)
public class LocationInfoUploadMsgHandler extends AbstractMsgHandler<LocationUploadRequestMsgBody> implements ApplicationContextAware, InitializingBean {

    @Autowired
    private TestService testService;

    private ApplicationContext context;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("{}", context);
        log.info("{}", testService);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, LocationUploadRequestMsgBody body, Jt808Session session) {

        log.info("{}", body);
        return Optional.of(commonSuccessReply(metadata, BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD));
    }
}
