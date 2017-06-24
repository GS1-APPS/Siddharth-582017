package org.gs1us.sgl.security;

public class Base64
{
    private static final int    BASE_64_CHAR_LIMIT = 128;
    private static final char NOPAD = '\0';
    
    public static final Base64 BASE64    = new Base64("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
    public static final Base64 BASE64URL = new Base64("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", NOPAD);

    private static int[] invertBase64Chars(char[] base64Chars, char base64PadChar)
    {
        int[] result = new int[BASE_64_CHAR_LIMIT];
        
        for (int i = 0; i < BASE_64_CHAR_LIMIT; i++)
            result[i] = -1;
        
        for (int i = 0; i < base64Chars.length; i++)
            result[base64Chars[i]] = i;
        
        result[base64PadChar] = 0;
        
        return result;
    }
    
    private char[] m_base64Chars;
    private char m_base64PadChar;
    private int[] m_base64Indices;
    
    public Base64(String base64Chars, char base64PadChar)
    {
        m_base64Chars = base64Chars.toCharArray();
        m_base64PadChar = base64PadChar;
        m_base64Indices = invertBase64Chars(m_base64Chars, base64PadChar);
    }

    public String encode(String s)
    {
        if (s == null)
            return "";
        
        byte[] bytes = s.getBytes();
        return encode(bytes);
    }

    public String encode(byte[] bytes)
    {
        if (bytes == null)
            return "";

        int byteCount = bytes.length;
        StringBuffer result = new StringBuffer(((byteCount + 2) / 3) * 4);
        for (int i = 0; i < byteCount; i += 3)
        {
            int b0 = ((int)bytes[i]) & 0xFF;
            int b1 = (i+1 < byteCount ? ((int)bytes[i+1]) & 0xFF: 0);
            int b2 = (i+2 < byteCount ? ((int)bytes[i+2]) & 0xFF: 0);
            
            int b012 = (b0 << 16) | (b1 << 8) | b2;
            
            char c0 = m_base64Chars[(b012 >> 18) & 0x3F];
            char c1 = m_base64Chars[(b012 >> 12) & 0x3F];
            char c2 = m_base64Chars[(b012 >>  6) & 0x3F];
            char c3 = m_base64Chars[(b012      ) & 0x3F];
            
            if (i+1 >= byteCount)
                c2 = m_base64PadChar;
            if (i+2 >= byteCount)
                c3 = m_base64PadChar;
            
            result.append(c0);
            result.append(c1);
            if (c2 != NOPAD)
                result.append(c2);
            if (c3 != NOPAD)
                result.append(c3);
            
        }
        return result.toString();
    }

    public byte[] decode(String s)
    {
        if (s == null)
            return null;
        
        if (s.length() % 4 != 0)
        {
            if (m_base64PadChar == NOPAD)
            {
                int padCount = 4 - (s.length() % 4);
                s += "\0\0\0".substring(0, padCount);
            }
            else
                return null;
        }
        
        int charCount = s.length();

        int byteCount = (charCount / 4) * 3;
        if (s.charAt(charCount-2) == m_base64PadChar)
            byteCount--;
        if (s.charAt(charCount-1) == m_base64PadChar)
            byteCount--;

        byte[] bytes = new byte[byteCount];

        int nextByte = 0;
        for (int i = 0; i < charCount; i += 4)
        {
            int i0 = m_base64Indices[s.charAt(i)];
            int i1 = m_base64Indices[s.charAt(i+1)];
            int i2 = m_base64Indices[s.charAt(i+2)];
            int i3 = m_base64Indices[s.charAt(i+3)];
            
            if (i0 < 0 || i1 < 0 || i2 < 0 || i3 < 0)
                return null;
            
            int i0123 = (i0 << 18) | (i1 << 12) | (i2 << 6) | i3;
            
            byte b0 = (byte)((i0123 >> 16) & 0xFF);
            byte b1 = (byte)((i0123 >>  8) & 0xFF);
            byte b2 = (byte)((i0123      ) & 0xFF);
            
            bytes[nextByte] = b0;
            if (nextByte+1 < byteCount)
                bytes[nextByte+1] = b1;
            if (nextByte+2 < byteCount)
                bytes[nextByte+2] = b2;

            nextByte += 3;
        }
        return bytes;
    }

    public String decodeString(String s)
    {
        if (s == null)
            return "";
        
        byte[] bytes = decode(s);
        if (bytes == null)
            return null;
        else
            return new String(bytes);
    }
}
