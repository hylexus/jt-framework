package io.github.hylexus.jt808.exception;

import io.github.hylexus.jt.exception.AbstractJtException;
import io.github.hylexus.jt808.handler.impl.reflection.MethodParameter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Session;
import lombok.Getter;

/**
 * @author hylexus
 * Created At 2020-02-02 1:35 下午
 */
@Getter
public class ArgumentResolveException extends AbstractJtException {
    private final MethodParameter parameter;
    private final RequestMsgMetadata metadata;
    private final Session session;
    private final RequestMsgBody msg;

    public ArgumentResolveException(MethodParameter parameter, RequestMsgMetadata metadata, Session session, RequestMsgBody msg) {
        this.parameter = parameter;
        this.metadata = metadata;
        this.session = session;
        this.msg = msg;
    }

    public ArgumentResolveException(String message, MethodParameter parameter, RequestMsgMetadata metadata, Session session, RequestMsgBody msg) {
        super(message);
        this.parameter = parameter;
        this.metadata = metadata;
        this.session = session;
        this.msg = msg;
    }

    public ArgumentResolveException(String message, Throwable cause, MethodParameter parameter, RequestMsgMetadata metadata, Session session, RequestMsgBody msg) {
        super(message, cause);
        this.parameter = parameter;
        this.metadata = metadata;
        this.session = session;
        this.msg = msg;
    }

    public ArgumentResolveException(Throwable cause, MethodParameter parameter, RequestMsgMetadata metadata, Session session, RequestMsgBody msg) {
        super(cause);
        this.parameter = parameter;
        this.metadata = metadata;
        this.session = session;
        this.msg = msg;
    }

    public ArgumentResolveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, MethodParameter parameter, RequestMsgMetadata metadata, Session session, RequestMsgBody msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.parameter = parameter;
        this.metadata = metadata;
        this.session = session;
        this.msg = msg;
    }
}
