package adapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import model.PropertiesModelCache;
import model.authentification.Credential;
import model.mail.MailServer;

public class PropertiesHelper
{
    private static final String PROPERTIES_FILE_NAME = "applications.properties";

    public final static String LOGIN = "login";
    public final static String PASSWORD = "password";
    public final static String SMTP = "smtp";
    public final static String SMTP_PORT = "port";

    //
    // Read Properties attributes
    //

    public static Credential getOrCreateCredentialFromProperties()
    {
        Credential credential = null;
        try
        {
            Properties properties = getProperties();
            if ( properties != null )
                credential = new Credential(properties.getProperty(LOGIN),
                        CredentialHelper.decryptPassword(properties.getProperty(PASSWORD)));
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        if ( credential == null )
            credential = new Credential(null, null);
        return credential;
    }

    public static MailServer getOrCreateMailServerFromProperties()
    {
        MailServer mailServer = null;
        try
        {
            Properties properties = getProperties();
            if ( properties != null )
                mailServer = new MailServer(properties.getProperty(SMTP), properties.getProperty(SMTP_PORT));
        }
        catch ( NumberFormatException e )
        {
            // ignore
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        if ( mailServer == null )
            mailServer = new MailServer(null, -1);
        return mailServer;
    }

    //
    // Save Properties
    //

    public static void saveProperties(PropertiesModelCache modelCache)
    {
        storeProperties(modelCache.getProperties());
    }

    //
    // Delete Properties
    //

    public static void deleteLoginProperties()
    {
        try
        {
            Properties properties = getProperties();
            if ( properties != null )
            {
                properties.remove(LOGIN);
                properties.remove(PASSWORD);

                storeProperties(properties);
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

    }

    //
    // Write Properties to file
    //

    private static void storeProperties(Properties prop)
    {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(PROPERTIES_FILE_NAME, false)))
        {
            prop.store(output, null);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    //
    // Read Properties from File
    //

    public static boolean propertiesFileExists()
    {
        File properties = new File(PROPERTIES_FILE_NAME);
        return properties.exists();
    }

    public static Properties getProperties() throws IOException
    {
        Properties props = null;
        if ( propertiesFileExists() )
        {
            props = new Properties();
            InputStream input = null;
            input = new FileInputStream(new File(PROPERTIES_FILE_NAME));
            props.load(input);
        }
        return props;
    }

    //
    // main
    //

    public static void main(String[] args)
    {
        Credential orCreateCredentialFromProperties = PropertiesHelper.getOrCreateCredentialFromProperties();
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
