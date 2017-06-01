package org.gs1us.sgl.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil
{
    public static void main(String[] args)
    {
        int bytes = Integer.parseInt(args[0]);
        System.out.println(generateKey(bytes));
    }
    private static SecureRandom s_secureRng = null;
    public synchronized static String generateKey(int bytes)
    {
        int i = 0;
        while (s_secureRng == null && i < 1){
            s_secureRng = new SecureRandom();
            i++;
        }
        
        byte[] key = new byte[bytes];
        
        s_secureRng.nextBytes(key);
        
        return Base64.BASE64URL.encode(key);
    }
    
    public static String generatePasswordResetKey()
    {
        return generateKey(256/8);
    }
    
    public static String validatePasswords(String pw1, String pw2)
    {
        if (pw1 == null || pw2 == null || pw1.length() < 6 || pw2.length() < 6)
            return "Passwords must be at least six characters in length.";
        else if (!pw1.equals(pw2))
            return "Passwords do not match; please try again.";
        else
            return null;
    }

}
