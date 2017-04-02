package com.hoangsong.zumechat.untils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Tang on 17/06/2016.
 */
public class Encryption {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
            //new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
            new byte[] { 'Q', 'e', 'X', 'h', 'a', '3', '9', 'D', 't', '5', 'v', 'z', 'b', 'u', 'F', 'd' };
            //new byte[] { 'U', 'O', 'W', 'A', 'i', '1', 'F', '3', '7', 'R', '8', 'V', 'S', 'a', '4', 'u', 'A', 't', 'x', 'a' };
            //new byte[] { '0', 'V', 'F', 'W', 'Y', '2', '6', 'D', 'F', 'V', 'c', 'z', 'T', 'V', 't', 'V', 'e', 'R', 'w', 'U' };

    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = Base64.encode(encValue);
        return encryptedValue;
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }
}
