package io.github.hylexus.jt.data.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hylexus
 * Created At 2019-09-29 12:03 上午
 */
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class BuiltinAdditionalInfo implements AdditionalInfo {

    public BuiltinAdditionalInfo(int id, int contentLength, byte[] content) {
        this.id = id;
        this.contentLength = contentLength;
        this.content = content;
    }

    private int id;
    private int contentLength;
    private byte[] content;

}
