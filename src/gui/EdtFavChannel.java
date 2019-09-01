package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class EdtFavChannel extends JDialog implements ActionListener {
    private int modalResult = 0;
    private JTextField jtfName;
    private JTextField jtfIcon;
    private JTextField jtfCorrection;

    public EdtFavChannel(Dialog owner) {
        super(owner);
        initGUI();
    }
    
    private void initGUI() {
    	setTitle(Messages.getString("StrTitleEdtChannels"));
		GridBagConstraints gridBagConstraints;
		JPanel jpnClient = new JPanel();
        GridBagLayout gblClientLayout = new GridBagLayout();
        gblClientLayout.columnWidths = new int[] {91, 100};
        jpnClient.setLayout(gblClientLayout);

        JLabel jlbName = new JLabel();
        jlbName.setHorizontalAlignment(SwingConstants.RIGHT);
        jlbName.setLabelFor(jtfName);
        jlbName.setText(Messages.getString("StrLbName")); //$NON-NLS-1$
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        jpnClient.add(jlbName, gridBagConstraints);

        jtfName = new JTextField();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 0, 4);
        jpnClient.add(jtfName, gridBagConstraints);

        JLabel jlbCorrection = new JLabel();
        jlbCorrection.setHorizontalAlignment(SwingConstants.RIGHT);
        jlbCorrection.setLabelFor(jtfCorrection);
        jlbCorrection.setText(Messages.getString("StrLbCorrection"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        jpnClient.add(jlbCorrection, gridBagConstraints);

        jtfCorrection = new JTextField();
        jtfCorrection.setText("0");
        jtfCorrection.setHorizontalAlignment(JTextField.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 0, 4);
        jpnClient.add(jtfCorrection, gridBagConstraints);

        JLabel jlbIcon = new JLabel();
        jlbIcon.setHorizontalAlignment(SwingConstants.RIGHT);
        jlbIcon.setLabelFor(jtfIcon);
        jlbIcon.setText(Messages.getString("StrLbIcon"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        jpnClient.add(jlbIcon, gridBagConstraints);

        jtfIcon = new JTextField();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 0, 4);
        jpnClient.add(jtfIcon, gridBagConstraints);

        getContentPane().add(jpnClient, BorderLayout.CENTER);

        JPanel jpnButtons = new JPanel();
        jpnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        JButton jbtOK = new JButton();
        jbtOK.setText(Messages.getString("StrBtExecute"));
        jbtOK.setActionCommand("cmd_OK");
        jbtOK.addActionListener(this);
        jpnButtons.add(jbtOK);

        JButton jbtCancel = new JButton();
        jbtCancel.setText(Messages.getString("StrBtCancel"));
        jbtCancel.setActionCommand("cmd_Cancel");
        jbtCancel.addActionListener(this);
        jpnButtons.add(jbtCancel);

        getContentPane().add(jpnButtons, BorderLayout.PAGE_END);

        setSize(new Dimension(360, 140));
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(jbtOK);
	}

    public int getModalResult() {
        return modalResult;
    }

    public String getCorrection() {
        return jtfCorrection.getText();
    }

    public void setCorrection(String correction) {
        this.jtfCorrection.setText(correction);
    }

    public String getCName() {
        return jtfName.getText();
    }

    public void setCName(String name) {
        this.jtfName.setText(name);
    }

    public String getIcon() {
        return jtfIcon.getText();
    }

    public void setIcon(String icon) {
        this.jtfIcon.setText(icon);
    }

    private void onOK() {
        modalResult = 1;
        setVisible(false);
        dispose();
    }

    private void onCancel() {
        modalResult = 0;
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