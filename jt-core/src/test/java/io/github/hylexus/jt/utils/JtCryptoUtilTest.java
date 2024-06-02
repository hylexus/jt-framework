package io.github.hylexus.jt.utils;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JtCryptoUtilTest {

    @Test
    void testSm4Ecb() {
        final byte[] key = Randoms.randomBytes(16);
        final String plaintext = "你好啊！123456";
        final byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);

        final byte[] encrypted = JtCryptoUtil.SM4.ecbEncrypt(key, data);
        final byte[] decrypted = JtCryptoUtil.SM4.ecbDecrypt(key, encrypted);
        assertArrayEquals(data, decrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testSm4Cbc() {
        final byte[] key = Randoms.randomBytes(16);
        final byte[] iv = Randoms.randomBytes(16);
        final String plaintext = "你好啊！123456";
        final byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);

        final byte[] encrypted = JtCryptoUtil.SM4.cbcEncrypt(key, data, iv);
        final byte[] decrypted = JtCryptoUtil.SM4.cbcDecrypt(key, encrypted, iv);
        assertArrayEquals(data, decrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testRsa() {
        final String plaintext = "你好啊！123456";
        final byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        final KeyPair keyPair = JtCryptoUtil.RSA.generateKeyPair(1024);
        final byte[] encrypted = JtCryptoUtil.RSA.encrypt(data, keyPair.getPublic());
        final byte[] decrypted = JtCryptoUtil.RSA.decrypt(encrypted, keyPair.getPrivate());
        assertArrayEquals(data, decrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testAesCbc() {
        final String plaintext = "你好啊！123456";
        final byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        final byte[] key = Randoms.randomBytes(16);
        final byte[] iv = Randoms.randomBytes(16);
        final byte[] encrypted = JtCryptoUtil.AES.cbcEncrypt(key, data, iv);
        final byte[] decrypted = JtCryptoUtil.AES.cbcDecrypt(key, encrypted, iv);
        assertArrayEquals(data, decrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testAesEcb() {
        final String plaintext = "你好啊！123456";
        final byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        final byte[] key = Randoms.randomBytes(16);
        final byte[] encrypted = JtCryptoUtil.AES.ecbEncrypt(key, data);
        final byte[] decrypted = JtCryptoUtil.AES.ecbDecrypt(key, encrypted);
        assertArrayEquals(data, decrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testDesCbc() {
        final String plaintext = "你好啊！123456";
        final byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        final byte[] key = Randoms.randomBytes(8);
        final byte[] iv = Randoms.randomBytes(8);
        final byte[] encrypted = JtCryptoUtil.DES.cbcEncrypt(key, data, iv);
        final byte[] decrypted = JtCryptoUtil.DES.cbcDecrypt(key, encrypted, iv);
        assertArrayEquals(data, decrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testDescEcb() {
        final String plaintext = "你好啊！123456";
        final byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        final byte[] key = Randoms.randomBytes(8);
        final byte[] encrypted = JtCryptoUtil.DES.ecbEncrypt(key, data);
        final byte[] decrypted = JtCryptoUtil.DES.ecbDecrypt(key, encrypted);
        assertArrayEquals(data, decrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }
}
