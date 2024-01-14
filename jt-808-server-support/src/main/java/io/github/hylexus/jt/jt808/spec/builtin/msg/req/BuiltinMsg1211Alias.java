package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg1211Alias {

    // byte[0]
    @RequestFieldAlias.Byte(order = 10)
    private short fileNameLength;

    @RequestFieldAlias.String(order = 20, lengthExpression = "#this.fileNameLength")
    private String fileName;

    @RequestFieldAlias.Byte(order = 30)
    private short fileType;

    @RequestFieldAlias.Dword(order = 40)
    private long fileSize;
}
