package views;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import model.mail.MailServer;
import views.mail.MailSenderView;

public class MainView extends JFrame
{
    public static void main(String[] args)
    {
        new MainView();
    }

    public MainView()
    {
        String classname = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; //$NON-NLS-1$
        try
        {
            LookAndFeel currentLaF = UIManager.getLookAndFeel();
            if ( currentLaF == null || !classname.equals(currentLaF.getClass().getName()) )
            {
                UIManager.setLookAndFeel(classname);
            }
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }

        MailSenderView view = MailSenderView.getInstance();
        if ( !view.getModelCache().getCredential().isValid() )
            view.getAuthentificationPanel().updateProperties();

        if ( !view.getModelCache().getMailServer().isValid() )
        {
            view.getModelCache().getMailServer().setSmtp(MailServer.getGmailServer().getSmtp());
            view.getModelCache().getMailServer().setPort(MailServer.getGmailServer().getPort());
            view.getMailSenderMenuBar().refresh();
        }
    }
}
