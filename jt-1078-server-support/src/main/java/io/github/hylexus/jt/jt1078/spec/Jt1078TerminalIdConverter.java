package io.github.hylexus.jt.jt1078.spec;

import java.util.function.Function;

/**
 * 1078 的 SIM 为 BCD[6]，但是
 * <ol>
 *     <li>v2011 或 2013 版的终端手机号就是 BCD[6]</li>
 *     <li>v2019的终端手机号是 BCD[10]</li>
 * </ol>
 * 默认实现: 截取最后 12(BCD[6]) 个字符
 * <p>
 *
 * @see Jt1078RequestHeader#sim()
 * @see io.github.hylexus.jt.jt1078.spec.impl.DefaultJt1078TerminalIdConverter
 */
@FunctionalInterface
public interface Jt1078TerminalIdConverter extends Function<String, String> {


    String convert(String original);

    default String apply(String s) {
        return this.convert(s);
    }

}
