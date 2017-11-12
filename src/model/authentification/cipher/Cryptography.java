package model.authentification.cipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public abstract class Cryptography
{
    public static final String AES = "AES";
    private Cipher cipher;

    public final String KEY = "4D371DDD8AA122F0C613612B991C6E58";

    protected Cryptography(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
        SecretKeySpec sks = new SecretKeySpec(hexStringToByteArray(KEY), AES);
        cipher = Cipher.getInstance(AES);
        cipher.init(mode, sks);
    }

    protected Cipher getCipher()
    {
        return cipher;
    }

    protected String byteArrayToHexString(byte[] b)
    {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for ( int i = 0; i < b.length; i++ )
        {
            int v = b[i] & 0xff;
            if ( v < 16 )
            {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    protected byte[] hexStringToByteArray(String s)
    {
        byte[] b = new byte[s.length() / 2];
        for ( int i = 0; i < b.length; i++ )
        {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte)v;
        }
        return b;
    }

    // private byte[] getKey() throws NoSuchAlgorithmException
    // {
    // KeyGenerator keyGen = KeyGenerator.getInstance(AES);
    // keyGen.init(128);
    // SecretKey sk = keyGen.generateKey();
    // byte[] encoded = sk.getEncoded();
    // String byteArrayToHexString = byteArrayToHexString(encoded);
    // System.out.println(byteArrayToHexString);
    // return hexStringToByteArray(byteArrayToHexString);
    // }
}
