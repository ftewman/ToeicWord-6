package com.groupthree.dictionary.controller.listen;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncodeName {
    public static String a = "abc123j";

    public static String encodeFileName(String str) {
        try {
            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] arrayOfByte1 = new byte[16];
            byte[] arrayOfByte2 = a.getBytes("UTF-8");
            int j = arrayOfByte2.length;
            int i = j;
            if (j > arrayOfByte1.length) {
                i = arrayOfByte1.length;
            }
            System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, i);
            localCipher.init(1, new SecretKeySpec(arrayOfByte1, "AES"), new IvParameterSpec(arrayOfByte1));
            return Base64.encodeToString(localCipher.doFinal(str.getBytes("UTF-8")), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
