package io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * @author hylexus
 * Created At 2020-02-09 12:14 下午
 */
@Getter
@Setter
@ToString
public class ArgumentContext {
    @Nullable
    private final RequestMsgMetadata metadata;
    @Nullable
    private final Jt808Session session;
    @Nullable
    private final RequestMsgBody msg;
    @Nullable
    private final Throwable throwable;

    public ArgumentContext(@Nullable RequestMsgMetadata metadata, @Nullable Jt808Session session, @Nullable RequestMsgBody msg, @Nullable Throwable throwable) {
        this.metadata = metadata;
        this.session = session;
        this.msg = msg;
        this.throwable = throwable;
    }
}
