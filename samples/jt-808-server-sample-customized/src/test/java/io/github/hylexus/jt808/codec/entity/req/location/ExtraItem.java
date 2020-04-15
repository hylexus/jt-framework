package io.github.hylexus.jt808.codec.entity.req.location;

import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import lombok.Data;

import static io.github.hylexus.jt.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.data.MsgDataType.BYTES;

/**
 * @author hylexus
 * Created At 2020-04-15 5:07 下午
 */
@Data
public class ExtraItem {
    @BasicField(startIndex = 0, dataType = BYTE)
    private Integer id;

    @BasicField(order = 1, startIndex = 1, dataType = BYTE)
    private Integer length;

    @BasicField(order = 2, startIndex = 2, dataType = BYTES, byteCountMethod = "getLength")
    private byte[] rawBytes;

}
