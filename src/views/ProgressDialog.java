package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import views.mail.MailSenderView;

public abstract class ProgressDialog implements Runnable
{
    private JProgressBar progressBar;
    private JDialog dialog;

    public ProgressDialog(String text)
    {
        initProgressBar(text);
    }

    @Override
    abstract public void run();

    public void startJob()
    {
        final SwingWorker<Void, Void> myWorker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                try
                {
                    ProgressDialog.this.run();
                }
                finally
                {
                    closeProgressBar();
                }
                return null;
            }
        };
        myWorker.execute();
        dialog.setVisible(true);
    }

    public void setProgressValue(double d)
    {
        progressBar.setValue((int)(progressBar.getMaximum() * d));
    }

    private void initProgressBar(String text)
    {
        progressBar = new JProgressBar(0, 10);
        progressBar.setStringPainted(true);
        progressBar.setValue(progressBar.getMinimum());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridheight = 25;
        panel.add(progressBar, gbc);

        dialog = new JDialog(MailSenderView.getInstance(), text, true);
        dialog.setPreferredSize(new Dimension(180, 80));
        dialog.setLocationRelativeTo(MailSenderView.getInstance());
        dialog.add(panel);
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void closeProgressBar()
    {
        if ( !SwingUtilities.isEventDispatchThread() )
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    closeProgressBar();
                }
            });
        }
        else
        {
            progressBar.setValue(progressBar.getMaximum());
            dialog.dispose();
        }
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("test progress bar");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel("test"), BorderLayout.WEST);
        TextArea comp = new TextArea();
        comp.setColumns(20);
        panel.add(comp, BorderLayout.CENTER);
        JButton showProgressBar = new JButton("show progress bar");
        showProgressBar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ProgressDialog pd = new ProgressDialog("test")
                {
                    @Override
                    public void run()
                    {
                        setProgressValue(0.2);

                        setProgressValue(0.8);

                        closeProgressBar();
                    }
                };
                pd.startJob();
            }
        });
        panel.add(showProgressBar, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
