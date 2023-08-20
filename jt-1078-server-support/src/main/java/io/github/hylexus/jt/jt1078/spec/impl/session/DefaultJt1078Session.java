package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.netty.channel.Channel;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Accessors(chain = true, fluent = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"channel"})
public class DefaultJt1078Session implements Jt1078Session {

    @Getter
    @Setter
    private String sim;

    @Getter
    @Setter
    private short channelNumber;

    @Getter
    @Setter
    private String sessionId;

    @Getter
    @Setter
    private Channel channel;

    @Getter
    @Setter
    @Builder.Default
    private long lastCommunicateTimestamp = 0L;

    @Getter
    @Builder.Default
    private long createdAt = System.currentTimeMillis();

}
