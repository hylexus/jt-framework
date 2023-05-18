---
icon: bit
---

# BitOperator(v2.1.1)

::: info 提示

`BitOperator` 是 **2.1.1** 中引入的辅助类。

:::

## 作用

`BitOperator` 是位操作的工具类，目前只支持 **64** 个 `Bit`(`java.lang.Long`) 的操作。

当然 `BitOperator` 也可以用在被 `@RequestField`、`@ResponseField`、`@RequestFieldAlias`、`@ResponseFieldAlias` 修饰的实体类的成员变量上。

## 场景示例

位操作的场景都可以用到，比如下面这个场景(需要对某个 `bit` 操作)：

<p class="demo">
    <img :src="$withBase('/img/v2/utilities/bit-operator-case-01.png')">
</p>

以下面这条位置上报报文(**2019**)为例，其中的**报警标志**字段中 第 **21**(进出路线)个 `bit` 和第 **20**(进出区域)个 `bit` 是 **1**。

```java
class BuiltinMsg0200Test extends BaseReqRespMsgTest {
    final String hex = "7E0200402D01000000000139123443210000003000000040000101CD41C2072901B00929029A005A23042821314101040000029B0202004303020309300163897E";

    @Test
    void test2019Alias() {
        final BuiltinMsg0200V2019Alias msg = decode(hex, BuiltinMsg0200V2019Alias.class);
        assertMsg(msg);
    }

    private void assertMsg(BuiltinMsg0200V2019Alias msg) {
        final BitOperator alarmStatus = BitOperator.mutable(msg.getAlarmFlag());
        // final BitOperator alarmStatus = msg.getBitOperator();
        assertEquals(1, alarmStatus.get(20));
        assertEquals(1, alarmStatus.get(21));
    }
    // ...
}
```

## 简单使用

```java
class BitOperatorTest {

    @Test
    void testBit() {
        // 64个bit: 全是0
        final long x = 0;
        // 第 1、3、22 个 bit 被置为 1，其他位没变
        final BitOperator operator = BitOperator.mutable(x).set(1).set(3).set(22);
        assertEquals("0000000000000000000000000000000000000000010000000000000000001010", operator.binaryString(64));
        assertEquals("00000000010000000000000000001010", operator.binaryString(32));
        assertEquals("010000000000000000001010", operator.binaryString(24));

        // 第 1 个 bit 被置为 0
        final BitOperator operator1 = operator.reset(1);
        assertEquals("010000000000000000001000", operator1.binaryString(24));

        final int y = 0b11111111;
        // 第 3 个 bit 被置为 0
        assertEquals("11110111", BitOperator.mutable(y).reset(3).binaryString(8));
        // 从第 3 个 bit 开始, 连续将 2 个 bit 置为 0
        assertEquals("11100111", BitOperator.mutable(y).resetRange(3, 2).binaryString(8));

        final long z = 0b00001000;
        // z 的第 3 个 bit 是 1
        assertEquals(1, BitOperator.mutable(z).get(3));

        assertEquals(0b11, BitOperator.mutable(x).set(1).set(0).wordValue());
        assertEquals(3, BitOperator.mutable(x).set(1).set(0).wordValue());
    }
}
```


