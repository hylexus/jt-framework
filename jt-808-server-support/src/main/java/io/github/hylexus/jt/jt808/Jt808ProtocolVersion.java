package io.github.hylexus.jt.jt808;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

/**
 * Created At 2020-07-20 19:03
 *
 * @author hylexus
 */
public enum Jt808ProtocolVersion {
    /**
     * 自动检测(2019版||2011版)
     */
    AUTO_DETECTION("ALL", (byte) -1),
    /**
     * 仅仅支持 2011 版
     */
    VERSION_2011("2011", (byte) 0),
    /**
     * 仅仅支持 2019 版
     */
    VERSION_2019("2019", (byte) 1),
    ;

    /**
     * 消息体属性中 第14位
     */
    private final byte versionBit;
    private final String shortDesc;

    private static final Set<Jt808ProtocolVersion> V_AUTO_DETECTION = Collections.unmodifiableSet(Sets.newHashSet(AUTO_DETECTION));
    private static final Set<Jt808ProtocolVersion> V2019 = Collections.unmodifiableSet(Sets.newHashSet(VERSION_2019));
    private static final Set<Jt808ProtocolVersion> V2011 = Collections.unmodifiableSet(Sets.newHashSet(VERSION_2011));
    private static final Set<Jt808ProtocolVersion> V2011_AND_2019 = Collections.unmodifiableSet(Sets.newHashSet(VERSION_2011, VERSION_2019));

    Jt808ProtocolVersion(String shortDesc, byte versionBit) {
        this.shortDesc = shortDesc;
        this.versionBit = versionBit;
    }

    public static Set<Jt808ProtocolVersion> unmodifiableSetVersionAutoDetection() {
        return V_AUTO_DETECTION;
    }

    public static Set<Jt808ProtocolVersion> unmodifiableSetVersion2019() {
        return V2019;
    }

    public static Set<Jt808ProtocolVersion> unmodifiableSetVersion2011() {
        return V2011;
    }

    public static Set<Jt808ProtocolVersion> unmodifiableSetVersion2011And2019() {
        return V2011_AND_2019;
    }

    // getters
    public byte getVersionBit() {
        return versionBit;
    }

    public String getShortDesc() {
        return shortDesc;
    }
}
