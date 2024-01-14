package io.github.hylexus.jt.jt808.support.extension.attachment.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgBodyProps;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.exception.Jt808UnknownMsgException;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleAttachmentJt808RequestProcessor implements AttachmentJt808RequestProcessor {
    public static final AttributeKey<Jt808Session> SESSION_ATTR_KEY = AttributeKey.newInstance("attachment-" + Jt808Session.class.getName());
    public static final byte[] ATTACHMENT_REQUEST_PREFIX = {0x30, 0x31, 0x63, 0x64};
    private final Jt808RequestRouteExceptionHandler routeExceptionHandler;
    private final Jt808MsgDecoder decoder;
    private final Jt808DispatcherHandler dispatcherHandler;
    private final AttachmentJt808SessionManager sessionManager;

    public SimpleAttachmentJt808RequestProcessor(
            Jt808MsgDecoder decoder,
            AttachmentJt808SessionManager sessionManager,
            Jt808RequestRouteExceptionHandler routeExceptionHandler,
            Jt808DispatcherHandler dispatcherHandler) {
        this.decoder = decoder;
        this.routeExceptionHandler = routeExceptionHandler;
        this.dispatcherHandler = dispatcherHandler;
        this.sessionManager = sessionManager;
    }

    @Override
    public void processJt808Request(ByteBuf buf, Channel channel) {
        if (startWith(buf, ATTACHMENT_REQUEST_PREFIX)) {
            final Jt808Session session = this.getSession(channel);
            final Jt808RequestHeader requestHeader;
            if (session == null) {
                requestHeader = Jt808RequestHeader.newBuilder()
                        .version(null)
                        .msgType(BuiltinJt808MsgType.CLIENT_MSG_30316364.getMsgId())
                        .msgBodyProps(new DefaultJt808MsgBodyProps(0))
                        .terminalId(null)
                        .flowId(0)
                        .subPackageProps(null)
                        .build();
            } else {
                requestHeader = Jt808RequestHeader.newBuilder()
                        .version(session.protocolVersion())
                        .msgType(BuiltinJt808MsgType.CLIENT_MSG_30316364.getMsgId())
                        .msgBodyProps(new DefaultJt808MsgBodyProps(0))
                        .terminalId(session.terminalId())
                        .flowId(0)
                        .subPackageProps(null)
                        .build();
            }
            final Jt808Request request = Jt808Request.newBuilder(BuiltinJt808MsgType.CLIENT_MSG_30316364)
                    .header(requestHeader)
                    .rawByteBuf(null)
                    // 跳过 0x30316364 4字节
                    .body(buf.readerIndex(buf.readerIndex() + 4))
                    .originalCheckSum((byte) 0)
                    .calculatedCheckSum((byte) 0)
                    .build();
            this.doDispatch(request, session);
        } else {
            this.doProcessAttachmentJt808Request(buf, channel);
        }
    }

    private void doProcessAttachmentJt808Request(ByteBuf buf, Channel channel) {
        Jt808Session jt808Session = null;
        Jt808Request request = null;
        try {
            try {
                request = decoder.decode(buf);
                final Jt808RequestHeader header = request.header();
                final String terminalId = header.terminalId();
                jt808Session = this.persistenceSessionIfNecessary(channel, request);
                // jt808Session = sessionManager.persistenceIfNecessary(terminalId, header.version(), channel);

                if (log.isDebugEnabled()) {
                    log.debug("[decode] : {}, terminalId={}, msg = {}", request.msgType(), terminalId, request);
                }
            } catch (Jt808UnknownMsgException unknownMsgException) {
                this.routeExceptionHandler.onReceiveUnknownMsg(unknownMsgException.getMsgId(), unknownMsgException.getPayload());
                return;
            } catch (Exception e) {
                if (request != null) {
                    request.release();
                }
                throw e;
            }
            doDispatch(request, jt808Session);
            // this.msgDispatcher.doDispatch(request);
        } catch (Throwable throwable) {
            try {
                log.error("", throwable);
                // commonExceptionHandler.handleException(null, ArgumentContext.of(request, jt808Session, new Jt808NettyException(throwable)));
                throw throwable;
            } catch (Throwable e) {
                log.error("An error occurred while invoke ExceptionHandler", e);
            }
        }
    }

    // 这个 session 对应的是上传文件的连接，而不是指令服务器对应的普通 808 连接
    private Jt808Session persistenceSessionIfNecessary(Channel channel, Jt808Request request) {
        final Jt808Session session = this.getSession(channel);
        if (session != null) {
            return session;
        }
        final Jt808Session attachmentSession = sessionManager.generateSession(request.terminalId(), request.version(), channel);
        channel.attr(SESSION_ATTR_KEY).set(attachmentSession);
        return attachmentSession;
    }

    private Jt808Session getSession(Channel channel) {
        return channel.attr(SESSION_ATTR_KEY).get();
    }

    private void doDispatch(Jt808Request originalRequest, Jt808Session jt808Session) {
        Jt808ServerExchange exchange = null;
        Jt808Response originalResponse = null;
        try {
            originalResponse = DefaultJt808Response.init(originalRequest.version(), originalRequest.terminalId());
            exchange = new DefaultJt808ServerExchange(originalRequest, originalResponse, jt808Session);
            dispatcherHandler.handleRequest(exchange);
        } finally {
            if (exchange != null && exchange.response() != originalResponse) {
                originalResponse.release();
            }
        }
    }


    private boolean startWith(ByteBuf buf, byte[] bytes) {
        for (int i = 0, j = buf.readerIndex(); i < bytes.length; i++, j++) {
            if (buf.getByte(j) != bytes[i]) {
                return false;
            }
        }
        return true;
    }

}
