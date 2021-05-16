package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;

/**
 * Created At 2020-12-05 13:46
 *
 * @author hylexus
 */
@Data
public class BuiltinRawBytesRequestMsgBody implements RequestMsgBody {
    private byte[] rwaBytes;

    public BuiltinRawBytesRequestMsgBody(byte[] rwaBytes) {
        this.rwaBytes = rwaBytes;
    }
}
