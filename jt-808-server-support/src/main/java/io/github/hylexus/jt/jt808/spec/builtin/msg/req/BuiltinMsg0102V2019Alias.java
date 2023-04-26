package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;

/**
 * 内置的终端鉴权消息体(V2019)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0102V2019Alias {

    // byte[0,1)    BYTE    鉴权码长度
    @RequestFieldAlias.Byte(order = 1)
    private byte authCodeLength;

    // byte[1,n)    STRING  鉴权码内容
    @RequestFieldAlias.String(order = 2, lengthExpression = "authCodeLength")
    private String authCode;

    // byte[n+1,n+1+15)     BYTE[]  IMEI
    @RequestFieldAlias.String(order = 3, length = 15)
    private String imei;

    // byte[n+16,n+16+20)   BYTE[20]    软件版本号
    @RequestFieldAlias.String(order = 4, length = 20)
    private String softwareVersion;

}
