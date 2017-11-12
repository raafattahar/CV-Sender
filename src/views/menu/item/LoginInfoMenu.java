package views.menu.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import model.PropertiesModelCache;
import model.authentification.Credential;
import views.SwingHelper;
import views.properties.authentification.AuthentificationPanel;

public class LoginInfoMenu extends JMenu implements ActionListener
{
    private final static String NOT_LOGGED = "Not Logged";
    private final String LOGGED_AS = "Logged as: ";
    private final String LOGIN = "Login";
    private final String CHANGE_LOGIN = "Change login";

    private JMenuItem login;
    private JMenuItem deleteLogin;

    private PropertiesModelCache modelCache;

    public LoginInfoMenu(PropertiesModelCache modelCache)
    {
        super(NOT_LOGGED);
        this.modelCache = modelCache;

        login = new JMenuItem(LOGIN, new ImageIcon("ressources/user.png"));
        login.addActionListener(this);

        deleteLogin = new JMenuItem("Delete properties", new ImageIcon("ressources/delete.png"));
        deleteLogin.addActionListener(this);

        add(login);
        add(deleteLogin);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ( e.getSource() == login )
        {
            AuthentificationPanel authentificationPanel = new AuthentificationPanel(modelCache);
            authentificationPanel.updateProperties();
        }
        else if ( e.getSource() == deleteLogin )
        {
            SwingHelper.deleteLogin();
            modelCache.getCredential().reset();
        }
        refresh();
    }

    public void refresh()
    {
        Credential credential = modelCache.getCredential();
        if ( credential == null || !credential.isValid() )
        {
            setText(NOT_LOGGED);
            login.setText(LOGIN);
        }
        else
        {
            setText(LOGGED_AS + credential.getLogin());
            login.setText(CHANGE_LOGIN);
        }
        revalidate();
        repaint();
    }
}