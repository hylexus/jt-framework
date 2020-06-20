package io.github.hylexus.jt808.converter.impl.resp;

import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.resp.CommandMsg;
import io.github.hylexus.jt808.session.Jt808Session;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-03-28 6:57 下午
 */
public class CommandMsgBodyConverter extends AbstractBuiltinRespBodyConverter {

    private final ReflectionBasedRespMsgBodyConverter reflectionBasedRespMsgBodyConverter;

    public CommandMsgBodyConverter(ReflectionBasedRespMsgBodyConverter reflectionBasedRespMsgBodyConverter) {
        this.reflectionBasedRespMsgBodyConverter = reflectionBasedRespMsgBodyConverter;
    }

    @Override
    public boolean supportsMsgBody(Object msg) {
        return msg instanceof CommandMsg;
    }

    @Override
    public Optional<RespMsgBody> convert(Object msg, @Nullable Jt808Session session, @Nullable RequestMsgMetadata metadata) {
        final CommandMsg commandMsg = (CommandMsg) msg;
        final Class<?> bodyClass = commandMsg.getBody().getClass();
        if (RespMsgBody.class.isAssignableFrom(bodyClass)) {
            return Optional.ofNullable((RespMsgBody) (commandMsg.getBody()));
        }

        if (isAnnotatedByRespMsgBody(bodyClass)) {
            return this.reflectionBasedRespMsgBodyConverter.convert(commandMsg.getBody(), session, metadata);
        }
        return Optional.empty();
    }

    private boolean isAnnotatedByRespMsgBody(Class<?> bodyClass) {
        // TODO cache
        return AnnotationUtils.findAnnotation(bodyClass, Jt808RespMsgBody.class) != null;
    }
}
