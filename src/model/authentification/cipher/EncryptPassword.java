package model.authentification.cipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptPassword extends Cryptography
{
    private static EncryptPassword INSTANCE = null;

    private EncryptPassword() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException

    {
        super(Cipher.ENCRYPT_MODE);
    }

    public static EncryptPassword getInstance()
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        if ( INSTANCE == null )
            INSTANCE = new EncryptPassword();
        return INSTANCE;
    }

    public String encode(String password) throws IllegalBlockSizeException, BadPaddingException
    {
        byte[] encrypted = getCipher().doFinal(password.getBytes());
        String encryptedpwd = byteArrayToHexString(encrypted);
        return encryptedpwd;
    }

    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException
    {
        // String key = "DB99A2A8EB6904F492E9DF0595ED683C";
        // String password = "Admin";

        Scanner scanner = new Scanner(System.in);
        // System.out.println("Please Enter Key:");
        // String key = scanner.next();
        System.out.println("Please Enter Plain Text Password:");
        String password = scanner.next();

        EncryptPassword instance = EncryptPassword.getInstance();
        System.out.println("****************  Encrypted Password  ****************");
        System.out.println(instance.encode(password));
        System.out.println("****************  Encrypted Password  ****************");
        scanner.close();
    }
}
