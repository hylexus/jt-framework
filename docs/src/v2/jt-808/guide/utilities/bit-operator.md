---
icon: bit
---

# BitOperator(v2.1.1)

::: info 提示

`BitOperator` 是 **2.1.1** 中才引入的辅助类。

:::

## 作用

`BitOperator` 是位操作的工具类，目前只支持 **64** 个 `Bit`(`java.lang.Long`) 的操作。

当然 `BitOperator` 也可以用在被 `@RequestField`、`@ResponseField`、`@RequestFieldAlias`、`@ResponseFieldAlias` 修饰的实体类的成员变量上。

## 用途

位操作的场景都可以用到，比如下面这个场景(需要对某个 `bit` 操作)：

<p class="demo">
    <img :src="$withBase('/img/v2/utilities/bit-operator-case-01.png')">
</p>

## 简单示例

```java
import java.util.function.BinaryOperator;

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

        // 第 1 和第 0 个 bit 置为 1 ==> 0b11
        assertEquals(0b11, BitOperator.mutable(x).set(1).set(0).wordValue());
        assertEquals(3, BitOperator.mutable(x).set(1).set(0).wordValue());
    }
}
```


