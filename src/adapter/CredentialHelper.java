package adapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import model.authentification.Credential;
import model.authentification.cipher.DecryptPassword;
import model.authentification.cipher.EncryptPassword;

public class CredentialHelper
{
    private static final String PROPERTIES_FILE_NAME = "applications.properties";

    private final static String LOGIN = "login";
    private final static String PASSWORD = "password";

    public static Credential getOrCreateCredentialFromProperties()
    {
        Credential credential = null;
        File properties = new File(PROPERTIES_FILE_NAME);
        if ( properties.exists() )
        {
            try
            {
                Properties prop = new Properties();
                InputStream input = null;
                input = new FileInputStream(properties.getAbsolutePath());
                prop.load(input);
                String login = prop.getProperty(LOGIN);
                String password = prop.getProperty(PASSWORD);
                credential = new Credential(login, decryptPassword(password));
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }

        if ( credential == null )
            credential = new Credential(null, null);
        return credential;
    }

    public static void saveProperties(Credential credential)
    {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(PROPERTIES_FILE_NAME, false)))
        {
            Properties prop = new Properties();
            prop.setProperty(LOGIN, credential.getLogin());
            prop.setProperty(PASSWORD, encryptPassword(credential.getPassword()));
            prop.store(output, null);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void deleteProperties()
    {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(PROPERTIES_FILE_NAME, false)))
        {
            Properties prop = new Properties();
            prop.store(output, null);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static boolean propertiesFileExists()
    {
        File properties = new File(PROPERTIES_FILE_NAME);
        return properties.exists();
    }

    public static String encryptPassword(String decryptedPassword)
    {
        if ( decryptedPassword == null )
            return null;

        String encryptedPassword = null;
        try
        {
            encryptedPassword = EncryptPassword.getInstance().encode(decryptedPassword);
        }
        catch ( InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException e )
        {
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    public static String decryptPassword(String encryptedPassword)
    {
        if ( encryptedPassword == null )
            return null;

        String decryptedPassword = null;
        try
        {
            decryptedPassword = DecryptPassword.getInstance().decrypt(encryptedPassword);
        }
        catch ( InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException e )
        {
            e.printStackTrace();
        }
        return decryptedPassword;
    }

    public static void main(String[] args)
    {
        Credential orCreateCredentialFromProperties = CredentialHelper.getOrCreateCredentialFromProperties();
        System.out.println(
                orCreateCredentialFromProperties.getLogin() + "-" + orCreateCredentialFromProperties.getPassword());
        try (BufferedWriter output = new BufferedWriter(new FileWriter(PROPERTIES_FILE_NAME, false)))
        {
            Properties prop = new Properties();
            prop.setProperty("login", "test");
            prop.setProperty("password", "testpass");
            prop.store(output, null);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

}
