package io.github.hylexus.jt808.samples.mixedversion.entity.req;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created At 2020-06-12 17:24
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
public class RegisterRequestMsgV2019 implements RequestMsgBody {
    private int provinceId;

    private int cityId;

    private byte[] manufacturerId;

    private byte[] terminalType;

    private byte[] terminalId;

    private byte color;

    private String carIdentifier;
}
