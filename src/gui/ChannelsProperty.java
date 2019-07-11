package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import common.DBParams;
import common.DBTableModelChannels;
import common.DBTableModelUserChannels;
import common.DBTableRender;
import common.DBTableRenderUserChannels;
import common.DBUtils;
import common.CommonTypes;

@SuppressWarnings("serial")
public class ChannelsProperty extends JDialog {
    private int modalResult = 0;
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
    private DBTableModelChannels channelsModel;
    private DBTableModelUserChannels userChannelsModel;
    private JSplitPane jslChannelsList;
    private JPanel jpnLeft;
    private JToolBar jtbrChannels;
    private JButton jbtAddToUser;
    private JButton jbtAddAllToUser;
    private JButton jbtAddChannel;
    private JButton jbtEdtChannel;
    private JButton jbtDelChannel;
    private JButton jbtDelAllChannels;
    private JButton jbtUpdateChannels;
    private JButton jbtUpdateIcons;
    private JScrollPane jspLeft;
    private JTable jtbChannels;
    private JPanel jpnRight;
    private JToolBar jtbrUserChannels;
    private JButton jbtDelFromUser;
    private JButton jbtDelAllFromUser;
    private JButton jbtEdtUsrChannel;
    private JTextField jtfCorrection;
    private JButton jbtSetCorrection;
    private JScrollPane jspRight;
    private JTable jtbUserChannels;
    private JPanel jpnButtons;
    private JButton jbtSave;
    private JPopupMenu jpmChannels;
    private JMenuItem jmiAddToUser;
    private JMenuItem jmiAddChannel;
    private JMenuItem jmiEdtChannel;
    private JMenuItem jmiDelChannel;
    private JPopupMenu jpmUserChannels;
    private JMenuItem jmiDelFromUser;
    private JMenuItem jmiEdtUsrChannel;

    public ChannelsProperty(Frame owner) {
        super(owner);        
        initActions();
        initGUI();
    }
    
    private void initActions() {
    	acAddToUser = new ActionAddToUser();
        acAddAllToUser = new ActionAddAllToUser();
        acAddChannel = new ActionAddChannel(this);
        acEdtChannel = new ActionEdtChannel(this);
        acDelChannel = new ActionDelChannel(this);
        acDelAllChannels = new ActionDelAllChannels(this);
        acUpdateChannels = new ActionUpdateChannels(this);
        acUpdateIcons = new ActionUpdateIcons(this);        
        acDelFromUser = new ActionDelFromUser(this);       
        acDelAllFromUser = new ActionDelAllFromUser(this);        
        acEdtUsrChannel = new ActionEdtUsrChannel(this);        
        acSetCorrection = new ActionSetCorrection();        
        acSave = new ActionSave(this);
	}
    
    private void initGUI() {
        setTitle(Messages.getString("StrActionChannels"));

        //Client

        jslChannelsList = new JSplitPane();
        jslChannelsList.setBorder(null);
        jslChannelsList.setDividerLocation(360);
        jslChannelsList.setDividerSize(3);
        jslChannelsList.setAutoscrolls(true);

        //Left

        jpnLeft = new JPanel();
        jpnLeft.setBorder(null);
        jpnLeft.setLayout(new BorderLayout());

        jtbrChannels = new JToolBar();
        jtbrChannels.setBorder(BorderFactory.createEtchedBorder());
        jtbrChannels.setFloatable(false);

        jbtAddToUser = new JButton();
        jbtAddToUser.setAction(acAddToUser);
        jbtAddToUser.setText("");
        jbtAddToUser.setFocusable(false);
        jbtAddToUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddToUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddToUser);

