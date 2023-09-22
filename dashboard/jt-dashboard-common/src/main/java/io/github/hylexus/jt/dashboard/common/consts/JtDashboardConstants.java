package io.github.hylexus.jt.dashboard.common.consts;

import java.time.format.DateTimeFormatter;

public class JtDashboardConstants {
    private JtDashboardConstants() {
    }

    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS_SSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
}
