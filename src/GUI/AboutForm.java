package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Common.Common.getImagesPatch;
import static Common.Strings.*;

@SuppressWarnings("serial")
public class AboutForm extends JDialog implements ActionListener {

    private int ModalResult = 0;

    private JPanel pnClient;
    private JLabel lbImage;
    private JLabel lbName;
    private JLabel lbAuthor;
    private JPanel pnButtons;
    private JButton btOk;

    public AboutForm(Frame owner) {
        super(owner);
        initComponents();
    }

    public int getModalResult() {
        return ModalResult;
    }

    private void initComponents() {
        setTitle(StrActionAbout);

        GridBagConstraints gridBagConstraints;
        pnClient = new JPanel();
        pnClient.setName("pnClient");
        pnClient.setLayout(new GridBagLayout());

        lbImage = new JLabel();
        lbImage.setIcon(new ImageIcon(getImagesPatch() + "main.png"));
        lbImage.setName("lbImage");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        pnClient.add(lbImage, gridBagConstraints);

        lbName = new javax.swing.JLabel();
        lbName.setText(StrTitleMain);
        lbName.setName("lbName");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        pnClient.add(lbName, gridBagConstraints);

        lbAuthor = new JLabel();
        lbAuthor.setText("Воронов Николай");
        lbAuthor.setName("lbAuthor");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        pnClient.add(lbAuthor, gridBagConstraints);

        add(pnClient, BorderLayout.CENTER);

        pnButtons = new JPanel();
        pnButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

        btOk = new JButton();
        btOk.setText(StrActionClose);
        btOk.setName("btOk");
        btOk.setActionCommand("cmd_OK");
        pnButtons.add(btOk);
        btOk.addActionListener(this);

        add(pnButtons, BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(263, 281));
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(btOk);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cmd_OK")) {
            onOK();
        }
    }

    private void onOK() {
        ModalResult = 1;
        setVisible(false);
        dispose();
    }
}