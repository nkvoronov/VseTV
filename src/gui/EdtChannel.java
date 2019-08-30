package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class EdtChannel extends JDialog implements ActionListener {
    private int modalResult = 0;
    private JTextField jtfIndex;
    private JTextField jtfName;
    private JTextField jtfIcon;
    private JComboBox<String> jcbLang;

    public EdtChannel(Dialog owner, String title) {
        super(owner, title);
        initGUI();
    }
    
    private void initGUI() {		
        GridBagConstraints gridBagConstraints;
        JPanel jpnClient = new JPanel();
        GridBagLayout gblClientLayout = new GridBagLayout();
        gblClientLayout.columnWidths = new int[] {91, 100};
        jpnClient.setLayout(gblClientLayout);

        JLabel jlbIndex = new JLabel();
        jlbIndex.setHorizontalAlignment(SwingConstants.RIGHT);
        jlbIndex.setLabelFor(jtfIndex);
        jlbIndex.setText(Messages.getString("StrLbIndex"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 5, 5);
        jpnClient.add(jlbIndex, gridBagConstraints);

        jtfIndex = new JTextField();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 5, 4);
        jpnClient.add(jtfIndex, gridBagConstraints);

        JLabel jlbName = new JLabel();
        jlbName.setHorizontalAlignment(SwingConstants.RIGHT);
        jlbName.setLabelFor(jtfName);
        jlbName.setText(Messages.getString("StrlbName"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 5, 5);
        jpnClient.add(jlbName, gridBagConstraints);

        jtfName = new JTextField();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 5, 4);
        jpnClient.add(jtfName, gridBagConstraints);

        JLabel jlbIcon = new JLabel();
        jlbIcon.setHorizontalAlignment(SwingConstants.RIGHT);
        jlbIcon.setLabelFor(jtfIcon);
        jlbIcon.setText(Messages.getString("StrlbIcon"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 5, 5);
        jpnClient.add(jlbIcon, gridBagConstraints);

        jtfIcon = new JTextField();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(4, 0, 5, 4);
        jpnClient.add(jtfIcon, gridBagConstraints);

        getContentPane().add(jpnClient, BorderLayout.CENTER);
        
        JLabel jlbLang = new JLabel(Messages.getString("StrLbLang"));
        GridBagConstraints gbcLang = new GridBagConstraints();
        gbcLang.insets = new Insets(0, 0, 0, 5);
        gbcLang.anchor = GridBagConstraints.EAST;
        gbcLang.gridx = 0;
        gbcLang.gridy = 3;
        jpnClient.add(jlbLang, gbcLang);
        
        jcbLang = new JComboBox<String>();
        jcbLang.setModel(new DefaultComboBoxModel<String>(new String[] {"rus", "ukr"}));
        jcbLang.setSelectedIndex(0);
        GridBagConstraints gbc_jcbLang = new GridBagConstraints();
        gbc_jcbLang.insets = new Insets(0, 0, 0, 4);
        gbc_jcbLang.fill = GridBagConstraints.HORIZONTAL;
        gbc_jcbLang.gridwidth = 6;
        gbc_jcbLang.gridx = 1;
        gbc_jcbLang.gridy = 3;
        jpnClient.add(jcbLang, gbc_jcbLang);

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

        setSize(new Dimension(360, 187));
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

    public String getIndex() {
        return jtfIndex.getText();
    }

    public void setIndex(String index) {
        this.jtfIndex.setText(index);
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
    public String getLang() {
    	if (this.jcbLang.getSelectedIndex() == 0) {
    		return "rus";
    	} else {
    		return "ukr";
    	}
    }
    
    public void setLang(String lang) {
    	if (lang.equals("rus")) {
    		this.jcbLang.setSelectedIndex(0);
    	}
    	if (lang.equals("ukr")) {
    		this.jcbLang.setSelectedIndex(1);
    	}
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