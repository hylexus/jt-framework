package io.github.hylexus.jt.jt1078.spec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078TerminalIdConverter;

public class DefaultJt1078TerminalIdConverter implements Jt1078TerminalIdConverter {

    @Override
    public String convert(String original) {
        // BCD[6] ==> 12
        // 视为 2013||2011 版
        if (original.length() <= 12) {
            return original;
        }
        // 视为 2019 ==> 只保留最后 12 位
        return original.substring(original.length() - 12);
    }
}
