package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import common.AppConfig;
import common.CommonTypes;


@SuppressWarnings("serial")
public class MainProperty extends JDialog implements ActionListener {
    private int modalResult = 0;
    private int countDay;
    private boolean useFullDesc;
    private JComboBox<String> jcbxCountDay;
    private JCheckBox jcbxUseFullDescription;

    public MainProperty(Frame owner) {
        super(owner);
        countDay = CommonTypes.appConfig.getCoutDays();
        useFullDesc = CommonTypes.appConfig.isFullDesc();
        initGUI();       
    }
    
    private void initGUI() {
		setTitle(Messages.getString("StrActionOptions"));
		GridBagConstraints gridBagConstraints;

		JTabbedPane jtpParams = new JTabbedPane();
		
		JPanel jpnMain = new JPanel();
        jtpParams.addTab(Messages.getString("StrPageMain"), jpnMain);
        GridBagLayout gblClientLayoutMain = new GridBagLayout();
        gblClientLayoutMain.columnWidths = new int[] {91, 100};
        jpnMain.setLayout(gblClientLayoutMain);
        
        JLabel jlbCountDay = new JLabel();
        jlbCountDay.setHorizontalAlignment(SwingConstants.LEFT);
        jlbCountDay.setLabelFor(jcbxCountDay);
        jlbCountDay.setText(Messages.getString("StrLbCountDay"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 5, 5);
        jpnMain.add(jlbCountDay, gridBagConstraints);
                
        jcbxCountDay = new JComboBox<>();
        jcbxCountDay.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4", "5", "6", "7"}));
        jcbxCountDay.setSelectedIndex(countDay - 1);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 5, 4);
        jcbxCountDay.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				countDay = jcbxCountDay.getSelectedIndex() + 1;
			}
		});        
        jpnMain.add(jcbxCountDay, gridBagConstraints);
        
        jcbxUseFullDescription = new JCheckBox();
        jcbxUseFullDescription.setHorizontalAlignment(SwingConstants.LEFT);
        jcbxUseFullDescription.setText(Messages.getString("StrLbUseFullDescription"));
        jcbxUseFullDescription.setSelected(useFullDesc);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 5, 4);
        jcbxUseFullDescription.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				useFullDesc = jcbxUseFullDescription.isSelected();
			}        	
        });
        jpnMain.add(jcbxUseFullDescription, gridBagConstraints);
                
        getContentPane().add(jtpParams, BorderLayout.CENTER);

        JPanel jpnButtons = new JPanel();
        jpnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        JButton jbtDefault = new JButton();
        jbtDefault.setText(Messages.getString("StrBtDefault"));
        jbtDefault.setName("btDefault");
        jbtDefault.setActionCommand("cmd_Default");
        jbtDefault.addActionListener(this);
        jpnButtons.add(jbtDefault);

        JButton jbtOK = new JButton();
        jbtOK.setText(Messages.getString("StrBtSave"));
        jbtOK.setName("btOK");
        jbtOK.setActionCommand("cmd_OK");
        jbtOK.addActionListener(this);
        jpnButtons.add(jbtOK);

        JButton jbtCancel = new JButton();
        jbtCancel.setText(Messages.getString("StrBtCancel"));
        jbtCancel.setName("btCancel");
        jbtCancel.setActionCommand("cmd_Cancel");
        jbtCancel.addActionListener(this);
        jpnButtons.add(jbtCancel);

        getContentPane().add(jpnButtons, BorderLayout.PAGE_END);

        setSize(new Dimension(416, 184));
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
    	CommonTypes.appConfig.setCoutDays(countDay);
    	CommonTypes.appConfig.setFullDesc(useFullDesc);
        modalResult = 1;
        setVisible(false);
        dispose();
    }

    private void onDefault() {
    	countDay = AppConfig.COUNT_DAY;
    	useFullDesc = AppConfig.FULL_DESC;
    	jcbxCountDay.setSelectedIndex(countDay - 1);
    	jcbxUseFullDescription.setSelected(useFullDesc);
    }

}