package io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 苏标-表-4-16 报警标识号格式
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
public class AlarmIdentifierAlias {
    // 终端ID BYTE[7] 7个字节，由大写字母和数字组成
    @RequestFieldAlias.String(order = 10, length = 7)
    @ResponseFieldAlias.String(order = 10)
    private String terminalId;

    // 时间   BCD[6]  YY-MM-DD-hh-mm-ss （GMT+8时间）
    @RequestFieldAlias.BcdDateTime(order = 20)
    @ResponseFieldAlias.BcdDateTime(order = 20)
    private LocalDateTime time;

    // 序号   BYTE    同一时间点报警的序号，从0循环累加
    @RequestFieldAlias.Byte(order = 30)
    @ResponseFieldAlias.Byte(order = 30)
    private short sequence;

    // 附件数量 BYTE    表示该报警对应的附件数量
    @RequestFieldAlias.Byte(order = 40)
    @ResponseFieldAlias.Byte(order = 40)
    private short attachmentCount;

    // 预留 BYTE
    @RequestFieldAlias.Byte(order = 50)
    @ResponseFieldAlias.Byte(order = 50)
    private short reserved = 0;
}