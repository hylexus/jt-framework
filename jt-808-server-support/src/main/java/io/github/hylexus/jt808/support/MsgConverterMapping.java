package io.github.hylexus.jt808.support;

import io.github.hylexus.jt808.converter.MsgConverter;
import io.github.hylexus.jt808.msg.MsgType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-22 21:21
 */
@Slf4j
public class MsgConverterMapping {

    private Map<MsgType, MsgConverter> mapping;

    public MsgConverterMapping() {
        this.mapping = new HashMap<>();
    }

    public Optional<MsgConverter> getConverter(MsgType msgType) {
        return Optional.ofNullable(mapping.get(msgType));
    }

    public MsgConverterMapping registerConverter(MsgType msgType, MsgConverter converter) {

        if (containsConverter(msgType)) {
            log.warn("duplicate msgType : {}, the MsgConverter {} is replaced by {}", msgType, mapping.get(msgType).getClass(),
                    converter.getClass());
        }

        this.mapping.put(msgType, converter);
        return this;
    }

    public boolean containsConverter(MsgType msgType) {
        return this.mapping.containsKey(msgType);
    }

    public boolean isEmpty() {
        return this.mapping == null || this.mapping.isEmpty();
    }

}
