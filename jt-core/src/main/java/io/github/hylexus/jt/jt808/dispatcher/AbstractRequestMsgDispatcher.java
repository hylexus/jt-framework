package io.github.hylexus.jt.jt808.dispatcher;

import io.github.hylexus.jt.jt808.converter.MsgConverter;
import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt.jt808.msg.MsgType;
import io.github.hylexus.jt.jt808.support.MsgConverterMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
@Slf4j
public abstract class AbstractRequestMsgDispatcher implements RequestMsgDispatcher {

    private MsgConverterMapping msgConverterMapping;

    public AbstractRequestMsgDispatcher(MsgConverterMapping msgConverterMapping) {
        this.msgConverterMapping = msgConverterMapping;
    }

    public void doDispatch(AbstractRequestMsg abstractMsg) throws Exception {

        final MsgType msgType = abstractMsg.getMsgType();
        final Optional<MsgConverter> converter = this.msgConverterMapping.getConverter(msgType);
        if (!converter.isPresent()) {
            log.error("No [MsgConverter] found for msgType {}", msgType);
            return;
        }

        @SuppressWarnings("unchecked") final MsgConverter<AbstractRequestMsg> msgConverter = converter.get();
        final Optional<AbstractRequestMsg> subMsg = msgConverter.convert2SubMsg(abstractMsg);
        if (!subMsg.isPresent()) {
            log.debug("[MsgConverter] return empty value. converter:{}", msgConverter.getClass());
            return;
        }
        final AbstractRequestMsg msg = subMsg.get();
        msg.setMsgType(msgType);
        this.doBroadcast(msg);
    }

    public abstract void doBroadcast(AbstractRequestMsg msg) throws Exception;
}
