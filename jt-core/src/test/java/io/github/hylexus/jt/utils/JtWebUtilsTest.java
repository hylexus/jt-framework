package io.github.hylexus.jt.utils;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JtWebUtilsTest {
    @Test
    public void testGetClientIp() {
        // Mocking HttpRequestHeaderProvider
        JtWebUtils.HttpRequestHeaderProvider headerProvider = mock(JtWebUtils.HttpRequestHeaderProvider.class);

        // Test case 1: Empty header value
        String headerName1 = "X-Forwarded-For";
        String headerValue1 = "";
        when(headerProvider.get(headerName1)).thenReturn(headerValue1);
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(headerProvider));

        // Test case 2: Header value is "unknown"
        String headerName2 = "HTTP_X_FORWARDED_FOR";
        String headerValue2 = "unknown";
        when(headerProvider.get(headerName2)).thenReturn(headerValue2);
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(headerProvider));

        // Test case 3: Header value is valid IP address
        String headerName3 = "HTTP_CLIENT_IP";
        String headerValue3 = "192.168.0.1";
        when(headerProvider.get(headerName3)).thenReturn(headerValue3);
        assertEquals(Optional.of("192.168.0.1"), JtWebUtils.getClientIp(headerProvider));

        // Test case 4: Header value is invalid IP address with comma
        String headerName4 = "HTTP_VIA";
        String headerValue4 = "192.168.0.1, 192.168.0.2";
        when(headerProvider.get(headerName4)).thenReturn(headerValue4);
        assertEquals(Optional.of("192.168.0.1"), JtWebUtils.getClientIp(headerProvider));
    }
}