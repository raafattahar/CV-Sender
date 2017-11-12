package model;

import java.util.Properties;

import adapter.CredentialHelper;
import adapter.PropertiesHelper;
import model.authentification.Credential;
import model.mail.MailServer;

public class PropertiesModelCache
{
    private Credential credential;
    private MailServer mailServer;

    public PropertiesModelCache(Credential credential, MailServer mailServer)
    {
        this.credential = credential;
        this.mailServer = mailServer;
    }

    public Credential getCredential()
    {
        return credential;
    }

    public MailServer getMailServer()
    {
        return mailServer;
    }

    public Properties getProperties()
    {
        Properties prop = new Properties();
        prop.setProperty(PropertiesHelper.LOGIN, credential.getLogin());
        prop.setProperty(PropertiesHelper.PASSWORD, CredentialHelper.encryptPassword(credential.getPassword()));
        if ( mailServer.getSmtp() != null )
            prop.setProperty(PropertiesHelper.SMTP, mailServer.getSmtp());
        if ( mailServer.getPort() != -1 )
            prop.setProperty(PropertiesHelper.SMTP_PORT, "" + mailServer.getPort());
        return prop;
    }
}
