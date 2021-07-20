package com.blotout.utilities;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ankuradhikari on 19,July,2020
 */

public class BOEncryptionManager {

    public static final String ALGORITHM_AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
    private static final byte[] CRYPTO_IVX = "Q0BG17E2819IWZYQ".getBytes();
    private static final String TAG = "SimpleCrypto";
    public static final int MODE_128BIT = 128;
    public static final int MODE_256BIT = 256;
    public static final int MODE_DEFAULT = 1;
    private static final int BASE64_FLAGS = Base64.NO_WRAP;

    private String algorithm;
    private byte[] rawKey;

    public BOEncryptionManager(String algorithm, String passphrase, int mode) {
        this.algorithm = algorithm;
        switch (mode) {
            case MODE_128BIT:
                this.rawKey = getKeyBytes(passphrase, MODE_128BIT);
                break;
            case MODE_256BIT:
                this.rawKey = getKeyBytes(passphrase, MODE_256BIT);
                break;
            default:
                this.rawKey = passphrase.getBytes();
                break;
        }
    }

    public String encrypt(String v) {
        try {
            return toHex(encrypt(v.getBytes()));
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return v;
        }
    }

    public String encrypt(Long v) {
        return toHex(encrypt(String.valueOf(v).getBytes()));
    }

    public String encrypt(Short v) {
        return toHex(encrypt(String.valueOf(v).getBytes()));
    }

    public String encrypt(Integer v) {
        return toHex(encrypt(String.valueOf(v).getBytes()));
    }

    public String encrypt(Float v) {
        return toHex(encrypt(String.valueOf(v).getBytes()));
    }

    public String encrypt(Double v) {
        return toHex(encrypt(String.valueOf(v).getBytes()));
    }

    public String encrypt(Boolean b) {
        return toHex(encrypt(String.valueOf(b).getBytes()));
    }

    public byte[] encrypt(byte[] v) {
        byte[] result = encrypt(rawKey, v);
        return result;
    }

    public byte[] decrypt(byte[] enc) {
        byte[] result = decrypt(rawKey, enc);
        return result;
    }

    public String decryptString(String encrypted) {
        try {
            return new String(decrypt(toByte(encrypted)));
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return encrypted;
        }
    }

    public Long decryptLong(String encrypted) {
        return Long.parseLong(decryptString(encrypted));
    }

    public Short decryptShort(String encrypted) {
        return Short.parseShort(decryptString(encrypted));
    }

    public Integer decryptInteger(String encrypted) {
        return Integer.parseInt(decryptString(encrypted));
    }

    public Boolean decryptBoolean(String encrypted) {
        return Boolean.parseBoolean(decryptString(encrypted));
    }

    public Float decryptFloat(String encrypted) {
        return Float.parseFloat(decryptString(encrypted));
    }

    public Double decryptDouble(String encrypted) {
        return Double.parseDouble(decryptString(encrypted));
    }

    private byte[] encrypt(byte[] raw, byte[] clear) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(CRYPTO_IVX);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(clear);
            return encrypted;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return raw;
        }

    }

    private byte[] decrypt(byte[] raw, byte[] encrypted) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(CRYPTO_IVX);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return raw;
        }
    }

    public String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public String toHex(byte[] buf) {
        try {
            if (buf == null)
                return "";
            StringBuffer result = new StringBuffer(2 * buf.length);
            for (int i = 0; i < buf.length; i++) {
                appendHex(result, buf[i]);
            }
            return result.toString();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

    private final static String HEX = "0123456789ABCDEF";

    private void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    /**
     * This method generate key byte from pass phrase with strict bits size.
     *
     * @param passPhrase
     * @param keyLenghtInBits should be 128/192/256
     * @return raw bytes of keyLenghtInBits/8 size
     */
    private byte[] getKeyBytes(String passPhrase, int keyLenghtInBits) {
        int lenInByte = keyLenghtInBits / 8;
        passPhrase = passPhrase.trim();
        while (passPhrase.getBytes().length < lenInByte) {
            passPhrase += "0000000000000000";
        }
        return Arrays.copyOf(passPhrase.getBytes(), lenInByte);
    }

    public String encryptWithBase64(String data) {
        try {
            String passphrase = new String(rawKey);
            String encrypted = Base64.encodeToString(encrypt(data).getBytes(), BASE64_FLAGS);
            Logger.INSTANCE.d(TAG, "Encrypting \"" + data + "\" with passphrase \"" + passphrase + "\"=\"" + encrypted + "\"");
            return encrypted;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return data;
        }
    }

    public String decryptWithBase64(String data) {
        try {
            byte[] encrypted = Base64.decode(data, BASE64_FLAGS);
            String encrpytedString = new String(encrypted);
            return decryptString(encrpytedString);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return data;
        }
    }

    public static String md5(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(input.getBytes());
            return toHexString(hash);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

    public static String sha1(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hash = sha1.digest(input.getBytes());
            return toHexString(hash);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

    public static String sha512(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-512");
            byte[] hash = sha1.digest(input.getBytes());
            return toHexString(hash);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

    public static String sha256(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha1.digest(input.getBytes());
            return toHexString(hash);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            // Append leading '0' for one digit ints
            if (hex.length() == 1) {
                hexString.append("0").append(hex);
            } else {
                hexString.append(hex);
            }
        }
        return hexString.toString();
    }

    ///Methods related to PHI and PII Data encryption
    public static String encryptText(byte[] msg, String base64PublicKeyString) {
        try {

            /////prepare key from base64String at SDK received from server
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(base64PublicKeyString,Base64.NO_WRAP)));

            ///encryption at SDK end
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptedBase64String = Base64.encodeToString(rsaCipher.doFinal(msg), Base64.NO_WRAP);

            return encryptedBase64String;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

}
