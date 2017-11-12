package model.mail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.authentification.Credential;

public class Mail
{
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");

    private String sender;
    private String password;

    // default gmail
    private String mailServer = "smtp.gmail.com";
    private int port = 587;

    private String company;
    private String receiver;
    private String subject;
    private String contactPerson;
    private String body;
    private List<File> attachments = new ArrayList<>();

    public Mail(String sender, String password, String mailServer, int port)
    {
        if ( sender == null || sender.isEmpty() )
            throw new IllegalArgumentException("Sender should not be empty !");
        if ( password == null || password.isEmpty() )
            throw new IllegalArgumentException("Password should not be empty !");
        if ( mailServer == null || mailServer.isEmpty() )
            throw new IllegalArgumentException("Mail server should not be empty !");
        if ( port < 0 )
            throw new IllegalArgumentException("Port should not be negative!");
        this.sender = sender;
        this.password = password;
        this.mailServer = mailServer;
        this.port = port;
    }

    public Mail(Credential credential, MailServer mailServer)
    {
        this(credential.getLogin(), credential.getPassword(), mailServer.getSmtp(), mailServer.getPort());
    }

    public Mail setCompany(String company)
    {
        this.company = company;
        return this;
    }

    public Mail setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
        return this;
    }

    public Mail setAttachements(List<File> attachments)
    {
        this.attachments = attachments;
        return this;
    }

    public Mail setReceiver(String receiver)
    {
        if ( receiver == null || receiver.isEmpty() )
            throw new NullPointerException("Receiver should not be empty !");
        this.receiver = receiver;
        return this;
    }

    public Mail setSubject(String subject)
    {
        if ( subject == null || subject.isEmpty() )
            throw new NullPointerException("Subject should not be empty !");
        this.subject = subject;
        return this;
    }

    public Mail setBody(String body)
    {
        if ( body == null || body.isEmpty() )
            throw new NullPointerException("Body should not be empty !");
        this.body = body;
        return this;
    }

    public String getSender()
    {
        return sender;
    }

    public String getPassword()
    {
        return password;
    }

    public String getMailServer()
    {
        return mailServer;
    }

    public int getPort()
    {
        return port;
    }

    public String getCompany()
    {
        return company;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public String getBody()
    {
        return body;
    }

    public List<File> getAttachments()
    {
        return attachments;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(company);
        sb.append(";");
        sb.append(subject);
        sb.append(";");
        sb.append(format.format(new Date()));
        sb.append(";");
        sb.append(receiver);
        sb.append(";");
        sb.append(contactPerson);
        sb.append(";");
        return sb.toString();
    }
}
