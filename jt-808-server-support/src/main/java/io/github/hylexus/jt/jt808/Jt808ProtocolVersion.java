package io.github.hylexus.jt.jt808;

import io.github.hylexus.jt.utils.Jdk8Adapter;

import java.util.Set;

/**
 * Created At 2020-07-20 19:03
 *
 * @author hylexus
 */
public enum Jt808ProtocolVersion {
    /**
     * 自动检测2019版||2013版
     */
    AUTO_DETECTION("ALL", (byte) -1),
    /**
     * 2013 版
     */
    VERSION_2013("2013", (byte) 0),
    /**
     * 2011 版
     */
    VERSION_2011("2011", (byte) 0),
    /**
     * 2019 版
     */
    VERSION_2019("2019", (byte) 1),
    ;

    /**
     * 消息体属性中 第14位
     */
    private final byte versionBit;
    private final String shortDesc;

    private static final Set<Jt808ProtocolVersion> V_AUTO_DETECTION = Jdk8Adapter.setOf(AUTO_DETECTION);
    private static final Set<Jt808ProtocolVersion> V2019 = Jdk8Adapter.setOf(VERSION_2019);
    private static final Set<Jt808ProtocolVersion> V2013 = Jdk8Adapter.setOf(VERSION_2013);
    private static final Set<Jt808ProtocolVersion> V2011 = Jdk8Adapter.setOf(VERSION_2011);

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

    public static Set<Jt808ProtocolVersion> unmodifiableSetVersion2013() {
        return V2013;
    }

    public static Set<Jt808ProtocolVersion> unmodifiableSetVersion2011() {
        return V2011;
    }

    // getters
    public byte getVersionBit() {
        return versionBit;
    }

    public String getShortDesc() {
        return shortDesc;
    }
}
