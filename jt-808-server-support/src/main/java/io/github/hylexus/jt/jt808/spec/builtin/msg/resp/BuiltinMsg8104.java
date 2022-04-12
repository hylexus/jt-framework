package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 终端参数查询
 *
 *  使用参考: https://hylexus.github.io/jt-framework/v2/jt-808/guide/basic/command-sender.html#%E6%89%8B%E5%8A%A8%E4%B8%8B%E5%8F%91
 *
 * @author puruidong
 * @version 2022/4/12
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8104)
public class BuiltinMsg8104 {
}
