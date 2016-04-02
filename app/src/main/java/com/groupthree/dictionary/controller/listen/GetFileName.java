package com.groupthree.dictionary.controller.listen;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Kent on 3/28/2016.
 */
public class GetFileName {

    public static String fileName(String paramString) {
        try {
            String str;
            for (paramString = new BigInteger(1, MessageDigest.getInstance("MD5").digest(paramString.getBytes())).toString(16); ; paramString = "0" + paramString) {
                str = paramString;
                if (paramString.length() >= 32) {
                    break;
                }
            }
            return str;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "hihi";
    }

}
