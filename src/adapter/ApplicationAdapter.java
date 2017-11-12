package adapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.mail.MailSender;

public class ApplicationAdapter
{
    public static final String APPLICATIONS_FILES = "applications.csv";
    public static final String TEMPLATE_FILE = "template.txt";

    public static void saveApplication(MailSender mailSender, String company, String contactPerson)
    {
        boolean exist = new File(APPLICATIONS_FILES).exists();

        try (BufferedWriter output = new BufferedWriter(new FileWriter(APPLICATIONS_FILES, true)))
        {
            if ( exist )
                output.newLine();
            output.write(mailSender.saveMail(company, contactPerson));
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
                output.write("Attachments: ");
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
            output.write(body);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
}
