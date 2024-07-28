package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 内置提供的心跳信息的空消息体。
 * <p>
 * 但是命名中的 `0200` 应该应该改为 `0002`，命名有误，被废弃了。
 * <p>
 * 如果有用到，应该使用 {@link BuiltinMsg0002} 代替。
 *
 * @author hylexus
 * @see BuiltinMsg0002
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
//@Deprecated(forRemoval = true, since = "2.1.1")
@Deprecated
public class BuiltinMsg0200 {
}
