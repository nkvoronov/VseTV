package GUI;

import Common.Common;
import Parser.ChannelList;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import static Common.Strings.*;

@SuppressWarnings("serial")
public class UpdateForm extends JDialog implements WindowListener, ActionListener, ChangeListener {
    private ChannelList Channels;
    private int ModalResult = 0;
    private DefaultListModel<String> listModel;
    private JList<String> lstLog;
    private JScrollPane spLog;
    private JPanel pnButtons;
    private JProgressBar pbUpdate;
    private JButton btExecute;

    public UpdateForm(Dialog owner, Boolean isUpdChannels) {
        super(owner, "Update");
        
        if (isUpdChannels) {
            this.setTitle(StrTitleUpdChannels);
        } else {
            this.setTitle(StrTitleUpdIcons);
        }
        setType(Window.Type.UTILITY);

        spLog = new JScrollPane();
        listModel = new DefaultListModel<>();
        lstLog = new JList<>(listModel);
        lstLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        spLog.setViewportView(lstLog);
        add(spLog, BorderLayout.CENTER);

        pnButtons = new JPanel();
        pnButtons.setLayout(new BorderLayout(4, 4));

        pbUpdate = new JProgressBar();
        pbUpdate.setMaximum(0);
        pbUpdate.setMinimum(0);
        pbUpdate.setValue(0);

        pnButtons.add(pbUpdate, BorderLayout.CENTER);

        btExecute = new JButton();        
        btExecute.setText(StrActionStop);
        btExecute.setActionCommand("cmd_Stop");
        btExecute.addActionListener(this);
        pnButtons.add(btExecute, BorderLayout.LINE_END);

        add(pnButtons, BorderLayout.PAGE_END);

        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(btExecute);
        addWindowListener(this);
        
        Channels = new ChannelList(Common.Lang, Common.IndexSort);
        Channels.setIsUpdChannels(isUpdChannels);
        Channels.getMonitor().addChangeListener(this);
    }

    public int getModalResult() {
        return ModalResult;
    }

    private void onOK() {
        ModalResult = 1;
        setVisible(false);
        dispose();
    }

    private void onStop() {
        ModalResult = 0;
        setVisible(false);
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cmd_OK")) {
            onOK();
        } else if (e.getActionCommand().equals("cmd_Stop")) {
            onStop();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        pbUpdate.setIndeterminate(Channels.getMonitor().isIndeterminate());
        pbUpdate.setMaximum(Channels.getMonitor().getTotal());
        pbUpdate.setValue(Channels.getMonitor().getCurrent());

        if (!Objects.equals(Channels.getMonitor().getStatus(), "")) {
            listModel.addElement(Channels.getMonitor().getStatus());
            lstLog.ensureIndexIsVisible(listModel.size()-1);
            lstLog.setSelectedIndex(listModel.size() - 1);
        }
        if (Channels.getMonitor().getCurrent() == -2) {
            btExecute.setText(StrActionClose);
            btExecute.setActionCommand("cmd_OK");
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        new Thread(Channels).start();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        //
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //
    }
}