package views.menu;

import javax.swing.JMenuBar;

import model.PropertiesModelCache;
import views.menu.item.LoginInfoMenu;
import views.menu.item.MailServerMenu;
import views.menu.item.MailTemplateMenu;

public class MailSenderMenuBar extends JMenuBar
{
    private LoginInfoMenu loginInfoMenu;
    private MailServerMenu mailServerMenu;

    public MailSenderMenuBar(PropertiesModelCache modelCache)
    {
        super();
        add(loginInfoMenu = new LoginInfoMenu(modelCache));
        add(mailServerMenu = new MailServerMenu(modelCache));
        add(new MailTemplateMenu());
    }

    public void refresh()
    {
        loginInfoMenu.refresh();
        mailServerMenu.refresh();
    }
}