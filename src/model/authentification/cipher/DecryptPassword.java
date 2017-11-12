package model.authentification.cipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DecryptPassword extends Cryptography
{
    private static DecryptPassword INSTANCE = null;

    // 0618F26FC8D4BC90F6DE945B6BA06D2D
    // 42271F7A177389E80A90D4B6B69A9015
    // **************** Encrypted Password ****************
    // 4109F282FED0A12D118240700D4E1132

    protected DecryptPassword() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
        super(Cipher.DECRYPT_MODE);
    }

    public static DecryptPassword getInstance()
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        if ( INSTANCE == null )
            INSTANCE = new DecryptPassword();
        return INSTANCE;
    }

    public String decrypt(String password) throws IllegalBlockSizeException, BadPaddingException
    {
        byte[] decrypted = getCipher().doFinal(hexStringToByteArray(password));
        String OriginalPassword = new String(decrypted);
        return OriginalPassword;
    }

    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException
    {
        String password = "53C5F6F2654FA6864E7D8C649B32A909";

        DecryptPassword instance = DecryptPassword.getInstance();
        System.out.println("****************  Original Password  ****************");
        System.out.println(instance.decrypt(password));
        System.out.println("****************  Original Password  ****************");
    }
}
