package io.github.hylexus.jt.jt1078.support.dispatcher;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListener;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Jt1078RequestPreprocessor implements Jt1078RequestLifecycleListenerAware {

    private final Jt1078SessionManager sessionManager;
    private final Jt1078MsgDecoder msgDecoder;
    private final Jt1078RequestMsgDispatcher requestMsgDispatcher;
    private Jt1078RequestLifecycleListener lifecycleListener;

    public Jt1078RequestPreprocessor(Jt1078SessionManager sessionManager, Jt1078MsgDecoder msgDecoder, Jt1078RequestMsgDispatcher requestMsgDispatcher) {
        this.sessionManager = sessionManager;
        this.msgDecoder = msgDecoder;
        this.requestMsgDispatcher = requestMsgDispatcher;
    }

    public void preprocess(Channel channel, ByteBuf msg) {

        if (msg.readableBytes() <= 0) {
            return;
        }

        Jt1078Request request = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("---> 30316364{}", HexStringUtils.byteBufToHexString(msg));
            }
            final boolean continueProcess = this.lifecycleListener.beforeDecode(msg, channel);
            if (!continueProcess) {
                return;
            }

            // retained in decoder
            request = this.msgDecoder.decode(msg);

            // update session
            this.sessionManager.persistenceIfNecessary(request, channel);

            final boolean continueProcessing = this.lifecycleListener.beforeDispatch(request, channel);
            if (!continueProcessing) {
                return;
            }
            // dispatch
            this.requestMsgDispatcher.doDispatch(request);
            if (log.isDebugEnabled()) {
                log.debug("{}\n", request);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (request != null) {
                request.release();
            }
        }

    }

    @Override
    public void setRequestLifecycleListener(Jt1078RequestLifecycleListener listener) {
        this.lifecycleListener = listener;
    }
}
