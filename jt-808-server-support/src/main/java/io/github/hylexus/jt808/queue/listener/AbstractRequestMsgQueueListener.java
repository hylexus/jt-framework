package io.github.hylexus.jt808.queue.listener;

import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt808.codec.Encoder;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.handler.ExceptionHandler;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl.ArgumentContext;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.session.SessionManager;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.utils.ArgumentUtils;
import io.github.hylexus.jt808.utils.ClientUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-25 6:43 下午
 */
@Slf4j
public abstract class AbstractRequestMsgQueueListener<T extends RequestMsgQueue> implements RequestMsgQueueListener {
    protected MsgHandlerMapping msgHandlerMapping;
    protected T queue;
    private final ExceptionHandler exceptionHandler;
    private final ResponseMsgBodyConverter responseMsgBodyConverter;
    private final Encoder encoder;

    public AbstractRequestMsgQueueListener(
            MsgHandlerMapping msgHandlerMapping, T queue, ExceptionHandler exceptionHandler,
            ResponseMsgBodyConverter responseMsgBodyConverter, Encoder encoder) {

        this.msgHandlerMapping = msgHandlerMapping;
        this.queue = queue;
        this.exceptionHandler = exceptionHandler;
        this.responseMsgBodyConverter = responseMsgBodyConverter;
        this.encoder = encoder;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void consumeMsg(RequestMsgMetadata metadata, RequestMsgBody body) {
        val handlerInfo = msgHandlerMapping.getHandler(metadata.getMsgType());
        if (!handlerInfo.isPresent()) {
            log.error("No handler found for MsgType : {}", metadata.getMsgType());
            return;
        }

        final MsgHandler<? extends RequestMsgBody> msgHandler = handlerInfo.get();
        Session session = null;

        try {

            session = getSession(metadata);

            ((MsgHandler) msgHandler).handleMsg(metadata, body, session);

        } catch (InvocationTargetException e) {
            val context = new ArgumentContext(metadata, session, body, e.getTargetException());
            invokeExceptionHandler(msgHandler, context);
        } catch (Throwable e) {
            val context = new ArgumentContext(metadata, session, body, e);
            invokeExceptionHandler(msgHandler, context);
        }
    }

    private void invokeExceptionHandler(MsgHandler<? extends RequestMsgBody> msgHandler, ArgumentContext argumentContext) {

        try {
            final Object result = exceptionHandler.handleException(msgHandler, argumentContext);
            if (ArgumentUtils.isNullReturnValue(result)) {
                return;
            }

            // 返回值的处理暂时只考虑RespMsgBody
            // 如有必要的话，后期重构增加其他类型返回值的处理
            final Session session = argumentContext.getSession();
            final RequestMsgMetadata metadata = argumentContext.getMetadata();
            final Optional<RespMsgBody> respMsgBodyInfo = responseMsgBodyConverter.convert(result, session, metadata);

            if (respMsgBodyInfo.isPresent() && session != null && metadata != null) {
                byte[] respBytes = this.encoder.encodeRespMsg(respMsgBodyInfo.get(), session.getCurrentFlowId(), metadata.getHeader().getTerminalId());
                ClientUtils.sendBytesToClient(session, respBytes);
            }
        } catch (Throwable throwable) {
            log.error("An unexpected exception occurred while invoke ExceptionHandler", throwable);
        }
    }

    protected Session getSession(RequestMsgMetadata metadata) {
        final String terminalId = metadata.getHeader().getTerminalId();
        Optional<Session> session = SessionManager.getInstance().findByTerminalId(terminalId);
        if (session.isPresent()) {
            return session.get();
        }

        throw new JtSessionNotFoundException(terminalId);
    }
}
