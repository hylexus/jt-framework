package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 空的心跳信息消息体。
 *
 * @author hylexus
 * @see io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType#CLIENT_HEART_BEAT
 * @since 2.1.1
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0002 {
}
