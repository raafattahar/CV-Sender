package views.menu.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import adapter.ApplicationAdapter;
import views.mail.MailSenderView;

public class MailTemplateMenu extends JMenu implements ActionListener
{
    private JMenuItem load;
    private JMenuItem save;

    public MailTemplateMenu()
    {
        super("Template");

        load = new JMenuItem("Load");
        load.addActionListener(this);

        save = new JMenuItem("Save");
        save.addActionListener(this);

        add(load);
        add(save);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        MailSenderView view = MailSenderView.getInstance();
        if ( e.getSource() == load )
        {
            try
            {
                ApplicationAdapter.loadTemplate();
            }
            catch ( IOException ex )
            {
                ex.printStackTrace();
                JOptionPane.showConfirmDialog(view, ex.getMessage(), "Error", JOptionPane.OK_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if ( e.getSource() == save )
        {
            ApplicationAdapter.saveTemplate(view.getMailSenderCenterPanel().getBodyValue(), view.getAttachments());
        }
    }
}
