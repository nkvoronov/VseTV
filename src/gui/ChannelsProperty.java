package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableModel;

import common.DBParams;
import common.DBTableModel;
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
    private JTable jtbChannels;
    private JTable jtbUserChannels;
    private JTextField jtfCorrection;

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

        JSplitPane jslChannelsList = new JSplitPane();
        jslChannelsList.setBorder(null);
        jslChannelsList.setDividerLocation(360);
        jslChannelsList.setDividerSize(3);
        jslChannelsList.setAutoscrolls(true);

        //Left

        JPanel jpnLeft = new JPanel();
        jpnLeft.setBorder(null);
        jpnLeft.setLayout(new BorderLayout());

        JToolBar jtbrChannels = new JToolBar();
        jtbrChannels.setBorder(BorderFactory.createEtchedBorder());
        jtbrChannels.setFloatable(false);

        JButton jbtAddToUser = new JButton();
        jbtAddToUser.setAction(acAddToUser);
        jbtAddToUser.setText("");
        jbtAddToUser.setFocusable(false);
        jbtAddToUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddToUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddToUser);

        JButton jbtAddAllToUser = new JButton();
        jbtAddAllToUser.setAction(acAddAllToUser);
        jbtAddAllToUser.setText("");
        jbtAddAllToUser.setFocusable(false);
        jbtAddAllToUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddAllToUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddAllToUser);

        jtbrChannels.add(new JToolBar.Separator());

        JButton jbtAddChannel = new JButton();
        jbtAddChannel.setAction(acAddChannel);
        jbtAddChannel.setText("");
        jbtAddChannel.setFocusable(false);
        jbtAddChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddChannel);

        JButton jbtEdtChannel = new JButton();
        jbtEdtChannel.setAction(acEdtChannel);
        jbtEdtChannel.setText("");
        jbtEdtChannel.setFocusable(false);
        jbtEdtChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtEdtChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtEdtChannel);

        JButton jbtDelChannel = new JButton();
        jbtDelChannel.setAction(acDelChannel);
        jbtDelChannel.setText("");
        jbtDelChannel.setFocusable(false);
        jbtDelChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtDelChannel);

        JButton jbtDelAllChannels = new JButton();
        jbtDelAllChannels.setAction(acDelAllChannels);
        jbtDelAllChannels.setText("");
        jbtDelAllChannels.setFocusable(false);
        jbtDelAllChannels.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelAllChannels.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtDelAllChannels);

        jtbrChannels.add(new JToolBar.Separator());

        JButton jbtUpdateChannels = new JButton();
        jbtUpdateChannels.setAction(acUpdateChannels);
        jbtUpdateChannels.setText("");
        jbtUpdateChannels.setFocusable(false);
        jbtUpdateChannels.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtUpdateChannels.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtUpdateChannels);

        JButton jbtUpdateIcons = new JButton();
        jbtUpdateIcons.setAction(acUpdateIcons);
        jbtUpdateIcons.setText("");
        jbtUpdateIcons.setFocusable(false);
        jbtUpdateIcons.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtUpdateIcons.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtUpdateIcons);

        jtbrChannels.setPreferredSize(new Dimension(300, 30));

        jpnLeft.add(jtbrChannels, BorderLayout.PAGE_START);

        //Left table

        JScrollPane jspLeft = new JScrollPane();
        
        jtbChannels = new JTable();
        
        DBTableModelChannels channelsModel = new DBTableModelChannels(DBUtils.SQL_CHANNELS);
        
        jtbChannels.setModel(channelsModel);
        CommonTypes.setTableProperties(jtbChannels);        
        jtbChannels.setDefaultRenderer(Object.class, new DBTableRender()); 
        
        jtbChannels.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
        			acEdtChannel.onExecute();		
        		}	
        	}
        });

        jtbChannels.getSelectionModel().addListSelectionListener(e -> {
            if (jtbChannels.getSelectedRow() != -1) {
                onSelectChannelsRow();
            }
        });

        JPopupMenu jpmChannels = new JPopupMenu();

        JMenuItem jmiAddToUser = new JMenuItem(acAddToUser);
        jmiAddToUser.setToolTipText("");
        jpmChannels.add(jmiAddToUser);

        jpmChannels.add(new JSeparator());

        JMenuItem jmiAddChannel = new JMenuItem(acAddChannel);
        jmiAddChannel.setToolTipText("");
        jpmChannels.add(jmiAddChannel);

        JMenuItem jmiEdtChannel = new JMenuItem(acEdtChannel);
        jmiEdtChannel.setToolTipText("");
        jpmChannels.add(jmiEdtChannel);

        JMenuItem jmiDelChannel = new JMenuItem(acDelChannel);
        jmiDelChannel.setToolTipText("");
        jpmChannels.add(jmiDelChannel);

        jtbChannels.setComponentPopupMenu(jpmChannels);

        jspLeft.setViewportView(jtbChannels);
        jpnLeft.add(jspLeft, BorderLayout.CENTER);
        jslChannelsList.setLeftComponent(jpnLeft);

        //Right

        JPanel jpnRight = new JPanel();
        jpnRight.setBorder(null);
        jpnRight.setLayout(new BorderLayout());

        JToolBar jtbrUserChannels = new JToolBar();
        jtbrUserChannels.setBorder(BorderFactory.createEtchedBorder());
        jtbrUserChannels.setFloatable(false);

        JButton jbtDelFromUser = new JButton();
        jbtDelFromUser.setAction(acDelFromUser);
        jbtDelFromUser.setText("");
        jbtDelFromUser.setFocusable(false);
        jbtDelFromUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelFromUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrUserChannels.add(jbtDelFromUser);

        JButton jbtDelAllFromUser = new JButton();
        jbtDelAllFromUser.setAction(acDelAllFromUser);
        jbtDelAllFromUser.setText("");
        jbtDelAllFromUser.setFocusable(false);
        jbtDelAllFromUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelAllFromUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrUserChannels.add(jbtDelAllFromUser);

        jtbrUserChannels.add(new JToolBar.Separator());

        JButton jbtEdtUsrChannel = new JButton();
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

        JButton jbtSetCorrection = new JButton();
        jbtSetCorrection.setAction(acSetCorrection);
        jbtSetCorrection.setText("");
        jbtSetCorrection.setFocusable(false);
        jbtSetCorrection.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtSetCorrection.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrUserChannels.add(jbtSetCorrection);

        jtbrUserChannels.setPreferredSize(new Dimension(300, 30));

        jpnRight.add(jtbrUserChannels, BorderLayout.PAGE_START);

        //Right table

        JScrollPane jspRight = new JScrollPane();
        
        jtbUserChannels = new JTable();
                
        DBTableModelUserChannels userChannelsModel = new DBTableModelUserChannels(DBUtils.SQL_USERCHANNELS);
        
        jtbUserChannels.setModel(userChannelsModel);
        CommonTypes.setTableProperties(jtbUserChannels);
        jtbUserChannels.setDefaultRenderer(String.class, new DBTableRenderUserChannels());
        jtbUserChannels.setDefaultRenderer(Integer.class, new DBTableRenderUserChannels());

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

        JPopupMenu jpmUserChannels = new JPopupMenu();

        JMenuItem jmiDelFromUser = new JMenuItem(acDelFromUser);
        jmiDelFromUser.setToolTipText("");
        jpmUserChannels.add(jmiDelFromUser);

        jpmUserChannels.add(new JSeparator());

        JMenuItem jmiEdtUsrChannel = new JMenuItem(acEdtUsrChannel);
        jmiEdtUsrChannel.setToolTipText("");
        jpmUserChannels.add(jmiEdtUsrChannel);

        jtbUserChannels.setComponentPopupMenu(jpmUserChannels);

        jspRight.setViewportView(jtbUserChannels);
        jpnRight.add(jspRight, BorderLayout.CENTER);
        jslChannelsList.setRightComponent(jpnRight);

        getContentPane().add(jslChannelsList, BorderLayout.CENTER);

        //Bottom

        JPanel jpnButtons = new JPanel();
        jpnButtons.setBorder(null);
        jpnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));

        JButton jbtSave = new JButton();
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
        int row = jtbChannels.getSelectedRow();
        TableModel tm = jtbChannels.getModel();        
        if (row != -1) {
            Object oUser = tm.getValueAt(row, DBUtils.INDEX_IS_USER);
            acAddToUser.setEnabled(oUser == null);
        }
    }

    private void refreshTableChannels(int row) {
    	DBTableModel tm = (DBTableModel) jtbChannels.getModel();
        tm.refreshContent();
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
    	DBTableModel tm = (DBTableModel) jtbUserChannels.getModel();
        tm.refreshContent();
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
			int row = jtbChannels.getSelectedRow();
            TableModel tm = jtbChannels.getModel();
            if (row != -1) {
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
                DBParams[] aParams = new DBParams[2];
                aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                aParams[1] = new DBParams(2, id, CommonTypes.DBType.INTEGER);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_ADD_CHANNEL, aParams) != -1) {
                    refreshTableChannels(row);
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
			int row = jtbChannels.getSelectedRow();
            if (DBUtils.getExecuteUpdate(DBUtils.SQL_ADD_ALLCHANNELS) != -1) {
                refreshTableChannels(row);
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
			int row = jtbChannels.getSelectedRow();
            TableModel tm = jtbChannels.getModel();
            if (row != -1) {
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
                EdtChannel edtChannel = new EdtChannel(parent, Messages.getString("StrTitleEdt"));
                edtChannel.setIndex((String) tm.getValueAt(row, DBUtils.INDEX_CHANNEL_INDEX));
                edtChannel.setCName((String) tm.getValueAt(row, DBUtils.INDEX_CHANNEL_NAME));
                edtChannel.setIcon((String) tm.getValueAt(row, DBUtils.INDEX_CHANNEL_ICON_STR));
                edtChannel.setVisible(true);
                if (edtChannel.getModalResult() != 0) {
                    DBParams[] aParams = new DBParams[4];
                    aParams[0] = new DBParams(1, new Integer(edtChannel.getIndex()), CommonTypes.DBType.INTEGER);
                    aParams[1] = new DBParams(2, edtChannel.getCName(), CommonTypes.DBType.STRING);
                    aParams[2] = new DBParams(3, edtChannel.getIcon(), CommonTypes.DBType.STRING);
                    aParams[3] = new DBParams(4, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_EDT_CHANNEL, aParams) != -1) {
                        refreshTableChannels(row);
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
			int row = jtbChannels.getSelectedRow();
            TableModel tm = jtbChannels.getModel();
            if (row != -1) {
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
                if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDel"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                    DBParams[] aParams = new DBParams[1];
                    aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DEL_CHANNELS, aParams) != -1) {
                        if (row != 0) {row--;}
                        refreshTableChannels(row);
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
			int crow = jtbChannels.getSelectedRow();
            int row = jtbUserChannels.getSelectedRow();
            TableModel tm = jtbUserChannels.getModel();
            if (row != -1) {
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
                if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDelUsr"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                    DBParams[] aParams = new DBParams[1];
                    aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DEL_USERCHANNELS, aParams) != -1) {
                        if (row != 0) {row--;}
                        refreshTableChannels(crow);
                        refreshTableUserChannels(row);
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
			int row = jtbChannels.getSelectedRow();
            if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDelUsrAll"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                if (DBUtils.getExecuteUpdate(DBUtils.SQL_DEL_ALLUSERCHANNELS) != -1) {
                    refreshTableChannels(row);
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
			int row = jtbUserChannels.getSelectedRow();
            TableModel tm = jtbUserChannels.getModel();
            if (row != -1) {
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
                EdtUsrChannel edtUsrChannel = new EdtUsrChannel(parent);
                edtUsrChannel.setCName((String) tm.getValueAt(row, DBUtils.INDEX_UCHANNEL_NAME));
                edtUsrChannel.setIcon((String) tm.getValueAt(row, DBUtils.INDEX_UCHANNEL_ICON_STR));
                edtUsrChannel.setCorrection((String) tm.getValueAt(row, DBUtils.INDEX_UCHANNEL_CORRECTION));
                edtUsrChannel.setVisible(true);
                if (edtUsrChannel.getModalResult() != 0) {
                    DBParams[] aParams = new DBParams[4];
                    aParams[0] = new DBParams(1, edtUsrChannel.getCName(), CommonTypes.DBType.STRING);
                    aParams[1] = new DBParams(2, edtUsrChannel.getIcon(), CommonTypes.DBType.STRING);
                    aParams[2] = new DBParams(3, new Integer(edtUsrChannel.getCorrection()), CommonTypes.DBType.INTEGER);
                    aParams[3] = new DBParams(4, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_EDT_USERCHANNELS, aParams) != -1) {
                        refreshTableUserChannels(row);
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
			int row = jtbUserChannels.getSelectedRow();
            Integer correction = new Integer(jtfCorrection.getText());
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, correction, CommonTypes.DBType.INTEGER);
            if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SET_CORRECTIONUSERCHANNELS, aParams) != -1) {
                refreshTableUserChannels(row);	
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