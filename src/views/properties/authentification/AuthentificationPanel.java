package views.properties.authentification;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.PropertiesModelCache;
import model.authentification.Credential;
import views.properties.PropertiesView;

public class AuthentificationPanel extends PropertiesView<Credential>
{
    private static final long serialVersionUID = 8452002604697744558L;

    private JTextField emailValue;
    private JPasswordField passwordValue;
    private JCheckBox saveCredential;

    public AuthentificationPanel(PropertiesModelCache modelCache)
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
        // email
        //
        JLabel email = new JLabel("Email:");
        GridBagConstraints emailGbc = new GridBagConstraints();
        emailGbc.fill = GridBagConstraints.HORIZONTAL;
        emailGbc.anchor = GridBagConstraints.WEST;
        emailGbc.insets = new Insets(5, 5, 5, 5);
        emailGbc.gridx = col++;
        emailGbc.gridy = row;
        add(email, emailGbc);
        email.setToolTipText("Email of the sender");

        emailValue = new JTextField(modelCache.getCredential().getLogin());
        GridBagConstraints emailValueGbc = new GridBagConstraints();
        emailValueGbc.fill = GridBagConstraints.HORIZONTAL;
        emailValueGbc.anchor = GridBagConstraints.CENTER;
        emailValueGbc.insets = new Insets(5, 5, 5, 5);
        emailValueGbc.gridx = col++;
        emailValueGbc.gridy = row;
        add(emailValue, emailValueGbc);
        email.setLabelFor(emailValue);
        emailValue.setToolTipText("Email of the sender");

        row++;
        col = 0;
        //
        // password
        //
        JLabel password = new JLabel("Password:");
        GridBagConstraints passwordGbc = new GridBagConstraints();
        passwordGbc.fill = GridBagConstraints.HORIZONTAL;
        passwordGbc.anchor = GridBagConstraints.WEST;
        passwordGbc.insets = new Insets(5, 5, 5, 5);
        passwordGbc.gridx = col++;
        passwordGbc.gridy = row;
        add(password, passwordGbc);
        password.setToolTipText("Password of the email");

        passwordValue = new JPasswordField(modelCache.getCredential().getPassword());
        GridBagConstraints passwordValueGbc = new GridBagConstraints();
        passwordValueGbc.fill = GridBagConstraints.HORIZONTAL;
        passwordValueGbc.anchor = GridBagConstraints.CENTER;
        passwordValueGbc.insets = new Insets(5, 5, 5, 5);
        passwordValueGbc.gridx = col++;
        passwordValueGbc.gridy = row;
        add(passwordValue, passwordValueGbc);
        password.setLabelFor(passwordValue);
        passwordValue.setToolTipText("Password of the email");

        row++;
        col = 0;
        //
        // save
        //
        saveCredential = new JCheckBox("Save");
        GridBagConstraints saveCredentialGbc = new GridBagConstraints();
        saveCredentialGbc.fill = GridBagConstraints.BOTH;
        saveCredentialGbc.anchor = GridBagConstraints.WEST;
        saveCredentialGbc.insets = new Insets(5, 5, 5, 5);
        saveCredentialGbc.gridx = col++;
        saveCredentialGbc.gridy = row;
        saveCredentialGbc.gridwidth = 2;
        add(saveCredential, saveCredentialGbc);
    }

    public Credential getCredential()
    {
        return modelCache.getCredential();
    }

    public void setCredential(Credential credential)
    {
        setCredential(credential.getLogin(), credential.getPassword());
    }

    public void setCredential(String email, String password)
    {
        this.emailValue.setText(email);
        this.passwordValue.setText(password);
        modelCache.getCredential().setLogin(email);
        modelCache.getCredential().setPassword(password);
    }

    @Override
    public boolean shouldSave()
    {
        return saveCredential.isSelected();
    }

    @Override
    protected String getDialogText()
    {
        return "Login";
    }

    @Override
    protected String getDialogIcon()
    {
        return "ressources/user.png";
    }

    @Override
    protected Credential updateModelCache()
    {
        modelCache.getCredential().setLogin(emailValue.getText());
        modelCache.getCredential().setPassword(new String(passwordValue.getPassword()));
        return modelCache.getCredential();
    }

    @Override
    protected Credential getObject()
    {
        return modelCache.getCredential();
    }
}
