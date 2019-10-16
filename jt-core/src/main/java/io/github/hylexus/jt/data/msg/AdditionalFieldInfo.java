package io.github.hylexus.jt.data.msg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.annotation.msg.req.AdditionalField.*;

/**
 * @author hylexus
 * Created At 2019-09-30 6:47 下午
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AdditionalFieldInfo {

    private int groupMsgId;

    private boolean isNestedAdditionalField = false;

    private int byteCountOfMsgId = DEFAULT_BYTE_COUNT_OF_MSG_ID;

    private int byteCountOfContentLength = DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH;

    public static final AdditionalFieldInfo DEFAULT_ROOT_GROUP;

    static {
        DEFAULT_ROOT_GROUP = new AdditionalFieldInfo()
                .setGroupMsgId(ROOT_GROUP_MSG_ID)
                .setNestedAdditionalField(false)
                .setByteCountOfMsgId(DEFAULT_BYTE_COUNT_OF_MSG_ID)
                .setByteCountOfContentLength(DEFAULT_BYTE_COUNT_OF_CONTENT_LENGTH);
    }
}
