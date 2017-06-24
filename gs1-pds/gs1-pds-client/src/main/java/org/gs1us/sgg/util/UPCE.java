package org.gs1us.sgg.util;

public class UPCE
{
    public static String gtinToUPCE(String gtin)
    {
        if (gtin.length() != 14 || gtin.charAt(0) != '0' || gtin.charAt(1) != '0' || gtin.charAt(2) != '0')
            return null;
        
        char[] upce = new char[8];
        
        upce[0] = '0';
        upce[7] = gtin.charAt(13);
        
        char d11 = gtin.charAt(12);
        char d10 = gtin.charAt(11);
        char d9  = gtin.charAt(10);
        char d8  = gtin.charAt(9);
        char d7  = gtin.charAt(8);
        char d6  = gtin.charAt(7);
        char d5  = gtin.charAt(6);
        char d4  = gtin.charAt(5);
        char d3  = gtin.charAt(4);
        char d2  = gtin.charAt(3);

        if (d11 >= '5' && d11 <= '9' && d10 == '0' && d9 == '0' && d8 == '0' && d7 == '0' && d6 != '0')
        {
            upce[1] = d2;
            upce[2] = d3;
            upce[3] = d4;
            upce[4] = d5;
            upce[5] = d6;
            upce[6] = d11;
        }
        else if (d10 == '0' && d9 == '0' && d8 == '0' && d7 == '0' && d6 == '0' && d5 != '0')
        {
            upce[1] = d2;
            upce[2] = d3;
            upce[3] = d4;
            upce[4] = d5;
            upce[5] = d11;
            upce[6] = '4';
        }
        else if (d4 >= '0' && d4 <= '2' && d8 == '0' && d7 == '0' && d6 == '0' && d5 == '0')
        {
            upce[1] = d2;
            upce[2] = d3;
            upce[3] = d9;
            upce[4] = d10;
            upce[5] = d11;
            upce[6] = d4;
        }
        else if (d4 >= '3' && d4 <= '9' && d9 == '0' && d8 == '0' && d7 == '0' && d6 == '0' && d5 == '0')
        {
            upce[1] = d2;
            upce[2] = d3;
            upce[3] = d4;
            upce[4] = d10;
            upce[5] = d11;
            upce[6] = '3';
        }
        else
            return null;
        
        return new String(upce);
    }
    
    public static String upceToGtin(String upce)
    {
        if (upce.length() != 8 || upce.charAt(0) != '0')
            return null;
        
        char[] gtin = new char[14];
        
        gtin[0] = '0';
        gtin[1] = '0';
        gtin[2] = '0';
        gtin[13] = upce.charAt(7);
      
        char x1 = upce.charAt(1);
        char x2 = upce.charAt(2);
        char x3 = upce.charAt(3);
        char x4 = upce.charAt(4);
        char x5 = upce.charAt(5);
        char x6 = upce.charAt(6);

        gtin[3] = x1;
        gtin[4] = x2;
        
        switch (x6)
        {
        case '0':
        case '1':
        case '2':
            gtin[5] = x6;
            gtin[6] = '0';
            gtin[7] = '0';
            gtin[8] = '0';
            gtin[9] = '0';
            gtin[10] = x3;
            gtin[11] = x4;
            gtin[12] = x5;
            break;
            
        case '3':
            gtin[5] = x3;
            gtin[6] = '0';
            gtin[7] = '0';
            gtin[8] = '0';
            gtin[9] = '0';
            gtin[10] = '0';
            gtin[11] = x4;
            gtin[12] = x5;
            break;
            
        case '4':
            gtin[5] = x3;
            gtin[6] = x4;
            gtin[7] = '0';
            gtin[8] = '0';
            gtin[9] = '0';
            gtin[10] = '0';
            gtin[11] = '0';
            gtin[12] = x5;
            break;
            
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            gtin[5] = x3;
            gtin[6] = x4;
            gtin[7] = x5;
            gtin[8] = '0';
            gtin[9] = '0';
            gtin[10] = '0';
            gtin[11] = '0';
            gtin[12] = x6;
            break;
            
        default:
            return null;
        }
        
        return new String(gtin);
    }
}
