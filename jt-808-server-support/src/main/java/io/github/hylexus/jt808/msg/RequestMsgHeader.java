package io.github.hylexus.jt808.msg;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
@Getter
@Setter
@ToString
public class RequestMsgHeader {
    // byte[0-1] 消息ID
    private int msgId;

    // byte[2-3] 消息体属性
    private int msgBodyPropsField;

    // byte[2-3][0-9] 消息体长度
    private int msgBodyLength;

    // byte[2-3][10-12] 数据加密方式
    private int encryptionType;

    // byte[2-3][13] 是否分包,true==>有消息包封装项
    private boolean hasSubPackage;

    // byte[2-3][14-15]
    // 2011 --> bit[14,15]为保留位
    // 2019 --> bit[14]==1 表示版本标识, bit[15]为保留位
    private int reservedBit;

    // byte[4-9] 终端Id
    private String terminalId;

    // byte[10-11] 流水号
    private int flowId;

    // 消息包总数 byte[12-13]
    private long totalSubPackage;

    // 包序号 byte[14-15]
    // (word(16))这次发送的这个消息包是分包中的第几个消息包, 从 1 开始
    private long subPackageSeq;
    private Jt808ProtocolVersion version = Jt808ProtocolVersion.AUTO_DETECTION;

}
