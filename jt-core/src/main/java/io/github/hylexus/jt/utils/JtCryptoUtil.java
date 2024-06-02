package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.function.Supplier;

/**
 * @author hylexus
 * @since 2.1.4
 */
public class JtCryptoUtil {
    private static final String PROVIDER_NAME = BouncyCastleProvider.PROVIDER_NAME;

    static {
        addProviderIfNecessary();
    }

    private static void addProviderIfNecessary() {

        if (Security.getProvider(PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static class JtCryptoException extends RuntimeException {
        public JtCryptoException(Throwable cause) {
            super(cause);
        }
    }

    private static class Base {

        public static ByteBuf toByteBuf(byte[] data) {
            return ByteBufAllocator.DEFAULT.buffer(data.length).writeBytes(data);
        }

        public static byte[] encrypt(String algorithm, Supplier<Cipher> cipherSupplier, byte[] key, byte[] data) {
            try {
                final SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
                final Cipher cipher = cipherSupplier.get();
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                return cipher.doFinal(data);
            } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                throw new JtCryptoException(e);
            }
        }

        public static byte[] decrypt(String algorithm, Supplier<Cipher> cipherSupplier, byte[] key, byte[] data) {
            try {
                final SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
                final Cipher cipher = cipherSupplier.get();
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                return cipher.doFinal(data);
            } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                throw new JtCryptoException(e);
            }
        }

        public static byte[] encryptWithIv(String algorithm, Supplier<Cipher> cipherSupplier, byte[] key, byte[] data, byte[] iv) {
            try {
                final SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
                final Cipher cipher = cipherSupplier.get();
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
                return cipher.doFinal(data);
            } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                throw new JtCryptoException(e);
            }
        }


        public static byte[] decryptWithIv(String algorithm, Supplier<Cipher> cipherSupplier, byte[] key, byte[] data, byte[] iv) {
            try {
                final SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
                final Cipher cipher = cipherSupplier.get();
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
                return cipher.doFinal(data);
            } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                throw new JtCryptoException(e);
            }
        }

    }

    public static class SM4 {
        static {
            addProviderIfNecessary();
        }

        public static ByteBuf ecbEncrypt(byte[] key, ByteBuf data) {
            final byte[] bytes = JtCommonUtils.getBytes(data);
            final byte[] encrypted = ecbEncrypt(key, bytes);
            return Base.toByteBuf(encrypted);
        }

        public static byte[] ecbEncrypt(byte[] key, byte[] data) {
            return Base.encrypt("SM4", () -> getCipher("SM4/ECB/PKCS5Padding"), key, data);
        }

        public static ByteBuf ecbDecrypt(byte[] key, ByteBuf data) {
            final byte[] bytes = JtCommonUtils.getBytes(data);
            final byte[] decrypted = ecbDecrypt(key, bytes);
            return Base.toByteBuf(decrypted);
        }

        public static byte[] ecbDecrypt(byte[] key, byte[] data) {
            return Base.decrypt("SM4", () -> getCipher("SM4/ECB/PKCS5Padding"), key, data);
        }

        public static byte[] cbcEncrypt(byte[] key, byte[] data, byte[] iv) {
            return Base.encryptWithIv("SM4", () -> getCipher("SM4/CBC/PKCS5Padding"), key, data, iv);
        }

        public static byte[] cbcDecrypt(byte[] key, byte[] data, byte[] iv) {
            return Base.decryptWithIv("SM4", () -> getCipher("SM4/CBC/PKCS5Padding"), key, data, iv);
        }

    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public static class AES {
        static {
            addProviderIfNecessary();
        }

        public static byte[] cbcEncrypt(byte[] key, byte[] data, byte[] iv) {
            return Base.encryptWithIv("AES", () -> getCipher("AES/CBC/PKCS5Padding"), key, data, iv);
        }


        public static byte[] cbcDecrypt(byte[] key, byte[] data, byte[] iv) {
            return Base.decryptWithIv("AES", () -> getCipher("AES/CBC/PKCS5Padding"), key, data, iv);
        }

        public static byte[] ecbEncrypt(byte[] key, byte[] data) {
            return Base.encrypt("AES", () -> getCipher("AES/ECB/PKCS5Padding"), key, data);
        }

        public static byte[] ecbDecrypt(byte[] key, byte[] data) {
            return Base.decrypt("AES", () -> getCipher("AES/ECB/PKCS5Padding"), key, data);
        }
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public static class DES {
        static {
            addProviderIfNecessary();
        }

        public static byte[] cbcEncrypt(byte[] key, byte[] data, byte[] iv) {
            return Base.encryptWithIv("DES", () -> getCipher("DES/CBC/PKCS5Padding"), key, data, iv);
        }

        public static byte[] cbcDecrypt(byte[] key, byte[] data, byte[] iv) {
            return Base.decryptWithIv("DES", () -> getCipher("DES/CBC/PKCS5Padding"), key, data, iv);
        }

        public static byte[] ecbEncrypt(byte[] key, byte[] data) {
            return Base.encrypt("DES", () -> getCipher("DES/ECB/PKCS5Padding"), key, data);
        }

        public static byte[] ecbDecrypt(byte[] key, byte[] data) {
            return Base.decrypt("DES", () -> getCipher("DES/ECB/PKCS5Padding"), key, data);
        }
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public static class RSA {

        static {
            addProviderIfNecessary();
        }

        public static KeyPair generateKeyPair(int keySize) {
            try {
                final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(keySize);
                return kpg.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                throw new JtCryptoException(e);
            }
        }

        public static byte[] encrypt(byte[] data, PublicKey publicKey) {
            try {
                final Cipher cipher = getCipher();
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(data);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                throw new JtCryptoException(e);
            }
        }

        private static Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
            return Cipher.getInstance("RSA");
        }

        public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) {
            try {
                Cipher cipher = getCipher();
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(encryptedData);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                throw new JtCryptoException(e);
            }
        }
    }

    private static Cipher getCipher(String transformation) {
        try {
            return Cipher.getInstance(transformation, PROVIDER_NAME);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            throw new JtCryptoException(e);
        }
    }
}
