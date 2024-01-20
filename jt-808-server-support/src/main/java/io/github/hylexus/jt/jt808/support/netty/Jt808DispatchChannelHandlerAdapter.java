package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestProcessor;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * v2.14中把代码都搬到了父类
 *
 * @author hylexus
 * @author lirenhao
 **/
@Slf4j
@ChannelHandler.Sharable
public class Jt808DispatchChannelHandlerAdapter extends InternalJt808DispatchChannelHandlerAdapter {

    public Jt808DispatchChannelHandlerAdapter(Jt808RequestProcessor requestProcessor, Jt808SessionManager sessionManager) {
        super(requestProcessor, sessionManager);
    }

}
