package io.github.hylexus.jt.utils;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JtWebUtilsTest {

    @Test
    void prefersForwardedForAndStopsReadingAfterFindingAnAddress() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Forwarded-For", " 203.0.113.8 , 192.0.2.5, 192.0.2.6 ");
        headers.put("X-Real-IP", "198.51.100.4");
        List<String> reads = new ArrayList<>();

        assertEquals(Optional.of("203.0.113.8"), JtWebUtils.getClientIp(name -> {
            reads.add(name);
            return headers.get(name);
        }));
        assertEquals(Collections.singletonList("X-Forwarded-For"), reads);
    }

    @Test
    void fallsBackToRealIpWhenTheFirstCandidateIsUnavailable() {
        for (String forwarded : Arrays.asList(null, "", " \t ", " unknown ", " UnKnOwN , 192.0.2.1", ", 192.0.2.1")) {
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Forwarded-For", forwarded);
            headers.put("X-Real-IP", " 198.51.100.4 ");
            assertEquals(Optional.of("198.51.100.4"), JtWebUtils.getClientIp(headers::get),
                    "X-Forwarded-For=" + forwarded);
        }
    }

    @Test
    void returnsEmptyWhenNeitherDefaultHeaderHasAnAddress() {
        Map<String, String> headers = new HashMap<>();
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(headers::get));
        headers.put("X-Forwarded-For", "unknown");
        headers.put("X-Real-IP", "  ");
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(headers::get));
    }

    @Test
    void doesNotGuessOldHeaderAliasesUnlessExplicitlyRequested() {
        Map<String, String> headers = new HashMap<>();
        headers.put("HTTP_CLIENT_IP", "203.0.113.19");
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(headers::get));
        assertEquals(Optional.of("203.0.113.19"), JtWebUtils.getClientIp(headers::get, "HTTP_CLIENT_IP"));
    }

    @Test
    void customHeaderOrderReplacesTheDefaultOrder() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Forwarded-For", "203.0.113.1");
        headers.put("Proxy-A", "unknown");
        headers.put("Proxy-B", " 198.51.100.2, 192.0.2.2 ");
        headers.put("Proxy-C", "198.51.100.3");
        List<String> reads = new ArrayList<>();

        assertEquals(Optional.of("198.51.100.2"), JtWebUtils.getClientIp(name -> {
            reads.add(name);
            return headers.get(name);
        }, "Proxy-A", "Proxy-B", "Proxy-C"));
        assertEquals(Arrays.asList("Proxy-A", "Proxy-B"), reads);
    }

    @Test
    void preservesIpv6TextWithoutAddressResolution() {
        assertEquals(Optional.of("2001:db8::8"), JtWebUtils.getClientIp(
                name -> "X-Forwarded-For".equals(name) ? " 2001:db8::8 , 2001:db8::9 " : null));
    }

    @Test
    void doesNotUseLaterAddressesFromAnUnavailableFirstCandidate() {
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(name -> "unknown, 203.0.113.2", "Proxy"));
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(name -> ", 203.0.113.2", "Proxy"));
    }

    @Test
    void emptyCustomHeaderListReadsNothing() {
        assertEquals(Optional.empty(), JtWebUtils.getClientIp(name -> {
            fail("No header should be read");
            return null;
        }, new String[0]));
    }

    @Test
    void rejectsNullInputsAndPreservesProviderFailures() {
        assertThrows(NullPointerException.class, () -> JtWebUtils.getClientIp(null));
        assertThrows(NullPointerException.class, () -> JtWebUtils.getClientIp(name -> null, (String[]) null));
        assertThrows(NullPointerException.class, () -> JtWebUtils.getClientIp(name -> null, (String) null));
        IllegalStateException error = new IllegalStateException("header lookup failed");
        assertSame(error, assertThrows(IllegalStateException.class, () -> JtWebUtils.getClientIp(name -> {
            throw error;
        })));
    }
}
