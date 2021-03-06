package model.mail;

public class MailServer
{
    public static enum SMTP
    {
        Gmail("smtp.gmail.com"), //
        Hotmail("smtp.live.com"), //
        Outlook("smtp.live.com"), //
        Gmx("mail.gmx.net");

        private String smtp;

        SMTP(String smtp)
        {
            this.smtp = smtp;
        }

        public String getSmtp()
        {
            return smtp;
        }

        public static SMTP getDefaultSmtp()
        {
            return Gmail;
        }
    }

    private String smtp;
    private int port;

    public MailServer(String smtp, int port)
    {
        this.smtp = smtp;
        this.port = port;
    }

    public MailServer(String smtp, String port) throws NumberFormatException
    {
        this.smtp = smtp;
        this.port = parsePort(port);
    }

    public String getSmtp()
    {
        return smtp;
    }

    public void setSmtp(String smtp)
    {
        this.smtp = smtp;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setPort(String port) throws NumberFormatException
    {
        setPort(parsePort(port));
    }

    private int parsePort(String stringPort) throws NumberFormatException
    {
        try
        {
            return Integer.parseInt(stringPort);
        }
        catch ( NumberFormatException e )
        {
            throw new NumberFormatException("Could not parse the port: " + e.getMessage().toLowerCase());
        }
    }

    public static MailServer getGmailServer()
    {
        return new MailServer(SMTP.getDefaultSmtp().getSmtp(), 587);
    }

    public boolean isValid()
    {
        if ( smtp == null || smtp.length() == 0 )
            return false;
        if ( port < 0 )
            return false;
        return true;
    }
}
