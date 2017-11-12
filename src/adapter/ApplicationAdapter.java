package adapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import model.mail.Mail;
import views.mail.MailSenderView;

public class ApplicationAdapter
{
    public static final String APPLICATIONS_FILES = "applications.csv";
    public static final String TEMPLATE_FILE = "template.txt";

    public static final String ATTACHMENTS = "attachments:";
    public static final String BODY = "body:";

    public static void saveApplication(Mail mailSender)
    {
        boolean exist = new File(APPLICATIONS_FILES).exists();

        try (BufferedWriter output = new BufferedWriter(new FileWriter(APPLICATIONS_FILES, true)))
        {
            if ( exist )
                output.newLine();
            output.write(mailSender.toString());
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void saveTemplate(String body, File[] attachments)
    {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(TEMPLATE_FILE, false)))
        {
            if ( attachments != null && attachments.length > 0 )
            {
                output.write(ATTACHMENTS);
                output.newLine();
                boolean first = true;
                for ( File file : attachments )
                {
                    if ( !first )
                        output.write(";");
                    output.write(file.getAbsolutePath());
                    first = false;
                }
                output.newLine();
            }
            output.write(BODY);
            output.newLine();
            output.write(body);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void loadTemplate() throws IOException
    {
        File file = new File(TEMPLATE_FILE);
        if ( !file.exists() )
            throw new IOException("Template file does not exist!");
        List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1);
        if ( allLines != null && allLines.size() > 0 )
        {
            MailSenderView mailSenderView = MailSenderView.getInstance();
            int line = 0;
            if ( allLines.get(line).toLowerCase().startsWith(ATTACHMENTS) && allLines.size() > 1 )
            {
                line++;
                String attachments = allLines.get(line);
                if ( attachments != null && !attachments.isEmpty() )
                {
                    String[] attachmentsList = attachments.split(";");
                    File[] files = new File[attachmentsList.length];
                    for ( int i = 0; i < attachmentsList.length; i++ )
                        files[i] = new File(attachmentsList[i]);
                    mailSenderView.attachFiles(files);
                    line++;
                }
            }

            if ( allLines.size() > line && allLines.get(line).toLowerCase().startsWith(BODY) )
            {
                line++;
                StringBuilder body = new StringBuilder();
                for ( int i = line; i < allLines.size(); i++ )
                {
                    if ( body.length() > 0 )
                        body.append("\n");
                    body.append(allLines.get(i));
                }
                if ( body.length() > 0 )
                    mailSenderView.getMailSenderCenterPanel().setBodyValue(body.toString());
            }
        }
    }
}
