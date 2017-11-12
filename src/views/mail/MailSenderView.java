package views.mail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import adapter.ApplicationAdapter;
import adapter.MailHelper;
import adapter.PropertiesHelper;
import model.PropertiesModelCache;
import model.mail.Mail;
import views.ProgressDialog;
import views.menu.MailSenderMenuBar;
import views.properties.authentification.AuthentificationPanel;

public class MailSenderView extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 6523879595478612001L;

    private MailSenderMenuBar mailSenderMenuBar;
    private MailSenderCenterPanel mailSenderCenterPanel;
    private AuthentificationPanel authentificationPanel;
    private JButton sendButton;
    private JButton attachFiles;
    private JLabel attachedFilesList;
    private File[] attachedFiles;

    private PropertiesModelCache modelCache;

    public static MailSenderView INSTANCE = null;

    public static MailSenderView getInstance()
    {
        if ( INSTANCE == null )
            INSTANCE = new MailSenderView();
        return INSTANCE;
    }

    private MailSenderView()
    {
        super("Applying for jobs");
        this.modelCache = new PropertiesModelCache(PropertiesHelper.getOrCreateCredentialFromProperties(),
                PropertiesHelper.getOrCreateMailServerFromProperties());

        JPanel view = new JPanel();

        view.setLayout(new BorderLayout());

        //
        // toolbar
        //
        mailSenderMenuBar = new MailSenderMenuBar(modelCache);
        setJMenuBar(mailSenderMenuBar);

        //
        // center panel / email corp
        //
        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
        scrollPanel.setPreferredSize(new Dimension(800, 600));
        scrollPanel.getVerticalScrollBar().setUnitIncrement(20);
        mailSenderCenterPanel = new MailSenderCenterPanel();
        scrollPanel.setViewportView(mailSenderCenterPanel);
        view.add(scrollPanel, BorderLayout.CENTER);

        sendButton = new JButton("send");
        sendButton.addActionListener(this);
        sendButton.setAlignmentX(SwingConstants.EAST);

        attachFiles = new JButton("attach files");
        attachFiles.addActionListener(this);
        attachFiles.setAlignmentX(SwingConstants.WEST);

        attachedFilesList = new JLabel("");

        //
        // button bar
        //
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        buttonPanel.add(attachFiles, BorderLayout.WEST);
        buttonPanel.add(attachedFilesList, BorderLayout.CENTER);
        buttonPanel.add(sendButton, BorderLayout.EAST);

        view.add(buttonPanel, BorderLayout.SOUTH);

        add(view);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(800, 600));
        setVisible(true);
        pack();

        authentificationPanel = new AuthentificationPanel(modelCache);
        mailSenderMenuBar.refresh();
    }

    public PropertiesModelCache getModelCache()
    {
        return modelCache;
    }

    public MailSenderMenuBar getMailSenderMenuBar()
    {
        return mailSenderMenuBar;
    }

    public AuthentificationPanel getAuthentificationPanel()
    {
        return authentificationPanel;
    }

    public MailSenderCenterPanel getMailSenderCenterPanel()
    {
        return mailSenderCenterPanel;
    }

    public File[] getAttachments()
    {
        return attachedFiles;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ( e.getSource() == sendButton )
            sendEmail();
        else if ( e.getSource() == attachFiles )
            attachFiles();
    }

    private void attachFiles()
    {
        FileDialog fd = new FileDialog(this, "Attach files", FileDialog.LOAD);
        fd.setMultipleMode(true);
        fd.setVisible(true);
        File[] files = fd.getFiles();
        attachFiles(files);
    }

    public void attachFiles(File[] files)
    {
        attachedFiles = files;
        StringBuilder sb = new StringBuilder();
        for ( File file : attachedFiles )
        {
            if ( sb.length() > 0 )
                sb.append(";");
            sb.append(file.getName());
        }
        attachedFilesList.setText(sb.toString());
    }

    private void sendEmail()
    {
        if ( !modelCache.getCredential().isValid() )
            authentificationPanel.updateProperties();

        ProgressDialog progressDialog = new ProgressDialog("Sending email")
        {
            @Override
            public void run()
            {
                boolean success = false;
                try
                {
                    ApplicationAdapter.saveTemplate(mailSenderCenterPanel.getBodyValue(), attachedFiles);
                    setProgressValue(0.2);
                    Mail mailSender = null;
                    try
                    {
                        mailSender = new Mail(modelCache.getCredential(), modelCache.getMailServer());
                        mailSender.setCompany(mailSenderCenterPanel.getCompanyValue())//
                                .setReceiver(mailSenderCenterPanel.getReceiverValue())//
                                .setSubject(mailSenderCenterPanel.getSubjectValue())//
                                .setContactPerson(mailSenderCenterPanel.getContactPersonValue())//
                                .setBody(mailSenderCenterPanel.getBodyValue());
                        if ( attachedFiles != null )
                            mailSender.setAttachements(Arrays.asList(attachedFiles));
                    }
                    catch ( NullPointerException ex )
                    {
                        JOptionPane.showMessageDialog(MailSenderView.this, "Fail to send E-mail: " + ex.getMessage());
                        throw ex;
                    }
                    setProgressValue(0.4);
                    try
                    {
                        success = MailHelper.send(mailSender);
                        setProgressValue(0.6);
                    }
                    catch ( MessagingException e1 )
                    {
                        JOptionPane.showMessageDialog(MailSenderView.this, "Fail to send E-mail: " + e1.getMessage());
                        throw e1;
                    }
                    if ( success )
                    {
                        ApplicationAdapter.saveApplication(mailSender);
                        setProgressValue(0.8);
                    }
                    setProgressValue(1);
                }
                catch ( Exception ex )
                {
                    if ( !success )
                        JOptionPane.showMessageDialog(MailSenderView.this, "Fail to send E-mail: " + ex.getMessage());
                    ex.printStackTrace();
                }
                if ( success )
                    JOptionPane.showMessageDialog(MailSenderView.this, "Email was send");
            }
        };
        progressDialog.startJob();
    }
}
