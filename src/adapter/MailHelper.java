package adapter;

import java.io.File;
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

import model.mail.Mail;

public class MailHelper
{
    private static final String DOCTYPE_HTML_STRING = "<!doctype html";
    private static final String HTML_STRING = "<html";

    private static boolean debug = false;

    public static boolean send(Mail mail) throws AddressException, MessagingException
    {
        String mailServer = mail.getMailServer();
        int port = mail.getPort();

        String sender = mail.getSender();
        String password = mail.getPassword();

        String receiver = mail.getReceiver();
        String subject = mail.getSubject();
        String contactPerson = mail.getContactPerson();
        String body = mail.getBody();
        List<File> attachments = mail.getAttachments();

        if ( mailServer == null )
            throw new RuntimeException("Mail server should be set to send the email!");
        boolean success = false;
        try
        {
            System.out.println("sending email to \"" + receiver + "\" via smtp host \"" + mailServer + "\"...");

            String protocol = "smtp";

            Properties props = new Properties(System.getProperties());
            props.put("mail." + protocol + ".port", String.valueOf(port)); //$NON-NLS-1$
            props.put("mail." + protocol + ".host", mailServer); //$NON-NLS-1$

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
                if ( contactPerson != null && contactPerson.length() > 0 )
                    body = body.replace("${CONTACT_PERSON}", contactPerson);
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
}
