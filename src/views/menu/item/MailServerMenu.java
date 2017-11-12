package views.menu.item;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import model.PropertiesModelCache;
import model.mail.MailServer;
import model.mail.MailServer.SMTP;
import views.properties.mail.MailServerPanel;

public class MailServerMenu extends JMenu implements ActionListener
{
    private final static String EMPTY_SERVER = "Empty server";
    private final String SERVER = "Server: ";

    private final String SET_SERVER = "Set server";
    private final String CHANGE_SERVER = "Change server";

    private JMenuItem server;

    private PropertiesModelCache modelCache;

    public MailServerMenu(PropertiesModelCache modelCache)
    {
        super(EMPTY_SERVER);
        this.modelCache = modelCache;

        ImageIcon icon = new ImageIcon("ressources/gmail.png");
        // resize the icon to be 16x16
        BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bi.getGraphics();
        graphics.drawImage(icon.getImage(), 0, 0, 16, 16, icon.getImageObserver());

        server = new JMenuItem(SET_SERVER, new ImageIcon(bi));
        server.addActionListener(this);
        add(server);

        addSeparator();

        String modelCacheSmtp = modelCache.getMailServer().getSmtp();

        SMTP[] values = SMTP.values();
        for ( SMTP smtp : values )
        {
            JMenuItem item = new JCheckBoxMenuItem(smtp.name());
            item.addActionListener(this);
            if ( smtp.getSmtp().equals(modelCacheSmtp) )
                item.setSelected(true);
            else if ( modelCacheSmtp == null && smtp == SMTP.getDefaultSmtp() )// default 
                item.setSelected(true);
            add(item);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ( e.getSource() == server )
        {
            MailServerPanel mailServerPanel = new MailServerPanel(modelCache);
            mailServerPanel.updateProperties();
        }
        else
        {
            try
            {
                Object source = e.getSource();
                if ( source instanceof JMenuItem )
                {
                    String serverName = ((JMenuItem)source).getText();
                    SMTP smtp = SMTP.valueOf(serverName);
                    modelCache.getMailServer().setSmtp(smtp.getSmtp());
                    modelCache.getMailServer().setPort(587);
                    for ( int i = 0; i < getItemCount(); i++ )
                    {
                        JMenuItem item = getItem(i);
                        if ( item instanceof JCheckBoxMenuItem && item != source )
                            item.setSelected(false);
                    }
                    ((JMenuItem)source).setSelected(true);
                }
            }
            catch ( Exception ex )
            {
                //
            }
        }
        refresh();
    }

    public void refresh()
    {
        MailServer mailServer = modelCache.getMailServer();
        if ( mailServer == null || !mailServer.isValid() )
        {
            setText(EMPTY_SERVER);
            server.setText(SET_SERVER);
        }
        else
        {
            setText(SERVER + mailServer.getSmtp() + ":" + mailServer.getPort());
            server.setText(CHANGE_SERVER);
        }
        revalidate();
        repaint();
    }
}