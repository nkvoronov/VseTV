package GUI;

import Common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import static Common.DBUtils.getExecutePreparedUpdate;
import static Common.DBUtils.getExecuteUpdate;
import static Common.Strings.*;

@SuppressWarnings("serial")
public class ChannelsProperty extends JDialog {
    private int ModalResult = 0;
    private ActionAddToUser acAddToUser;    
    private ActionAddAllToUser acAddAllToUser;    
    private ActionAddChannel acAddChannel;    
    private ActionEdtChannel acEdtChannel;    
    private ActionDelChannel acDelChannel;    
    private ActionDelAllChannels acDelAllChannels;   
    private ActionUpdateChannels acUpdateChannels;    
    private ActionUpdateIcons acUpdateIcons;    
    private ActionDelFromUser acDelFromUser;    
    private ActionDelAllFromUser acDelAllFromUser;    
    private ActionEdtUsrChannel acEdtUsrChannel;   
    private ActionSetCorrection acSetCorrection;    
    private ActionSave acSave;
    private DBTableModelChannels ChannelsModel;
    private DBTableModelUserChannels UserChannelsModel;
    private JSplitPane spChannelsList;
    private JPanel pnLeft;
    private JToolBar tbChannels;
    private JButton btAddToUser;
    private JButton btAddAllToUser;
    private JButton btAddChannel;
    private JButton btEdtChannel;
    private JButton btDelChannel;
    private JButton btDelAllChannels;
    private JButton btUpdateChannels;
    private JButton btUpdateIcons;
    private JScrollPane scpLeft;
    private JTable tblChannels;
    private JPanel pnRight;
    private JToolBar tbUserChannels;
    private JButton btDelFromUser;
    private JButton btDelAllFromUser;
    private JButton btEdtUsrChannel;
    private JTextField tfCorrection;
    private JButton btSetCorrection;
    private JScrollPane scpRight;
    private JTable tblUserChannels;
    private JPanel pnButtons;
    private JButton btSave;
    private JPopupMenu pmChannels;
    private JMenuItem miAddToUser;
    private JMenuItem miAddChannel;
    private JMenuItem miEdtChannel;
    private JMenuItem miDelChannel;
    private JPopupMenu pmUserChannels;
    private JMenuItem miDelFromUser;
    private JMenuItem miEdtUsrChannel;

    public ChannelsProperty(Frame owner) {
        super(owner);
        
        acAddToUser = new ActionAddToUser("");
        acAddAllToUser = new ActionAddAllToUser("");
        acAddChannel = new ActionAddChannel("");
        acEdtChannel = new ActionEdtChannel("");
        acDelChannel = new ActionDelChannel("");
        acDelAllChannels = new ActionDelAllChannels("");
        acUpdateChannels = new ActionUpdateChannels("");
        acUpdateIcons = new ActionUpdateIcons("");        
        acDelFromUser = new ActionDelFromUser("");       
        acDelAllFromUser = new ActionDelAllFromUser("");        
        acEdtUsrChannel = new ActionEdtUsrChannel("");        
        acSetCorrection = new ActionSetCorrection("");        
        acSave = new ActionSave("");
        
        setTitle(StrTitleChannelList);

        //Client

        spChannelsList = new JSplitPane();
        spChannelsList.setBorder(null);
        spChannelsList.setDividerLocation(360);
        spChannelsList.setDividerSize(3);
        spChannelsList.setAutoscrolls(true);

        //Left

        pnLeft = new JPanel();
        pnLeft.setBorder(null);
        pnLeft.setLayout(new BorderLayout());

        tbChannels = new JToolBar();
        tbChannels.setBorder(BorderFactory.createEtchedBorder());
        tbChannels.setFloatable(false);

        btAddToUser = new JButton();
        btAddToUser.setAction(acAddToUser);
        btAddToUser.setText("");
        btAddToUser.setFocusable(false);
        btAddToUser.setHorizontalTextPosition(SwingConstants.CENTER);
        btAddToUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btAddToUser);