        jbtAddAllToUser = new JButton();
        jbtAddAllToUser.setAction(acAddAllToUser);
        jbtAddAllToUser.setText("");
        jbtAddAllToUser.setFocusable(false);
        jbtAddAllToUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddAllToUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddAllToUser);

        jtbrChannels.add(new JToolBar.Separator());

        jbtAddChannel = new JButton();
        jbtAddChannel.setAction(acAddChannel);
        jbtAddChannel.setText("");
        jbtAddChannel.setFocusable(false);
        jbtAddChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddChannel);

        jbtEdtChannel = new JButton();
        jbtEdtChannel.setAction(acEdtChannel);
        jbtEdtChannel.setText("");
        jbtEdtChannel.setFocusable(false);
        jbtEdtChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtEdtChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtEdtChannel);

        jbtDelChannel = new JButton();
        jbtDelChannel.setAction(acDelChannel);
        jbtDelChannel.setText("");
        jbtDelChannel.setFocusable(false);
        jbtDelChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtDelChannel);

        jbtDelAllChannels = new JButton();
        jbtDelAllChannels.setAction(acDelAllChannels);
        jbtDelAllChannels.setText("");
        jbtDelAllChannels.setFocusable(false);
        jbtDelAllChannels.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelAllChannels.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtDelAllChannels);

        jtbrChannels.add(new JToolBar.Separator());

        jbtUpdateChannels = new JButton();
        jbtUpdateChannels.setAction(acUpdateChannels);
        jbtUpdateChannels.setText("");
        jbtUpdateChannels.setFocusable(false);
        jbtUpdateChannels.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtUpdateChannels.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtUpdateChannels);

        jbtUpdateIcons = new JButton();
        jbtUpdateIcons.setAction(acUpdateIcons);
        jbtUpdateIcons.setText("");
        jbtUpdateIcons.setFocusable(false);
        jbtUpdateIcons.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtUpdateIcons.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtUpdateIcons);

        jtbrChannels.setPreferredSize(new Dimension(300, 30));

        jpnLeft.add(jtbrChannels, BorderLayout.PAGE_START);

        //Left table

        jspLeft = new JScrollPane();
        jtbChannels = new JTable();
        jtbChannels.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
        			acEdtChannel.onExecute();		
        		}	
        	}
        });
        channelsModel = new DBTableModelChannels(DBUtils.SQL_CHANNELS);
        jtbChannels.setModel(channelsModel);

        jtbChannels.setFillsViewportHeight(true);
        jtbChannels.setFocusable(false);
        jtbChannels.setRowHeight(28);
        jtbChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbChannels.setShowHorizontalLines(true);
        jtbChannels.setShowVerticalLines(false);
        jtbChannels.getTableHeader().setResizingAllowed(false);
        jtbChannels.getTableHeader().setReorderingAllowed(false);
        jtbChannels.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbChannels.setDefaultRenderer(Object.class, new DBTableRender());
        jtbChannels.setGridColor(Color.LIGHT_GRAY);
        jtbChannels.setIntercellSpacing(new Dimension(0, 1));

        if (jtbChannels.getColumnModel().getColumnCount() > 0) {
        	jtbChannels.getColumnModel().getColumn(0).setMinWidth(0);
        	jtbChannels.getColumnModel().getColumn(0).setPreferredWidth(0);
        	jtbChannels.getColumnModel().getColumn(0).setMaxWidth(0);
        	jtbChannels.getColumnModel().getColumn(1).setMinWidth(0);
        	jtbChannels.getColumnModel().getColumn(1).setPreferredWidth(0);
        	jtbChannels.getColumnModel().getColumn(1).setMaxWidth(0);
        	jtbChannels.getColumnModel().getColumn(2).setMinWidth(0);
        	jtbChannels.getColumnModel().getColumn(2).setPreferredWidth(0);
        	jtbChannels.getColumnModel().getColumn(2).setMaxWidth(0);
        	jtbChannels.getColumnModel().getColumn(3).setMinWidth(28);
        	jtbChannels.getColumnModel().getColumn(3).setPreferredWidth(28);
        	jtbChannels.getColumnModel().getColumn(3).setMaxWidth(28);
        	jtbChannels.getColumnModel().getColumn(4).setMinWidth(28);
        	jtbChannels.getColumnModel().getColumn(4).setPreferredWidth(28);
        	jtbChannels.getColumnModel().getColumn(4).setMaxWidth(28);
        }

        jtbChannels.getSelectionModel().addListSelectionListener(e -> {
            if (jtbChannels.getSelectedRow() != -1) {
                onSelectChannelsRow();
            }
        });

        jpmChannels = new JPopupMenu();

        jmiAddToUser = new JMenuItem(acAddToUser);
        jmiAddToUser.setToolTipText("");
        jpmChannels.add(jmiAddToUser);

        jpmChannels.add(new JSeparator());

        jmiAddChannel = new JMenuItem(acAddChannel);
        jmiAddChannel.setToolTipText("");
        jpmChannels.add(jmiAddChannel);

        jmiEdtChannel = new JMenuItem(acEdtChannel);
        jmiEdtChannel.setToolTipText("");
        jpmChannels.add(jmiEdtChannel);

        jmiDelChannel = new JMenuItem(acDelChannel);
        jmiDelChannel.setToolTipText("");
        jpmChannels.add(jmiDelChannel);

        jtbChannels.setComponentPopupMenu(jpmChannels);

        jspLeft.setViewportView(jtbChannels);
        jpnLeft.add(jspLeft, BorderLayout.CENTER);
        jslChannelsList.setLeftComponent(jpnLeft);

        //Right

        jpnRight = new JPanel();
        jpnRight.setBorder(null);
        jpnRight.setLayout(new BorderLayout());

        jtbrUserChannels = new JToolBar();
        jtbrUserChannels.setBorder(BorderFactory.createEtchedBorder());
        jtbrUserChannels.setFloatable(false);

        jbtDelFromUser = new JButton();
        jbtDelFromUser.setAction(acDelFromUser);
        jbtDelFromUser.setText("");
        jbtDelFromUser.setFocusable(false);
        jbtDelFromUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelFromUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrUserChannels.add(jbtDelFromUser);

        jbtDelAllFromUser = new JButton();
        jbtDelAllFromUser.setAction(acDelAllFromUser);
        jbtDelAllFromUser.setText("");
        jbtDelAllFromUser.setFocusable(false);
        jbtDelAllFromUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelAllFromUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrUserChannels.add(jbtDelAllFromUser);

        jtbrUserChannels.add(new JToolBar.Separator());

        jbtEdtUsrChannel = new JButton();
        jbtEdtUsrChannel.setAction(acEdtUsrChannel);
        jbtEdtUsrChannel.setText("");
        jbtEdtUsrChannel.setFocusable(false);
        jbtEdtUsrChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtEdtUsrChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrUserChannels.add(jbtEdtUsrChannel);

        jtbrUserChannels.add(new JToolBar.Separator());

        jtfCorrection = new JTextField();
        jtfCorrection.setActionCommand("cmd_Correction");
        jtfCorrection.setHorizontalAlignment(JTextField.RIGHT);
        jtfCorrection.setText("0");
        jtfCorrection.setToolTipText(Messages.getString("StrlbCorrection"));
        jtfCorrection.setPreferredSize(new Dimension(60, 28));
        jtfCorrection.setMaximumSize(new Dimension(60, 28));
        jtfCorrection.setMinimumSize(new Dimension(60, 28));
        jtbrUserChannels.add(jtfCorrection);

        jbtSetCorrection = new JButton();
        jbtSetCorrection.setAction(acSetCorrection);
        jbtSetCorrection.setText("");
        jbtSetCorrection.setFocusable(false);
        jbtSetCorrection.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtSetCorrection.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrUserChannels.add(jbtSetCorrection);

        jtbrUserChannels.setPreferredSize(new Dimension(300, 30));

        jpnRight.add(jtbrUserChannels, BorderLayout.PAGE_START);

        //Right table

        jspRight = new JScrollPane();
        jtbUserChannels = new JTable();
        jtbUserChannels.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                if (jtbChannels.getSelectedRow() != -1) {
                    onSelectChannelsRow();
                }
        		if (e.getClickCount() == 2) {
        			acEdtUsrChannel.onExecute();		
        		}	
        	}
        });
        userChannelsModel = new DBTableModelUserChannels(DBUtils.SQL_USERCHANNELS);
        jtbUserChannels.setModel(userChannelsModel);

        jtbUserChannels.setFillsViewportHeight(true);
        jtbUserChannels.setFocusable(false);
        jtbUserChannels.setRowHeight(28);
        jtbUserChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbUserChannels.setShowHorizontalLines(true);
        jtbUserChannels.setShowVerticalLines(false);
        jtbUserChannels.getTableHeader().setResizingAllowed(false);
        jtbUserChannels.getTableHeader().setReorderingAllowed(false);
        jtbUserChannels.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbUserChannels.setDefaultRenderer(Object.class, new DBTableRenderUserChannels());
        jtbUserChannels.setGridColor(Color.LIGHT_GRAY);
        jtbUserChannels.setIntercellSpacing(new Dimension(0, 1));

        DefaultTableCellRenderer centerRenderer = new DBTableRenderUserChannels();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        if (jtbUserChannels.getColumnModel().getColumnCount() > 0) {
        	jtbUserChannels.getColumnModel().getColumn(0).setMinWidth(0);
        	jtbUserChannels.getColumnModel().getColumn(0).setPreferredWidth(0);
        	jtbUserChannels.getColumnModel().getColumn(0).setMaxWidth(0);
        	jtbUserChannels.getColumnModel().getColumn(1).setMinWidth(0);
        	jtbUserChannels.getColumnModel().getColumn(1).setPreferredWidth(0);
        	jtbUserChannels.getColumnModel().getColumn(1).setMaxWidth(0);
        	jtbUserChannels.getColumnModel().getColumn(2).setMinWidth(28);
        	jtbUserChannels.getColumnModel().getColumn(2).setPreferredWidth(28);
        	jtbUserChannels.getColumnModel().getColumn(2).setMaxWidth(28);
        	jtbUserChannels.getColumnModel().getColumn(4).setMinWidth(70);
        	jtbUserChannels.getColumnModel().getColumn(4).setPreferredWidth(70);
        	jtbUserChannels.getColumnModel().getColumn(4).setMaxWidth(70);

        	jtbUserChannels.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        }

        jpmUserChannels = new JPopupMenu();

        jmiDelFromUser = new JMenuItem(acDelFromUser);
        jmiDelFromUser.setToolTipText("");
        jpmUserChannels.add(jmiDelFromUser);

        jpmUserChannels.add(new JSeparator());

        jmiEdtUsrChannel = new JMenuItem(acEdtUsrChannel);
        jmiEdtUsrChannel.setToolTipText("");
        jpmUserChannels.add(jmiEdtUsrChannel);

        jtbUserChannels.setComponentPopupMenu(jpmUserChannels);

        jspRight.setViewportView(jtbUserChannels);
        jpnRight.add(jspRight, BorderLayout.CENTER);
        jslChannelsList.setRightComponent(jpnRight);

        getContentPane().add(jslChannelsList, BorderLayout.CENTER);

        //Bottom

        jpnButtons = new JPanel();
        jpnButtons.setBorder(null);
        jpnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        jbtSave = new JButton();
        jbtSave.setAction(acSave);
        jpnButtons.add(jbtSave);

        getContentPane().add(jpnButtons, BorderLayout.PAGE_END);

        refreshTableChannels(0);
        refreshTableUserChannels(0);

        setSize(new Dimension(723, 563));
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(jbtSave);
    	
    }

    public int getModalResult() {
        return modalResult;
    }

    private void onSelectChannelsRow() {
        int row1 = jtbChannels.getSelectedRow();
        TableModel tm = jtbChannels.getModel();        
        if (row1 != -1) {
            Boolean isUse = (Boolean) tm.getValueAt(row1, 3);
            acAddToUser.setEnabled(!isUse);
        }
    }

    private void refreshTableChannels(int row) {
        channelsModel.refreshContent();
        jtbChannels.setVisible(false);
        jtbChannels.setVisible(true);
        if (jtbChannels.getRowCount() != 0) {
            acAddToUser.setEnabled(true);
            acAddAllToUser.setEnabled(true);
            acEdtChannel.setEnabled(true);
            acDelChannel.setEnabled(true);
            acDelAllChannels.setEnabled(true);
            jtbChannels.setRowSelectionInterval(row, row);
        } else {
            acAddToUser.setEnabled(false);
            acAddAllToUser.setEnabled(false);
            acEdtChannel.setEnabled(false);
            acDelChannel.setEnabled(false);
            acDelAllChannels.setEnabled(false);
        }
    }

    private void refreshTableUserChannels(int row) {
        userChannelsModel.refreshContent();
        jtbUserChannels.setVisible(false);
        jtbUserChannels.setVisible(true);
        if (jtbUserChannels.getRowCount() != 0) {
            jtfCorrection.setEnabled(true);
            acDelFromUser.setEnabled(true);
            acDelAllFromUser.setEnabled(true);
            acEdtUsrChannel.setEnabled(true);
            acSetCorrection.setEnabled(true);
            jtbUserChannels.setRowSelectionInterval(row, row);
        } else {
            jtfCorrection.setEnabled(false);
            acDelFromUser.setEnabled(false);
            acDelAllFromUser.setEnabled(false);
            acEdtUsrChannel.setEnabled(false);
            acSetCorrection.setEnabled(false);
        }
    }

    private class ActionAddToUser extends AbstractAction {
    	public ActionAddToUser() {
    		putValue(NAME, Messages.getString("StrTitleAdd"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrTitleAdd"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/add.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row1 = jtbChannels.getSelectedRow();
            TableModel tm = jtbChannels.getModel();
            if (row1 != -1) {
                Integer id = new Integer((String) tm.getValueAt(row1, 0));
                DBParams[] aParams = new DBParams[2];
                aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                aParams[1] = new DBParams(2, id, CommonTypes.DBType.INTEGER);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_ADD_CHANNEL, aParams) != -1) {
                    refreshTableChannels(row1);
                    refreshTableUserChannels(0);
                }
            }  		
		}
    }
    
    private class ActionAddAllToUser extends AbstractAction {
    	public ActionAddAllToUser() {
    		putValue(NAME, Messages.getString("StrActionAddAll"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionAddAll"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/add_all.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row1 = jtbChannels.getSelectedRow();
            if (DBUtils.getExecuteUpdate(DBUtils.SQL_ADD_ALLCHANNELS) != -1) {
                refreshTableChannels(row1);
                refreshTableUserChannels(0);
            }
		}
    }
    
    private class ActionAddChannel extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionAddChannel(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionAddChannel"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionAddChannel"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/add_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			TableModel tm = jtbChannels.getModel();
            EdtChannel edtChannel = new EdtChannel(parent, Messages.getString("StrTitleAdd"));
            edtChannel.setVisible(true);
            if (edtChannel.getModalResult() != 0) {
                DBParams[] aParams = new DBParams[3];
                aParams[0] = new DBParams(1, new Integer(edtChannel.getIndex()), CommonTypes.DBType.INTEGER);
                aParams[1] = new DBParams(2, edtChannel.getCName(), CommonTypes.DBType.STRING);
                aParams[2] = new DBParams(3, edtChannel.getIcon(), CommonTypes.DBType.STRING);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_INS_CHANNEL, aParams) != -1) {
                    refreshTableChannels(tm.getRowCount());
                }
            }  
		}
    }
    
    private class ActionEdtChannel extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionEdtChannel(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrTitleEdtChannels"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrTitleEdtChannels"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/edt_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row1 = jtbChannels.getSelectedRow();
            TableModel tm = jtbChannels.getModel();
            if (row1 != -1) {
                Integer id = new Integer((String) tm.getValueAt(row1, 0));
                EdtChannel edtChannel = new EdtChannel(parent, Messages.getString("StrTitleEdt"));
                edtChannel.setIndex((String) tm.getValueAt(row1, 1));
                edtChannel.setCName((String) tm.getValueAt(row1, 5));
                edtChannel.setIcon((String) tm.getValueAt(row1, 2));
                edtChannel.setVisible(true);
                if (edtChannel.getModalResult() != 0) {
                    DBParams[] aParams = new DBParams[4];
                    aParams[0] = new DBParams(1, new Integer(edtChannel.getIndex()), CommonTypes.DBType.INTEGER);
                    aParams[1] = new DBParams(2, edtChannel.getCName(), CommonTypes.DBType.STRING);
                    aParams[2] = new DBParams(3, edtChannel.getIcon(), CommonTypes.DBType.STRING);
                    aParams[3] = new DBParams(4, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_EDT_CHANNEL, aParams) != -1) {
                        refreshTableChannels(row1);
                    }
                }
            } 
		}
    	
    }
    
    private class ActionDelChannel extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionDelChannel(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionDelChannel"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionDelChannel"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/del_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row1 = jtbChannels.getSelectedRow();
            TableModel tm = jtbChannels.getModel();
            if (row1 != -1) {
                Integer id = new Integer((String) tm.getValueAt(row1, 0));
                if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDel"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                    DBParams[] aParams = new DBParams[1];
                    aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DEL_CHANNELS, aParams) != -1) {
                        if (row1 != 0) {row1--;}
                        refreshTableChannels(row1);
                        refreshTableUserChannels(0);
                    }
                }
            }  	
		}
    }
    
    private class ActionDelAllChannels extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionDelAllChannels(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionDelChnAll"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionDelChnAll"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/del_all_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDelAll"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                if (DBUtils.getExecuteUpdate(DBUtils.SQL_DEL_ALLCHANNELS) != -1) {
                    refreshTableChannels(0);
                    refreshTableUserChannels(0);
                }
            } 
		}
    }
    
    private class ActionUpdateChannels extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionUpdateChannels(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrTitleUpdChannels"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrTitleUpdChannels"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/update_channel.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			UpdateForm updateForm = new UpdateForm(parent, true);
			updateForm.setVisible(true);
            if (updateForm.getModalResult() != 0) {
                refreshTableChannels(0);
            }
		}
    }
    
    private class ActionUpdateIcons extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionUpdateIcons(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrTitleUpdIcons"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrTitleUpdIcons"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/update_icons.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			UpdateForm updateForm = new UpdateForm(parent, false);
			updateForm.setVisible(true);
            if (updateForm.getModalResult() != 0) {
                refreshTableChannels(0);
            }
		}
    }    

    private class ActionDelFromUser extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionDelFromUser(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionDel"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionDel"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/del.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row1 = jtbChannels.getSelectedRow();
            int row2 = jtbUserChannels.getSelectedRow();
            TableModel tm = jtbUserChannels.getModel();
            if (row2 != -1) {
                Integer id = new Integer((String) tm.getValueAt(row2, 0));
                if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDelUsr"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                    DBParams[] aParams = new DBParams[1];
                    aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DEL_USERCHANNELS, aParams) != -1) {
                        if (row2 != 0) {row2--;}
                        refreshTableChannels(row1);
                        refreshTableUserChannels(row2);
                    }
                }
            } 
		}
    }
    
    private class ActionDelAllFromUser extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionDelAllFromUser(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionDelAll"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionDelAll"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/del_all.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row1 = jtbChannels.getSelectedRow();
            if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDelUsrAll"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                if (DBUtils.getExecuteUpdate(DBUtils.SQL_DEL_ALLUSERCHANNELS) != -1) {
                    refreshTableChannels(row1);
                    refreshTableUserChannels(0);
                }
            }
		}
    }
    
    private class ActionEdtUsrChannel extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionEdtUsrChannel(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrTitleEdt"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrTitleEdt"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/usr_channel_edt.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row2 = jtbUserChannels.getSelectedRow();
            TableModel tm = jtbUserChannels.getModel();
            if (row2 != -1) {
                Integer id = new Integer((String) tm.getValueAt(row2, 0));
                EdtUsrChannel edtUsrChannel = new EdtUsrChannel(parent);
                edtUsrChannel.setCName((String) tm.getValueAt(row2, 3));
                edtUsrChannel.setIcon((String) tm.getValueAt(row2, 1));
                edtUsrChannel.setCorrection((String) tm.getValueAt(row2, 4));
                edtUsrChannel.setVisible(true);
                if (edtUsrChannel.getModalResult() != 0) {
                    DBParams[] aParams = new DBParams[4];
                    aParams[0] = new DBParams(1, edtUsrChannel.getCName(), CommonTypes.DBType.STRING);
                    aParams[1] = new DBParams(2, edtUsrChannel.getIcon(), CommonTypes.DBType.STRING);
                    aParams[2] = new DBParams(3, new Integer(edtUsrChannel.getCorrection()), CommonTypes.DBType.INTEGER);
                    aParams[3] = new DBParams(4, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_EDT_USERCHANNELS, aParams) != -1) {
                        refreshTableUserChannels(row2);
                    }
                }
            } 
		}
    }
    
    private class ActionSetCorrection extends AbstractAction {
    	public ActionSetCorrection() {
    		putValue(NAME, Messages.getString("StrActionSetCorrection"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionSetCorrection"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/set_correction.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row2 = jtbUserChannels.getSelectedRow();
            Integer correction = new Integer(jtfCorrection.getText());
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, correction, CommonTypes.DBType.INTEGER);
            if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SET_CORRECTIONUSERCHANNELS, aParams) != -1) {
                refreshTableUserChannels(row2);	
            }
		}
    }
    
    private class ActionSave extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionSave(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrBtSave"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrBtSave"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/savexmltv.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
    		modalResult = 1;
    		parent.setVisible(false);
    		parent.dispose();			
		}
    }
    
}