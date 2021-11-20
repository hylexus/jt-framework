# 协议扫盲
## 数据类型
### 808协议数据类型

数据类型 	|描述及要求
------------|--------------
BYTE		|无符号单字节整形（字节， 8 位）
WORD		|无符号双字节整形（字， 16 位）
DWORD		|无符号四字节整形（双字， 32 位）
BYTE[n] 	| n 字节
BCD[n] 		|8421 码， n 字节
STRING 		|GBK 编码，若无数据，置空

### 对应Java数据类型

和文档中定义的数据类型都在枚举类 `io.github.hylexus.jt.data.MsgDataType` 中。

```java
@Getter
public enum MsgDataType {
    BYTE(1, "无符号单字节整型(字节，8 位)", newHashSet(byte.class, Byte.class, int.class, Integer.class, Short.class, short.class)),
    BYTES(0, "", newHashSet(byte[].class)),
    WORD(2, "无符号双字节整型(字，16 位)", newHashSet(short.class, Short.class, int.class, Integer.class)),
    // https://github.com/hylexus/jt-framework/issues/34
    DWORD(4, "无符号四字节整型(双字，32 位)", newHashSet(long.class, Long.class, int.class, Integer.class)),
    BCD(0, "8421 码，n 字节", newHashSet(String.class)),
    STRING(0, "GBK 编码，若无数据，置空", newHashSet(String.class)),
    UNKNOWN(0, "未知类型，用于占位符或默认值", newHashSet(String.class)),
    ;

    /**
     * 字节数
     * 为零表示使用外部指定的长度
     *
     * @see BasicField#length()
     */
    private final int byteCount;

    private final String desc;

    private final Set<Class<?>> expectedTargetClassType;

    MsgDataType(int byteCount, String desc, Set<Class<?>> expectedTargetClassType) {
        this.byteCount = byteCount;
        this.desc = desc;
        this.expectedTargetClassType = expectedTargetClassType;
    }

}
```

## 消息结构


标识位		|消息头		|消息体		|校验码	 	|标识位
------------|-----------|-----------|-----------|------
1byte(0x7e)	|16byte		|			|1byte		|1byte(0x7e)


## 消息头

整个报文中最复杂也就是消息头的处理了。

- `分包消息` 的消息头长度为 `16字节`
- `非分包消息` 的消息头长度为 `12字节`

消息头的结构如下所示：

```
消息ID(0-1)	消息体属性(2-3)	终端手机号(4-9)	消息流水号(10-11)	消息包封装项(12-15)

byte[0-1] 	消息ID word(16)
byte[2-3] 	消息体属性 word(16)
		bit[0-9]	消息体长度
		bit[10-12]	数据加密方式
						此三位都为 0，表示消息体不加密
						第 10 位为 1，表示消息体经过 RSA 算法加密
						其它保留
		bit[13]		分包
						1：消息体卫长消息，进行分包发送处理，具体分包信息由消息包封装项决定
						0：则消息头中无消息包封装项字段
		bit[14-15]	保留
byte[4-9] 	终端手机号或设备ID bcd[6]
		根据安装后终端自身的手机号转换
		手机号不足12 位，则在前面补 0
byte[10-11] 	消息流水号 word(16)
		按发送顺序从 0 开始循环累加
byte[12-15] 	消息包封装项
		byte[0-1]	消息包总数(word(16))
						该消息分包后得总包数
		byte[2-3]	包序号(word(16))
						从 1 开始
		如果消息体属性中相关标识位确定消息分包处理,则该项有内容
		否则无该项
```