        btAddAllToUser = new JButton();
        btAddAllToUser.setAction(acAddAllToUser);
        btAddAllToUser.setText("");
        btAddAllToUser.setFocusable(false);
        btAddAllToUser.setHorizontalTextPosition(SwingConstants.CENTER);
        btAddAllToUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btAddAllToUser);

        tbChannels.add(new JToolBar.Separator());

        btAddChannel = new JButton();
        btAddChannel.setAction(acAddChannel);
        btAddChannel.setText("");
        btAddChannel.setFocusable(false);
        btAddChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        btAddChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btAddChannel);

        btEdtChannel = new JButton();
        btEdtChannel.setAction(acEdtChannel);
        btEdtChannel.setText("");
        btEdtChannel.setFocusable(false);
        btEdtChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        btEdtChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btEdtChannel);

        btDelChannel = new JButton();
        btDelChannel.setAction(acDelChannel);
        btDelChannel.setText("");
        btDelChannel.setFocusable(false);
        btDelChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        btDelChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btDelChannel);

        btDelAllChannels = new JButton();
        btDelAllChannels.setAction(acDelAllChannels);
        btDelAllChannels.setText("");
        btDelAllChannels.setFocusable(false);
        btDelAllChannels.setHorizontalTextPosition(SwingConstants.CENTER);
        btDelAllChannels.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btDelAllChannels);

        tbChannels.add(new JToolBar.Separator());

        btUpdateChannels = new JButton();
        btUpdateChannels.setAction(acUpdateChannels);
        btUpdateChannels.setText("");
        btUpdateChannels.setFocusable(false);
        btUpdateChannels.setHorizontalTextPosition(SwingConstants.CENTER);
        btUpdateChannels.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btUpdateChannels);

        btUpdateIcons = new JButton();
        btUpdateIcons.setAction(acUpdateIcons);
        btUpdateIcons.setText("");
        btUpdateIcons.setFocusable(false);
        btUpdateIcons.setHorizontalTextPosition(SwingConstants.CENTER);
        btUpdateIcons.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbChannels.add(btUpdateIcons);

        tbChannels.setPreferredSize(new Dimension(300, 30));

        pnLeft.add(tbChannels, BorderLayout.PAGE_START);

        //Left table

        scpLeft = new JScrollPane();
        tblChannels = new JTable();
        tblChannels.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
        			onEdtChannel();		
        		}	
        	}
        });
        ChannelsModel = new DBTableModelChannels(DBUtils.sqlChannels);
        tblChannels.setModel(ChannelsModel);

        tblChannels.setFillsViewportHeight(true);
        tblChannels.setFocusable(false);
        tblChannels.setRowHeight(28);
        tblChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChannels.setShowHorizontalLines(true);
        tblChannels.setShowVerticalLines(false);
        tblChannels.getTableHeader().setResizingAllowed(false);
        tblChannels.getTableHeader().setReorderingAllowed(false);
        tblChannels.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChannels.setDefaultRenderer(Object.class, new DBTableRender());
        tblChannels.setGridColor(Color.LIGHT_GRAY);
        tblChannels.setIntercellSpacing(new Dimension(0, 1));

        if (tblChannels.getColumnModel().getColumnCount() > 0) {
            tblChannels.getColumnModel().getColumn(0).setMinWidth(0);
            tblChannels.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblChannels.getColumnModel().getColumn(0).setMaxWidth(0);
            tblChannels.getColumnModel().getColumn(1).setMinWidth(0);
            tblChannels.getColumnModel().getColumn(1).setPreferredWidth(0);
            tblChannels.getColumnModel().getColumn(1).setMaxWidth(0);
            tblChannels.getColumnModel().getColumn(2).setMinWidth(0);
            tblChannels.getColumnModel().getColumn(2).setPreferredWidth(0);
            tblChannels.getColumnModel().getColumn(2).setMaxWidth(0);
            tblChannels.getColumnModel().getColumn(3).setMinWidth(28);
            tblChannels.getColumnModel().getColumn(3).setPreferredWidth(28);
            tblChannels.getColumnModel().getColumn(3).setMaxWidth(28);
            tblChannels.getColumnModel().getColumn(4).setMinWidth(28);
            tblChannels.getColumnModel().getColumn(4).setPreferredWidth(28);
            tblChannels.getColumnModel().getColumn(4).setMaxWidth(28);
        }

        tblChannels.getSelectionModel().addListSelectionListener(e -> {
            if (tblChannels.getSelectedRow() != -1) {
                onSelectChannelsRow();
            }
        });

        pmChannels = new JPopupMenu();

        miAddToUser = new JMenuItem(acAddToUser);
        miAddToUser.setToolTipText("");
        pmChannels.add(miAddToUser);

        pmChannels.add(new JSeparator());

        miAddChannel = new JMenuItem(acAddChannel);
        miAddChannel.setToolTipText("");
        pmChannels.add(miAddChannel);

        miEdtChannel = new JMenuItem(acEdtChannel);
        miEdtChannel.setToolTipText("");
        pmChannels.add(miEdtChannel);

        miDelChannel = new JMenuItem(acDelChannel);
        miDelChannel.setToolTipText("");
        pmChannels.add(miDelChannel);

        tblChannels.setComponentPopupMenu(pmChannels);

        scpLeft.setViewportView(tblChannels);
        pnLeft.add(scpLeft, BorderLayout.CENTER);
        spChannelsList.setLeftComponent(pnLeft);

        //Right

        pnRight = new JPanel();
        pnRight.setBorder(null);
        pnRight.setLayout(new BorderLayout());

        tbUserChannels = new JToolBar();
        tbUserChannels.setBorder(BorderFactory.createEtchedBorder());
        tbUserChannels.setFloatable(false);

        btDelFromUser = new JButton();
        btDelFromUser.setAction(acDelFromUser);
        btDelFromUser.setText("");
        btDelFromUser.setFocusable(false);
        btDelFromUser.setHorizontalTextPosition(SwingConstants.CENTER);
        btDelFromUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbUserChannels.add(btDelFromUser);

        btDelAllFromUser = new JButton();
        btDelAllFromUser.setAction(acDelAllFromUser);
        btDelAllFromUser.setText("");
        btDelAllFromUser.setFocusable(false);
        btDelAllFromUser.setHorizontalTextPosition(SwingConstants.CENTER);
        btDelAllFromUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbUserChannels.add(btDelAllFromUser);

        tbUserChannels.add(new JToolBar.Separator());

        btEdtUsrChannel = new JButton();
        btEdtUsrChannel.setAction(acEdtUsrChannel);
        btEdtUsrChannel.setText("");
        btEdtUsrChannel.setFocusable(false);
        btEdtUsrChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        btEdtUsrChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbUserChannels.add(btEdtUsrChannel);

        tbUserChannels.add(new JToolBar.Separator());

        tfCorrection = new JTextField();
        tfCorrection.setActionCommand("cmd_Correction");
        tfCorrection.setHorizontalAlignment(JTextField.RIGHT);
        tfCorrection.setText("0");
        tfCorrection.setToolTipText(StrlbCorrection);
        tfCorrection.setPreferredSize(new Dimension(60, 28));
        tfCorrection.setMaximumSize(new Dimension(60, 28));
        tfCorrection.setMinimumSize(new Dimension(60, 28));
        tbUserChannels.add(tfCorrection);

        btSetCorrection = new JButton();
        btSetCorrection.setAction(acSetCorrection);
        btSetCorrection.setText("");
        btSetCorrection.setFocusable(false);
        btSetCorrection.setHorizontalTextPosition(SwingConstants.CENTER);
        btSetCorrection.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbUserChannels.add(btSetCorrection);

        tbUserChannels.setPreferredSize(new Dimension(300, 30));

        pnRight.add(tbUserChannels, BorderLayout.PAGE_START);

        //Right table

        scpRight = new JScrollPane();
        tblUserChannels = new JTable();
        tblUserChannels.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                if (tblChannels.getSelectedRow() != -1) {
                    onSelectChannelsRow();
                }
        		if (e.getClickCount() == 2) {
        			onEdtUsrChannel();		
        		}	
        	}
        });
        UserChannelsModel = new DBTableModelUserChannels(DBUtils.sqlUserChannels);
        tblUserChannels.setModel(UserChannelsModel);

        tblUserChannels.setFillsViewportHeight(true);
        tblUserChannels.setFocusable(false);
        tblUserChannels.setRowHeight(28);
        tblUserChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUserChannels.setShowHorizontalLines(true);
        tblUserChannels.setShowVerticalLines(false);
        tblUserChannels.getTableHeader().setResizingAllowed(false);
        tblUserChannels.getTableHeader().setReorderingAllowed(false);
        tblUserChannels.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUserChannels.setDefaultRenderer(Object.class, new DBTableRenderUserChannels());
        tblUserChannels.setGridColor(Color.LIGHT_GRAY);
        tblUserChannels.setIntercellSpacing(new Dimension(0, 1));

        DefaultTableCellRenderer centerRenderer = new DBTableRenderUserChannels();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        if (tblUserChannels.getColumnModel().getColumnCount() > 0) {
            tblUserChannels.getColumnModel().getColumn(0).setMinWidth(0);
            tblUserChannels.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblUserChannels.getColumnModel().getColumn(0).setMaxWidth(0);
            tblUserChannels.getColumnModel().getColumn(1).setMinWidth(0);
            tblUserChannels.getColumnModel().getColumn(1).setPreferredWidth(0);
            tblUserChannels.getColumnModel().getColumn(1).setMaxWidth(0);
            tblUserChannels.getColumnModel().getColumn(2).setMinWidth(28);
            tblUserChannels.getColumnModel().getColumn(2).setPreferredWidth(28);
            tblUserChannels.getColumnModel().getColumn(2).setMaxWidth(28);
            tblUserChannels.getColumnModel().getColumn(4).setMinWidth(70);
            tblUserChannels.getColumnModel().getColumn(4).setPreferredWidth(70);
            tblUserChannels.getColumnModel().getColumn(4).setMaxWidth(70);

            tblUserChannels.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        }

        pmUserChannels = new JPopupMenu();

        miDelFromUser = new JMenuItem(acDelFromUser);
        miDelFromUser.setToolTipText("");
        pmUserChannels.add(miDelFromUser);

        pmUserChannels.add(new JSeparator());

        miEdtUsrChannel = new JMenuItem(acEdtUsrChannel);
        miEdtUsrChannel.setToolTipText("");
        pmUserChannels.add(miEdtUsrChannel);

        tblUserChannels.setComponentPopupMenu(pmUserChannels);

        scpRight.setViewportView(tblUserChannels);
        pnRight.add(scpRight, BorderLayout.CENTER);
        spChannelsList.setRightComponent(pnRight);

        getContentPane().add(spChannelsList, BorderLayout.CENTER);

        //Bottom

        pnButtons = new JPanel();
        pnButtons.setBorder(null);
        pnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        btSave = new JButton();
        btSave.setAction(acSave);
        pnButtons.add(btSave);

        getContentPane().add(pnButtons, BorderLayout.PAGE_END);

        RefreshTableChannels(0);
        RefreshTableUserChannels(0);

        setSize(new Dimension(723, 563));
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(btSave);
    }

    public int getModalResult() {
        return ModalResult;
    }

    private void onAddToUser() {
        int row1 = tblChannels.getSelectedRow();
        TableModel tm = tblChannels.getModel();
        if (row1 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row1, 0));
            DBParams[] Params = new DBParams[2];
            Params[0] = new DBParams(1, id, Common.DBType.INTEGER);
            Params[1] = new DBParams(2, id, Common.DBType.INTEGER);
            if (getExecutePreparedUpdate(DBUtils.sqlAddChannel, Params) != -1) {
                RefreshTableChannels(row1);
                RefreshTableUserChannels(0);
            }
        }
    }

    private void onAddAllToUser() {
        int row1 = tblChannels.getSelectedRow();
        if (getExecuteUpdate(DBUtils.sqlAddAllChannels) != -1) {
            RefreshTableChannels(row1);
            RefreshTableUserChannels(0);
        }
    }

    private void onAddChannel() {
        TableModel tm = tblChannels.getModel();
        EdtChannel addchn = new EdtChannel(this, StrTitleAdd);
        addchn.setVisible(true);
        if (addchn.getModalResult() != 0) {
            DBParams[] Params = new DBParams[3];
            Params[0] = new DBParams(1, new Integer(addchn.getIndex()), Common.DBType.INTEGER);
            Params[1] = new DBParams(2, addchn.getName(), Common.DBType.STRING);
            Params[2] = new DBParams(3, addchn.getIcon(), Common.DBType.STRING);
            if (getExecutePreparedUpdate(DBUtils.sqlInsChannel, Params) != -1) {
                RefreshTableChannels(tm.getRowCount());
            }
        }
    }

    private void onEdtChannel() {
        int row1 = tblChannels.getSelectedRow();
        TableModel tm = tblChannels.getModel();
        if (row1 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row1, 0));
            EdtChannel addchn = new EdtChannel(this, StrTitleEdt);
            addchn.setIndex((String) tm.getValueAt(row1, 1));
            addchn.setName((String) tm.getValueAt(row1, 5));
            addchn.setIcon((String) tm.getValueAt(row1, 2));
            addchn.setVisible(true);
            if (addchn.getModalResult() != 0) {
                DBParams[] Params = new DBParams[4];
                Params[0] = new DBParams(1, new Integer(addchn.getIndex()), Common.DBType.INTEGER);
                Params[1] = new DBParams(2, addchn.getName(), Common.DBType.STRING);
                Params[2] = new DBParams(3, addchn.getIcon(), Common.DBType.STRING);
                Params[3] = new DBParams(4, id, Common.DBType.INTEGER);
                if (getExecutePreparedUpdate(DBUtils.sqlEdtChannel, Params) != -1) {
                    RefreshTableChannels(row1);
                }
            }
        }
    }

    private void onDelChannel() {
        int row1 = tblChannels.getSelectedRow();
        TableModel tm = tblChannels.getModel();
        if (row1 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row1, 0));
            if (JOptionPane.showConfirmDialog(this, StrConfirmDel, StrTitleDel, JOptionPane.YES_NO_OPTION) == 0) {
                DBParams[] Params = new DBParams[1];
                Params[0] = new DBParams(1, id, Common.DBType.INTEGER);
                if (getExecutePreparedUpdate(DBUtils.sqlDelChannels, Params) != -1) {
                    if (row1 != 0) {row1--;}
                    RefreshTableChannels(row1);
                    RefreshTableUserChannels(0);
                }
            }
        }
    }

    private void onDelAllChannels() {
        if (JOptionPane.showConfirmDialog(this, StrConfirmDelAll, StrTitleDel, JOptionPane.YES_NO_OPTION) == 0) {
            if (getExecuteUpdate(DBUtils.sqlDelAllChannels) != -1) {
                RefreshTableChannels(0);
                RefreshTableUserChannels(0);
            }
        }
    }

    private void onUpdateChannels() {
        UpdateForm updchn = new UpdateForm(this, true);
        updchn.setVisible(true);
        if (updchn.getModalResult() != 0) {
            RefreshTableChannels(0);
        }
    }

    private void onUpdateIcons() {
        UpdateForm updicn = new UpdateForm(this, false);
        updicn.setVisible(true);
        if (updicn.getModalResult() != 0) {
            RefreshTableChannels(0);
        }
    }

    private void onDelFromUser() {
        int row1 = tblChannels.getSelectedRow();
        int row2 = tblUserChannels.getSelectedRow();
        TableModel tm = tblUserChannels.getModel();
        if (row2 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row2, 0));
            if (JOptionPane.showConfirmDialog(this, StrConfirmDelUsr, StrTitleDel, JOptionPane.YES_NO_OPTION) == 0) {
                DBParams[] Params = new DBParams[1];
                Params[0] = new DBParams(1, id, Common.DBType.INTEGER);
                if (getExecutePreparedUpdate(DBUtils.sqlDelUserChannels, Params) != -1) {
                    if (row2 != 0) {row2--;}
                    RefreshTableChannels(row1);
                    RefreshTableUserChannels(row2);
                }
            }
        }
    }

    private void onDelAllFromUser() {
        int row1 = tblChannels.getSelectedRow();
        if (JOptionPane.showConfirmDialog(this, StrConfirmDelUsrAll, StrTitleDel, JOptionPane.YES_NO_OPTION) == 0) {
            if (getExecuteUpdate(DBUtils.sqlDelAllUserChannels) != -1) {
                RefreshTableChannels(row1);
                RefreshTableUserChannels(0);
            }
        }
    }

    private void onEdtUsrChannel() {
        int row2 = tblUserChannels.getSelectedRow();
        TableModel tm = tblUserChannels.getModel();
        if (row2 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row2, 0));
            EdtUsrChannel edtusrchn = new EdtUsrChannel(this);
            edtusrchn.setName((String) tm.getValueAt(row2, 3));
            edtusrchn.setIcon((String) tm.getValueAt(row2, 1));
            edtusrchn.setCorrection((String) tm.getValueAt(row2, 4));
            edtusrchn.setVisible(true);
            if (edtusrchn.getModalResult() != 0) {
                DBParams[] Params = new DBParams[4];
                Params[0] = new DBParams(1, edtusrchn.getName(), Common.DBType.STRING);
                Params[1] = new DBParams(2, edtusrchn.getIcon(), Common.DBType.STRING);
                Params[2] = new DBParams(3, new Integer(edtusrchn.getCorrection()), Common.DBType.INTEGER);
                Params[3] = new DBParams(4, id, Common.DBType.INTEGER);
                if (getExecutePreparedUpdate(DBUtils.sqlEdtUserChannels, Params) != -1) {
                    RefreshTableUserChannels(row2);
                }
            }
        }
    }

    private void onSetCorrection() {
        int row2 = tblUserChannels.getSelectedRow();
        Integer correction = new Integer(tfCorrection.getText());
        DBParams[] Params = new DBParams[1];
        Params[0] = new DBParams(1, correction, Common.DBType.INTEGER);
        if (getExecutePreparedUpdate(DBUtils.sqlSetCorrectionUserChannels, Params) != -1) {
            RefreshTableUserChannels(row2);
        }
    }

    private void onSelectChannelsRow() {
        int row1 = tblChannels.getSelectedRow();
        TableModel tm = tblChannels.getModel();
        if (row1 != -1) {
            Boolean isuse = (Boolean) tm.getValueAt(row1, 3);
            if (isuse) {
                acAddToUser.setEnabled(false);
            } else acAddToUser.setEnabled(true);
        }
    }

    private void onOK() {
        ModalResult = 1;
        setVisible(false);
        dispose();
    }

    private void RefreshTableChannels(int row) {
        ChannelsModel.refreshContent();
        tblChannels.setVisible(false);
        tblChannels.setVisible(true);
        if (tblChannels.getRowCount() != 0) {
            acAddToUser.setEnabled(true);
            acAddAllToUser.setEnabled(true);
            acEdtChannel.setEnabled(true);
            acDelChannel.setEnabled(true);
            acDelAllChannels.setEnabled(true);
            tblChannels.setRowSelectionInterval(row, row);
        } else {
            acAddToUser.setEnabled(false);
            acAddAllToUser.setEnabled(false);
            acEdtChannel.setEnabled(false);
            acDelChannel.setEnabled(false);
            acDelAllChannels.setEnabled(false);
        }
    }

    private void RefreshTableUserChannels(int row) {
        UserChannelsModel.refreshContent();
        tblUserChannels.setVisible(false);
        tblUserChannels.setVisible(true);
        if (tblUserChannels.getRowCount() != 0) {
            tfCorrection.setEnabled(true);
            acDelFromUser.setEnabled(true);
            acDelAllFromUser.setEnabled(true);
            acEdtUsrChannel.setEnabled(true);
            acSetCorrection.setEnabled(true);
            tblUserChannels.setRowSelectionInterval(row, row);
        } else {
            tfCorrection.setEnabled(false);
            acDelFromUser.setEnabled(false);
            acDelAllFromUser.setEnabled(false);
            acEdtUsrChannel.setEnabled(false);
            acSetCorrection.setEnabled(false);
        }
    }

    public class ActionAddToUser extends AbstractAction
    {
    	public ActionAddToUser(String text) {
    		putValue(NAME, StrTitleAdd);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrTitleAdd);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/add.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onAddToUser();    		
    	}
    }
    
    public class ActionAddAllToUser extends AbstractAction
    {
    	public ActionAddAllToUser(String text) {
    		putValue(NAME, StrActionAddAll);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionAddAll);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/add_all.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onAddAllToUser();    		
    	}
    }
    
    public class ActionAddChannel extends AbstractAction
    {
    	public ActionAddChannel(String text) {
    		putValue(NAME, StrActionAddChannel);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionAddChannel);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/add_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onAddChannel();    		
    	}
    }
    
    public class ActionEdtChannel extends AbstractAction
    {
    	public ActionEdtChannel(String text) {
    		putValue(NAME, StrTitleEdtChannels);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrTitleEdtChannels);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/edt_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onEdtChannel();   		
    	}
    }
    
    public class ActionDelChannel extends AbstractAction
    {
    	public ActionDelChannel(String text) {
    		putValue(NAME, StrActionDelChannel);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionDelChannel);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/del_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onDelChannel();   		
    	}
    }
    
    public class ActionDelAllChannels extends AbstractAction
    {
    	public ActionDelAllChannels(String text) {
    		putValue(NAME, StrActionDelChnAll);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionDelChnAll);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/del_all_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onDelAllChannels();    		
    	}
    }
    
    public class ActionUpdateChannels extends AbstractAction
    {
    	public ActionUpdateChannels(String text) {
    		putValue(NAME, StrActionUpdChns);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionUpdChns);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/update_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onUpdateChannels();   		
    	}
    }
    
    public class ActionUpdateIcons extends AbstractAction
    {
    	public ActionUpdateIcons(String text) {
    		putValue(NAME, StrActionUpdIcons);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionUpdIcons);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/update_icons.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onUpdateIcons();   		
    	}
    }    

    public class ActionDelFromUser extends AbstractAction
    {
    	public ActionDelFromUser(String text) {
    		putValue(NAME, StrActionDel);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionDel);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/del.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onDelFromUser();   		
    	}
    }
    
    public class ActionDelAllFromUser extends AbstractAction
    {
    	public ActionDelAllFromUser(String text) {
    		putValue(NAME, StrActionDelAll);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionDelAll);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/del_all.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onDelAllFromUser();    		
    	}
    }
    
    public class ActionEdtUsrChannel extends AbstractAction
    {
    	public ActionEdtUsrChannel(String text) {
    		putValue(NAME, StrTitleEdt);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrTitleEdt);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/usr_channel_edt.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onEdtUsrChannel();   		
    	}
    }
    
    public class ActionSetCorrection extends AbstractAction
    {
    	public ActionSetCorrection(String text) {
    		putValue(NAME, StrActionSetCorrect);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionSetCorrect);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/set_correction.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onSetCorrection();   		
    	}
    }
    public class ActionSave extends AbstractAction
    {
    	public ActionSave(String text) {
    		putValue(NAME, StrBtSave);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrBtSave);
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/Resources/savexmltv.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onOK();   		
    	}
    }
}