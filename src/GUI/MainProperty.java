package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static Common.Strings.*;

@SuppressWarnings("serial")
public class MainProperty extends JDialog implements ActionListener {
    private int ModalResult = 0;
    private JTabbedPane tpParams;
    private JPanel tabMain;
    private JPanel tabUpdate;
    private JPanel pnButtons;
    private JButton btCancel;
    private JButton btDefault;
    private JButton btOK;

    public MainProperty(Frame owner) {
        super(owner);
        setTitle(StrActionOptions);


        tpParams = new JTabbedPane();
        tabMain = new JPanel();
        tabMain.setLayout(new GridBagLayout());
        tpParams.addTab(StrPageMain, tabMain);

        tabUpdate = new JPanel();
        tabUpdate.setLayout(new java.awt.GridBagLayout());
        tpParams.addTab(StrPageUpdate, tabUpdate);

        add(tpParams, BorderLayout.CENTER);

        pnButtons = new JPanel();
        pnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        btDefault = new JButton();
        btDefault.setText(StrBtDefault);
        btDefault.setName("btDefault");
        btDefault.setActionCommand("cmd_Default");
        btDefault.addActionListener(this);
        pnButtons.add(btDefault);

        btOK = new JButton();
        btOK.setText(StrBtSave);
        btOK.setName("btOK");
        btOK.setActionCommand("cmd_OK");
        btOK.addActionListener(this);
        pnButtons.add(btOK);

        btCancel = new JButton();
        btCancel.setText(StrBtCancel);
        btCancel.setName("btCancel");
        btCancel.setActionCommand("cmd_Cancel");
        btCancel.addActionListener(this);
        pnButtons.add(btCancel);

        add(pnButtons, BorderLayout.PAGE_END);

        setSize(new Dimension(416, 338));
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(btOK);
    }

    public int getModalResult() {
        return ModalResult;
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
        }

    }

    private void onCancel() {
        ModalResult = 0;
        setVisible(false);
        dispose();
    }

    private void onOK() {
        ModalResult = 1;
        setVisible(false);
        dispose();
    }

    private void onDefault() {
        //
    }

}