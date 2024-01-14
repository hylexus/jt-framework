package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TJSATL12—2017道路运输车辆主动安全智能防控系统 $4.6.4--文件数据上传
 * <p>
 * 该消息不需要回复客户端
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg30316364Alias {

    // BYTE[50]  文件名称
    @RequestFieldAlias.Bytes(order = 10, lengthExpression = "50")
    private String fileName;

    // DWORD 数据偏移量
    @RequestFieldAlias.Dword(order = 20)
    private long dataOffset;

    // DWORD 数据长度
    @RequestFieldAlias.Dword(order = 30)
    private long dataLength;

    // BYTE[n] 数据体 默认长度64K，文件小于64K则为实际长度
    @RequestFieldAlias.Bytes(order = 40, lengthExpression = "#this.dataLength")
    private byte[] data;

    @Override
    public String toString() {
        return "BuiltinMsg30316364Alias{"
                + "fileName='" + fileName + '\''
                + ", dataOffset=" + dataOffset
                + ", dataLength=" + dataLength
                + '}';
    }
}
