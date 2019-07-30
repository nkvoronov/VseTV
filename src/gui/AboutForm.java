package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class AboutForm extends JDialog implements ActionListener {
    private int modalResult = 0;

    public AboutForm(Frame owner) {
        super(owner);
        initGUI();
    }

    public int getModalResult() {
        return modalResult;
    }

    private void initGUI() {
        setTitle(Messages.getString("StrActionAbout"));

        GridBagConstraints gridBagConstraints;
        JPanel jpnClient = new JPanel();
        jpnClient.setName("pnClient");
        jpnClient.setLayout(new GridBagLayout());

        JLabel jlbImage = new JLabel();
        jlbImage.setIcon(new ImageIcon(AboutForm.class.getResource("/resources/main.png")));
        jlbImage.setName("lbImage");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        jpnClient.add(jlbImage, gridBagConstraints);

        JLabel jlbName = new JLabel();
        jlbName.setText(Messages.getString("StrTitleMain"));
        jlbName.setName("lbName");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jpnClient.add(jlbName, gridBagConstraints);

        JLabel jlbAuthor = new JLabel();
        jlbAuthor.setText("Воронов Николай");
        jlbAuthor.setName("lbAuthor");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jpnClient.add(jlbAuthor, gridBagConstraints);

        getContentPane().add(jpnClient, BorderLayout.CENTER);

        JPanel jpnButtons = new JPanel();
        jpnButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

        JButton jbtOk = new JButton();
        jbtOk.setText(Messages.getString("StrActionClose"));
        jbtOk.setName("btOk");
        jbtOk.setActionCommand("cmd_OK");
        jpnButtons.add(jbtOk);
        jbtOk.addActionListener(this);

        getContentPane().add(jpnButtons, BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(263, 281));
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(jbtOk);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cmd_OK")) {
            onOK();
        }
    }

    private void onOK() {
        modalResult = 1;
        setVisible(false);
        dispose();
    }
}