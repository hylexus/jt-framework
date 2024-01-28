package io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;

public @interface ValueDescriptor {

    MsgDataType source();

    Class<?> target();
}
