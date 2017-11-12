package model.mail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPSendFailedException;

import model.authentification.Credential;

public class MailSender
{
    private static final String DOCTYPE_HTML_STRING = "<!doctype html";
    private static final String HTML_STRING = "<html";
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");

    private String sender;
    private String receiver;
    private String password;
    private String subject;
    private String body;
    private List<File> attachments = new ArrayList<>();

    // default gmail
    private String mailServer = "smtp.gmail.com";
    private int port = 587;

    private boolean debug = false;

    public MailSender(String sender, String password, String mailServer, int port)
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

    public MailSender(Credential credential, MailServer mailServer)
    {
        this(credential.getLogin(), credential.getPassword(), mailServer.getSmtp(), mailServer.getPort());
    }

    public MailSender setAttachements(List<File> attachments)
    {
        this.attachments = attachments;
        return this;
    }

    public MailSender setReceiver(String receiver)
    {
        if ( receiver == null || receiver.isEmpty() )
            throw new NullPointerException("Receiver should not be empty !");
        this.receiver = receiver;
        return this;
    }

    public MailSender setSubject(String subject)
    {
        if ( subject == null || subject.isEmpty() )
            throw new NullPointerException("Subject should not be empty !");
        this.subject = subject;
        return this;
    }

    public MailSender setBody(String body)
    {
        if ( body == null || body.isEmpty() )
            throw new NullPointerException("Body should not be empty !");
        this.body = body;
        return this;
    }

    public boolean send(String contactPerson) throws AddressException, MessagingException
    {
        if ( mailServer == null )
            throw new RuntimeException("Mail server should be set to send the email!");
        boolean success = false;
        try
        {
            System.out.println("sending email to \"" + receiver + "\" via smtp host \"" + mailServer + "\"...");

            String protocol = "smtp";

            Properties props = new Properties(System.getProperties());
            if ( mailServer != null )
            {
                props.put("mail." + protocol + ".port", String.valueOf(port)); //$NON-NLS-1$
                props.put("mail." + protocol + ".host", mailServer); //$NON-NLS-1$
            }

            if ( sender != null && sender.length() == 0 )
                sender = null;

            if ( sender != null )
            {
                props.put("mail." + protocol + ".auth", "true");
                props.put("mail." + protocol + ".starttls.enable", "true");
                props.put("mail." + protocol + ".ssl.trust", "*");
            }

            Session session = Session.getInstance(props, null);
            session.setDebug(debug);

            Message msg = new MimeMessage(session);

            if ( sender != null )
                msg.setFrom(new InternetAddress(sender));

            try
            {
                if ( receiver != null )
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver, false));
            }
            catch ( RuntimeException x )
            {
                throw new AddressException("error in email receiver (" + x.getClass().getName() + ")", receiver);
            }

            if ( msg.getAllRecipients() == null )
                throw new AddressException("unable to send mail without recipients");

            msg.setSubject(subject);
            boolean sendAsHtml = false;
            if ( body != null )
            {
                body.replaceAll("${CONTACT_PERSON}", contactPerson);
                String bodyTrimmedLoweCase = body.trim().toLowerCase();
                sendAsHtml = (bodyTrimmedLoweCase.startsWith(DOCTYPE_HTML_STRING)
                        || bodyTrimmedLoweCase.startsWith(HTML_STRING));
            }

            if ( attachments.isEmpty() )
            {
                if ( sendAsHtml )
                    msg.setContent(body, "text/html; charset=utf-8");
                else
                    msg.setText(body);
            }
            else
            {
                MimeMultipart multiPart = new MimeMultipart();

                MimeBodyPart textBodyPart = new MimeBodyPart();
                if ( sendAsHtml )
                    textBodyPart.setContent(body, "text/html; charset=utf-8");
                else
                    textBodyPart.setText(body);
                multiPart.addBodyPart(textBodyPart);

                for ( File attachment : attachments )
                {
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachment);
                    bodyPart.setDataHandler(new DataHandler(source));
                    bodyPart.setFileName(attachment.getName());

                    multiPart.addBodyPart(bodyPart);
                }
                if ( sendAsHtml )
                    msg.setContent(multiPart, "text/html; charset=utf-8");
                else
                    msg.setContent(multiPart);
            }

            msg.setSentDate(new Date());

            msg.saveChanges();

            if ( sender == null )
            {
                Transport.send(msg);
            }
            else
            {
                Transport t = session.getTransport(protocol);
                try
                {
                    t.connect(sender, password);
                    t.sendMessage(msg, msg.getAllRecipients());
                }
                finally
                {
                    t.close();
                }
            }

            success = true;
        }
        catch ( SMTPSendFailedException e )
        {
            String msg = e.getMessage();
            if ( "550 No SMTP server defined. Use real server address instead of 127.0.0.1 in your account.\n".equals(
                    msg) )
                throw new SendFailedException("Antivirus is blocking SMTP server connection.", e);

            throw e;
        }
        finally
        {
            System.out.println("... email was" + (success ? "" : " NOT") + " sent");
        }
        return success;
    }

    public String saveMail(String company, String contactPerson)
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
