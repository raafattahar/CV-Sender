package views.properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.PropertiesModelCache;
import views.SwingHelper;
import views.mail.MailSenderView;

public abstract class PropertiesView<T> extends JPanel
{
    protected PropertiesModelCache modelCache;

    public PropertiesView(PropertiesModelCache modelCache)
    {
        super();
        this.modelCache = modelCache;
    }

    public T updateProperties()
    {
        T obj = getObject();

        int result = JOptionPane.showConfirmDialog(MailSenderView.getInstance(), this, getDialogText(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(getDialogIcon()));
        if ( result == JOptionPane.OK_OPTION )
        {
            try
            {
                obj = updateModelCache();
                if ( shouldSave() )
                    SwingHelper.save(modelCache);
            }
            catch ( NumberFormatException e )
            {
                // Most likely that the port could not be parsed
                // Should show a warning and ask the user to 
                // full fill again
                JOptionPane.showMessageDialog(MailSenderView.getInstance(), e.getMessage(), "Error",
                        JOptionPane.QUESTION_MESSAGE, new ImageIcon(getDialogIcon()));
            }
        }

        return obj;
    }

    protected abstract String getDialogText();

    protected abstract String getDialogIcon();

    protected abstract T getObject();

    protected abstract T updateModelCache();

    protected abstract boolean shouldSave();
}
