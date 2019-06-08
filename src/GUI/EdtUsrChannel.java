package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static Common.Strings.*;

@SuppressWarnings("serial")
public class EdtUsrChannel extends JDialog implements ActionListener {
    private int ModalResult = 0;
    private JPanel pnClient;
    private JLabel lbName;
    private JTextField tfName;
    private JLabel lbIcon;
    private JTextField tfIcon;
    private JLabel lbCorrection;
    private JTextField tfCorrection;
    private JPanel pnButtons;
    private JButton btOK;
    private JButton btCancel;

    public EdtUsrChannel(Dialog owner) {
        super(owner, StrTitleEdtChannels);

        GridBagConstraints gridBagConstraints;
        pnClient = new JPanel();
        GridBagLayout pnClientLayout = new GridBagLayout();
        pnClientLayout.columnWidths = new int[] {91, 100};
        pnClient.setLayout(pnClientLayout);

        lbName = new JLabel();
        lbName.setHorizontalAlignment(SwingConstants.RIGHT);
        lbName.setLabelFor(tfName);
        lbName.setText(StrlbName);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        pnClient.add(lbName, gridBagConstraints);

        tfName = new JTextField();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 0, 4);
        pnClient.add(tfName, gridBagConstraints);

        lbCorrection = new JLabel();
        lbCorrection.setHorizontalAlignment(SwingConstants.RIGHT);
        lbCorrection.setLabelFor(tfCorrection);
        lbCorrection.setText(StrlbCorrection);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        pnClient.add(lbCorrection, gridBagConstraints);

        tfCorrection = new JTextField();
        tfCorrection.setText("0");
        tfCorrection.setHorizontalAlignment(JTextField.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 0, 4);
        pnClient.add(tfCorrection, gridBagConstraints);

        lbIcon = new JLabel();
        lbIcon.setHorizontalAlignment(SwingConstants.RIGHT);
        lbIcon.setLabelFor(tfIcon);
        lbIcon.setText(StrlbIcon);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        pnClient.add(lbIcon, gridBagConstraints);

        tfIcon = new JTextField();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 0, 4);
        pnClient.add(tfIcon, gridBagConstraints);

        getContentPane().add(pnClient, BorderLayout.CENTER);

        pnButtons = new javax.swing.JPanel();
        pnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        btOK = new JButton();
        btOK.setText(StrBtExecute);
        btOK.setActionCommand("cmd_OK");
        btOK.addActionListener(this);
        pnButtons.add(btOK);

        btCancel = new JButton();
        btCancel.setText(StrBtCancel);
        btCancel.setActionCommand("cmd_Cancel");
        btCancel.addActionListener(this);
        pnButtons.add(btCancel);

        getContentPane().add(pnButtons, BorderLayout.PAGE_END);

        setSize(new Dimension(360, 140));
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(btOK);
    }

    public int getModalResult() {
        return ModalResult;
    }

    public String getCorrection() {
        return tfCorrection.getText();
    }

    public void setCorrection(String Correction) {
        this.tfCorrection.setText(Correction);
    }

    public String getName() {
        return tfName.getText();
    }

    public void setName(String Name) {
        this.tfName.setText(Name);
    }

    public String getIcon() {
        return tfIcon.getText();
    }

    public void setIcon(String Icon) {
        this.tfIcon.setText(Icon);
    }

    private void onOK() {
        ModalResult = 1;
        setVisible(false);
        dispose();
    }

    private void onCancel() {
        ModalResult = 0;
        setVisible(false);
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cmd_OK")) {
            onOK();
        } else if (e.getActionCommand().equals("cmd_Cancel")) {
            onCancel();
        }
    }

}