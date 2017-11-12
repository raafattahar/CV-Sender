package views.properties.mail;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.PropertiesModelCache;
import model.mail.MailServer;
import views.properties.PropertiesView;

public class MailServerPanel extends PropertiesView<MailServer>
{
    private JTextField smtpValue;
    private JTextField portValue;
    private JCheckBox saveMailServer;

    public MailServerPanel(PropertiesModelCache modelCache)
    {
        super(modelCache);

        GridBagLayout layout = new GridBagLayout();
        layout.columnWeights = new double[] { 0.0, 1.0 };
        layout.columnWidths = new int[] { 20, 150 };
        layout.rowWeights = new double[] { 1.0, 1.0, 1.0 };
        layout.rowHeights = new int[] { 25, 25, 25 };

        setLayout(layout);

        int row = 0;
        int col = 0;
        //
        // SMTP
        //
        JLabel smtp = new JLabel("Mail server:");
        GridBagConstraints smtpGbc = new GridBagConstraints();
        smtpGbc.fill = GridBagConstraints.HORIZONTAL;
        smtpGbc.anchor = GridBagConstraints.WEST;
        smtpGbc.insets = new Insets(5, 5, 5, 5);
        smtpGbc.gridx = col++;
        smtpGbc.gridy = row;
        add(smtp, smtpGbc);
        smtp.setToolTipText("Mail server that the sender will use");

        smtpValue = new JTextField(modelCache.getMailServer().getSmtp());
        GridBagConstraints smtpValueGbc = new GridBagConstraints();
        smtpValueGbc.fill = GridBagConstraints.HORIZONTAL;
        smtpValueGbc.anchor = GridBagConstraints.CENTER;
        smtpValueGbc.insets = new Insets(5, 5, 5, 5);
        smtpValueGbc.gridx = col++;
        smtpValueGbc.gridy = row;
        add(smtpValue, smtpValueGbc);
        smtp.setLabelFor(smtpValue);
        smtpValue.setToolTipText("Mail server that the sender will use");

        row++;
        col = 0;
        //
        // Port
        //
        JLabel portlbl = new JLabel("Port:");
        GridBagConstraints portGbc = new GridBagConstraints();
        portGbc.fill = GridBagConstraints.HORIZONTAL;
        portGbc.anchor = GridBagConstraints.WEST;
        portGbc.insets = new Insets(5, 5, 5, 5);
        portGbc.gridx = col++;
        portGbc.gridy = row;
        add(portlbl, portGbc);
        portlbl.setToolTipText("Port that the mail server will use");

        String port = modelCache.getMailServer().getPort() < 0 ? "" : "" + modelCache.getMailServer().getPort();
        portValue = new JTextField("" + port);
        GridBagConstraints portValueGbc = new GridBagConstraints();
        portValueGbc.fill = GridBagConstraints.HORIZONTAL;
        portValueGbc.anchor = GridBagConstraints.CENTER;
        portValueGbc.insets = new Insets(5, 5, 5, 5);
        portValueGbc.gridx = col++;
        portValueGbc.gridy = row;
        add(portValue, portValueGbc);
        portlbl.setLabelFor(portValue);
        portValue.setToolTipText("Port that the mail server will use");

        row++;
        col = 0;
        //
        // save
        //
        saveMailServer = new JCheckBox("Save");
        GridBagConstraints saveMailServerGbc = new GridBagConstraints();
        saveMailServerGbc.fill = GridBagConstraints.BOTH;
        saveMailServerGbc.anchor = GridBagConstraints.WEST;
        saveMailServerGbc.insets = new Insets(5, 5, 5, 5);
        saveMailServerGbc.gridx = col++;
        saveMailServerGbc.gridy = row;
        saveMailServerGbc.gridwidth = 2;
        add(saveMailServer, saveMailServerGbc);
    }

    public MailServer getMailServer()
    {
        return modelCache.getMailServer();
    }

    public void setMailServer(MailServer mailServer)
    {
        setMailServer(mailServer.getSmtp(), mailServer.getPort());
    }

    public void setMailServer(String smtp, int port)
    {
        this.smtpValue.setText(smtp);
        this.portValue.setText("" + port);
        modelCache.getMailServer().setSmtp(smtp);
        modelCache.getMailServer().setPort(port);
    }

    @Override
    protected MailServer updateModelCache()
    {
        modelCache.getMailServer().setSmtp(smtpValue.getText());
        modelCache.getMailServer().setPort(portValue.getText());
        return modelCache.getMailServer();
    }

    @Override
    public boolean shouldSave()
    {
        return saveMailServer.isSelected();
    }

    @Override
    protected String getDialogText()
    {
        return "Mail Server";
    }

    @Override
    protected String getDialogIcon()
    {
        return "ressources/gmail.png";
    }

    @Override
    protected MailServer getObject()
    {
        return modelCache.getMailServer();
    }
}
