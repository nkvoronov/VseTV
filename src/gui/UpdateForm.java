package gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import common.CommonTypes;
import parser.ChannelList;

@SuppressWarnings("serial")
public class UpdateForm extends JDialog implements ActionListener, ChangeListener {
    private ChannelList channels;
    private int modalResult = 0;
    private DefaultListModel<String> listModel;
    private JList<String> jltLog;
    private JProgressBar jpbUpdate;
    private JButton jbtExecute;

    public UpdateForm(Dialog owner, Boolean isUpdChannels) {
        super(owner, "Update");
               
        if (isUpdChannels) {
            this.setTitle(Messages.getString("StrTitleUpdChannels"));
        } else {
            this.setTitle(Messages.getString("StrTitleUpdIcons"));
        }
        
        initGUI();
        
        channels = new ChannelList(CommonTypes.LANG, CommonTypes.INDEX_SORT);
        channels.setIsUpdChannels(isUpdChannels);
        channels.getMonitor().addChangeListener(this);
        
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowOpened(WindowEvent e) {
        		new Thread(channels).start();
        	}
        });

    }
    
    private void initGUI() {
		setType(Window.Type.UTILITY);

		JScrollPane jspLog = new JScrollPane();
        listModel = new DefaultListModel<>();
        jltLog = new JList<>(listModel);
        jltLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jspLog.setViewportView(jltLog);
        getContentPane().add(jspLog, BorderLayout.CENTER);

        JPanel jpnButtons = new JPanel();
        jpnButtons.setLayout(new BorderLayout(4, 4));

        jpbUpdate = new JProgressBar();
        jpbUpdate.setMaximum(0);
        jpbUpdate.setMinimum(0);
        jpbUpdate.setValue(0);

        jpnButtons.add(jpbUpdate, BorderLayout.CENTER);

        jbtExecute = new JButton();        
        jbtExecute.setText(Messages.getString("StrActionStop"));
        jbtExecute.setActionCommand("cmd_Stop");
        jbtExecute.addActionListener(this);
        jpnButtons.add(jbtExecute, BorderLayout.LINE_END);

        getContentPane().add(jpnButtons, BorderLayout.PAGE_END);

        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(jbtExecute);
	}

    public int getModalResult() {
        return modalResult;
    }

    private void onOK() {
        modalResult = 1;
        setVisible(false);
        dispose();
    }

    private void onStop() {
        modalResult = 0;
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
        jpbUpdate.setIndeterminate(channels.getMonitor().isIndeterminate());
        jpbUpdate.setMaximum(channels.getMonitor().getTotal());
        jpbUpdate.setValue(channels.getMonitor().getCurrent());

        if (!Objects.equals(channels.getMonitor().getStatus(), "")) {
            listModel.addElement(channels.getMonitor().getStatus());
            jltLog.ensureIndexIsVisible(listModel.size()-1);
            jltLog.setSelectedIndex(listModel.size() - 1);
        }
        if (channels.getMonitor().getCurrent() == -2) {
            jbtExecute.setText(Messages.getString("StrActionClose"));
            jbtExecute.setActionCommand("cmd_OK");
        }
    }

}