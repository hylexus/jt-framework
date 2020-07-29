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

    // 消息体长度
    private int msgBodyLength;

    // 数据加密方式
    private int encryptionType;

    // 是否分包,true==>有消息包封装项
    private boolean hasSubPackage;

    // 终端Id byte[4-9]
    private String terminalId;

    // 流水号 byte[10-12]
    private int flowId;

    // 保留位 byte[14-15]
    private int reservedBit;

    // 消息包总数 byte[12-13]
    private long totalSubPackage;

    // 包序号 byte[12-13]
    // (word(16))这次发送的这个消息包是分包中的第几个消息包, 从 1 开始
    private long subPackageSeq;
    private Jt808ProtocolVersion version;

}
