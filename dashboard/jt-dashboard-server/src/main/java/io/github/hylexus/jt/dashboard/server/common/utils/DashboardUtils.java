package io.github.hylexus.jt.dashboard.server.common.utils;

import io.github.hylexus.jt.dashboard.server.model.constants.ProtocolType;
import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt1078Registration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;

public class DashboardUtils {
    public static String constructFlvPlayerUrl(StreamAddressDto dto, JtInstance jt1078ServerInstance) {
        final boolean http = dto.getProtocolType() == ProtocolType.HTTP || dto.getProtocolType() == ProtocolType.HTTPS;
        final String url = dto.getProtocolType().getScheme()
                + "://"
                + ((Jt1078Registration) jt1078ServerInstance.getRegistration()).getHost()
                + ":"
                + ((Jt1078Registration) jt1078ServerInstance.getRegistration()).getHttpPort()
                + "/api/dashboard-client/jt1078/video-stream/"
                + (http ? "http" : "websocket")
                + "/flv/"
                + dto.getSim() + "/" + dto.getChannelNumber();

        if (dto.getTimeout().toSeconds() > 0) {
            return url + "?timeout=" + dto.getTimeout().toMillis();
        }

        return url;
    }
}
