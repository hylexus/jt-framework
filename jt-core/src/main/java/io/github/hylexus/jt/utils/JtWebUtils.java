package io.github.hylexus.jt.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class JtWebUtils {
    public interface HttpRequestHeaderProvider {
        String get(String name);
    }

    /**
     * @see <a href="https://stackoverflow.com/questions/22877350/how-to-extract-ip-address-in-spring-mvc-controller-get-call">https://stackoverflow.com/questions/22877350/how-to-extract-ip-address-in-spring-mvc-controller-get-call</a>
     */
    private static final String[] IP_HEADER_NAMES = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static Optional<String> getClientIp(HttpRequestHeaderProvider headerProvider) {
        for (String headerName : IP_HEADER_NAMES) {
            String ip = headerProvider.get(headerName);
            if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(',');
                if (index != -1) {
                    ip = ip.substring(0, index);
                }
                return Optional.of(ip);
            }
        }
        return Optional.empty();
    }
}
