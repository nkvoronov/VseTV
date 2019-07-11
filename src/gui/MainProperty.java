package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class MainProperty extends JDialog implements ActionListener {
    private int modalResult = 0;
    private JTabbedPane jtpParams;
    private JPanel jpnMain;
    private JPanel jpnUpdate;
    private JPanel jpnButtons;
    private JButton jbtCancel;
    private JButton jbtDefault;
    private JButton jbtOK;

    public MainProperty(Frame owner) {
        super(owner);
        initGUI();       
    }
    
    private void initGUI() {
		setTitle(Messages.getString("StrActionOptions"));

        jtpParams = new JTabbedPane();
        jpnMain = new JPanel();
        jpnMain.setLayout(new GridBagLayout());
        jtpParams.addTab(Messages.getString("StrPageMain"), jpnMain);

        jpnUpdate = new JPanel();
        jpnUpdate.setLayout(new java.awt.GridBagLayout());
        jtpParams.addTab(Messages.getString("StrPageUpdate"), jpnUpdate);

        getContentPane().add(jtpParams, BorderLayout.CENTER);

        jpnButtons = new JPanel();
        jpnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        jbtDefault = new JButton();
        jbtDefault.setText(Messages.getString("StrBtDefault"));
        jbtDefault.setName("btDefault");
        jbtDefault.setActionCommand("cmd_Default");
        jbtDefault.addActionListener(this);
        jpnButtons.add(jbtDefault);

        jbtOK = new JButton();
        jbtOK.setText(Messages.getString("StrBtSave"));
        jbtOK.setName("btOK");
        jbtOK.setActionCommand("cmd_OK");
        jbtOK.addActionListener(this);
        jpnButtons.add(jbtOK);

        jbtCancel = new JButton();
        jbtCancel.setText(Messages.getString("StrBtCancel"));
        jbtCancel.setName("btCancel");
        jbtCancel.setActionCommand("cmd_Cancel");
        jbtCancel.addActionListener(this);
        jpnButtons.add(jbtCancel);

        getContentPane().add(jpnButtons, BorderLayout.PAGE_END);

        setSize(new Dimension(416, 338));
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(jbtOK);
	}

    public int getModalResult() {
        return modalResult;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "cmd_OK":
                onOK();
                break;
            case "cmd_Cancel":
                onCancel();
                break;
            case "cmd_Default":
                onDefault();
                break;
            default:
            	break;
        }

    }

    private void onCancel() {
        modalResult = 0;
        setVisible(false);
        dispose();
    }

    private void onOK() {
        modalResult = 1;
        setVisible(false);
        dispose();
    }

    private void onDefault() {
        //
    }

}