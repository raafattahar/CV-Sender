package views.mail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MailSenderCenterPanel extends JPanel
{
    private static final long serialVersionUID = -2703481415995898595L;
    private JTextField companyValue;
    private JTextField receiverValue;
    private JTextField subjectValue;
    private JTextField contactPersonValue;
    private JTextArea bodyValue;

    public MailSenderCenterPanel()
    {
        super();

        setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWeights = new double[] { 0.0, 1.0 };
        layout.columnWidths = new int[] { 1, 1 };
        layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
        layout.rowHeights = new int[] { 1, 1, 1, 1, 1 };

        setLayout(layout);

        int row = 0;
        int col = 0;

        //
        // Company
        //
        JLabel company = new JLabel("Company:");
        GridBagConstraints companyGbc = new GridBagConstraints();
        companyGbc.fill = GridBagConstraints.BOTH;
        companyGbc.anchor = GridBagConstraints.WEST;
        companyGbc.insets = new Insets(5, 5, 5, 5);
        companyGbc.gridx = col++;
        companyGbc.gridy = row;
        add(company, companyGbc);
        company.setToolTipText("Name of the company to apply for");

        companyValue = new JTextField();
        GridBagConstraints companyValueGbc = new GridBagConstraints();
        companyValueGbc.fill = GridBagConstraints.BOTH;
        companyValueGbc.anchor = GridBagConstraints.CENTER;
        companyValueGbc.insets = new Insets(5, 5, 5, 5);
        companyValueGbc.gridx = col++;
        companyValueGbc.gridy = row;
        add(companyValue, companyValueGbc);
        company.setLabelFor(companyValue);
        companyValue.setToolTipText("Name of the company to apply for");

        row++;
        col = 0;
        //
        // Send to
        //
        JLabel receiver = new JLabel("Receiver:");
        GridBagConstraints receiverGbc = new GridBagConstraints();
        receiverGbc.fill = GridBagConstraints.BOTH;
        receiverGbc.anchor = GridBagConstraints.WEST;
        receiverGbc.insets = new Insets(5, 5, 5, 5);
        receiverGbc.gridx = col++;
        receiverGbc.gridy = row;
        add(receiver, receiverGbc);
        receiver.setToolTipText("The email of the receiver");

        receiverValue = new JTextField();
        GridBagConstraints receiverValueGbc = new GridBagConstraints();
        receiverValueGbc.fill = GridBagConstraints.BOTH;
        receiverValueGbc.anchor = GridBagConstraints.CENTER;
        receiverValueGbc.insets = new Insets(5, 5, 5, 5);
        receiverValueGbc.gridx = col++;
        receiverValueGbc.gridy = row;
        add(receiverValue, receiverValueGbc);
        receiver.setLabelFor(receiverValue);
        receiverValue.setToolTipText("The email of the receiver");

        row++;
        col = 0;
        //
        // Subject
        //
        JLabel subject = new JLabel("Subject:");
        GridBagConstraints subjectGbc = new GridBagConstraints();
        subjectGbc.fill = GridBagConstraints.BOTH;
        subjectGbc.anchor = GridBagConstraints.WEST;
        subjectGbc.insets = new Insets(5, 5, 5, 5);
        subjectGbc.gridx = col++;
        subjectGbc.gridy = row;
        add(subject, subjectGbc);
        subject.setToolTipText("The subject of the email");

        subjectValue = new JTextField();
        GridBagConstraints subjectValueGbc = new GridBagConstraints();
        subjectValueGbc.fill = GridBagConstraints.BOTH;
        subjectValueGbc.anchor = GridBagConstraints.CENTER;
        subjectValueGbc.insets = new Insets(5, 5, 5, 5);
        subjectValueGbc.gridx = col++;
        subjectValueGbc.gridy = row;
        add(subjectValue, subjectValueGbc);
        subject.setLabelFor(subjectValue);
        subjectValue.setToolTipText("The subject of the email");

        row++;
        col = 0;
        //
        // Contact person
        //
        JLabel contactPerson = new JLabel("Contact person:");
        GridBagConstraints contactPersonGbc = new GridBagConstraints();
        contactPersonGbc.fill = GridBagConstraints.BOTH;
        contactPersonGbc.anchor = GridBagConstraints.WEST;
        contactPersonGbc.insets = new Insets(5, 5, 5, 5);
        contactPersonGbc.gridx = col++;
        contactPersonGbc.gridy = row;
        add(contactPerson, contactPersonGbc);
        contactPerson.setToolTipText("The contact person of the email/ that will answer to the application");

        contactPersonValue = new JTextField();
        GridBagConstraints contactPersonValueGbc = new GridBagConstraints();
        contactPersonValueGbc.fill = GridBagConstraints.BOTH;
        contactPersonValueGbc.anchor = GridBagConstraints.CENTER;
        contactPersonValueGbc.insets = new Insets(5, 5, 5, 5);
        contactPersonValueGbc.gridx = col++;
        contactPersonValueGbc.gridy = row;
        add(contactPersonValue, contactPersonValueGbc);
        contactPerson.setLabelFor(contactPersonValue);
        contactPersonValue.setToolTipText("The contact person of the email/ that will answer to the application");

        row++;
        col = 0;
        //
        // Body
        //
        JLabel body = new JLabel("Body:");
        GridBagConstraints bodyGbc = new GridBagConstraints();
        bodyGbc.fill = GridBagConstraints.HORIZONTAL;
        bodyGbc.anchor = GridBagConstraints.NORTHWEST;
        bodyGbc.insets = new Insets(5, 5, 5, 5);
        bodyGbc.gridx = col++;
        bodyGbc.gridy = row;
        add(body, bodyGbc);
        body.setToolTipText("The email that you want to send for the application");

        bodyValue = new JTextArea();
        bodyValue.setWrapStyleWord(true);
        bodyValue.setLineWrap(true);
        bodyValue.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        GridBagConstraints bodyValueGbc = new GridBagConstraints();
        bodyValueGbc.fill = GridBagConstraints.BOTH;
        bodyValueGbc.anchor = GridBagConstraints.CENTER;
        bodyValueGbc.insets = new Insets(5, 5, 5, 5);
        bodyValueGbc.gridx = col++;
        bodyValueGbc.gridy = row;
        add(bodyValue, bodyValueGbc);
        body.setLabelFor(bodyValue);
        bodyValue.setToolTipText("The email that you want to send for the application");
    }

    public String getCompanyValue()
    {
        return companyValue.getText();
    }

    public String getReceiverValue()
    {
        return receiverValue.getText();
    }

    public String getContactPersonValue()
    {
        return contactPersonValue.getText();
    }

    public String getSubjectValue()
    {
        return subjectValue.getText();
    }

    public String getBodyValue()
    {
        return bodyValue.getText();
    }

    public void setBodyValue(String body)
    {
        bodyValue.setText(body);
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.add(new MailSenderCenterPanel());
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setVisible(true);
        frame.setLocation(-1400, -200);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
