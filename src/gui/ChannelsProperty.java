package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.table.TableModel;

import common.DBParams;
import common.DBTableModel;
import common.DBTableModelChannels;
import common.DBTableModelFavoritesChannels;
import common.DBTableRender;
import common.DBTableRenderFavoritesChannels;
import common.DBUtils;
import common.CommonTypes;

@SuppressWarnings("serial")
public class ChannelsProperty extends JDialog {
    private int modalResult = 0;
    private ActionAddToFav acAddToFav;    
    private ActionAddAllToFav acAddAllToFav;    
    private ActionAddChannel acAddChannel;    
    private ActionEdtChannel acEdtChannel;    
    private ActionDelChannel acDelChannel;    
    private ActionDelAllChannels acDelAllChannels;   
    private ActionUpdateChannels acUpdateChannels;    
    private ActionUpdateIcons acUpdateIcons;    
    private ActionDelFromFav acDelFromFav;    
    private ActionDelAllFromFav acDelAllFromFav;    
    private ActionEdtFavChannel acEdtFavChannel;   
    private ActionSetCorrection acSetCorrection;    
    private ActionSave acSave;   
    private JTable jtbChannels;
    private JTable jtbFavoritesChannels;
    private JTextField jtfCorrection;
    private JComboBox<String> jcbLang;

    public ChannelsProperty(Frame owner) {
        super(owner);        
        initActions();
        initGUI();
    }
    
    private void initActions() {
    	acAddToFav = new ActionAddToFav();
        acAddAllToFav = new ActionAddAllToFav();
        acAddChannel = new ActionAddChannel(this);
        acEdtChannel = new ActionEdtChannel(this);
        acDelChannel = new ActionDelChannel(this);
        acDelAllChannels = new ActionDelAllChannels(this);
        acUpdateChannels = new ActionUpdateChannels(this);
        acUpdateIcons = new ActionUpdateIcons(this);        
        acDelFromFav = new ActionDelFromFav(this);       
        acDelAllFromFav = new ActionDelAllFromFav(this);        
        acEdtFavChannel = new ActionEdtFavChannel(this);        
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
        
        jcbLang = new JComboBox<>();
        jcbLang.setModel(new DefaultComboBoxModel<String>(new String[] {Messages.getString("StrLangAll"), Messages.getString("StrLangRus"), Messages.getString("StrLangUkr")}));
        jcbLang.setSelectedIndex(0);
        jcbLang.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				onChangeLang();			
			}
		});
        jtbrChannels.add(jcbLang);

        JButton jbtAddToFav = new JButton();
        jbtAddToFav.setAction(acAddToFav);
        jbtAddToFav.setText("");
        jbtAddToFav.setFocusable(false);
        jbtAddToFav.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddToFav.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddToFav);

        JButton jbtAddAllToFav = new JButton();
        jbtAddAllToFav.setAction(acAddAllToFav);
        jbtAddAllToFav.setText("");
        jbtAddAllToFav.setFocusable(false);
        jbtAddAllToFav.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtAddAllToFav.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrChannels.add(jbtAddAllToFav);

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

        JMenuItem jmiAddToFav = new JMenuItem(acAddToFav);
        jmiAddToFav.setToolTipText("");
        jpmChannels.add(jmiAddToFav);

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

        JToolBar jtbrFavChannels = new JToolBar();
        jtbrFavChannels.setBorder(BorderFactory.createEtchedBorder());
        jtbrFavChannels.setFloatable(false);

        JButton jbtDelFromFav = new JButton();
        jbtDelFromFav.setAction(acDelFromFav);
        jbtDelFromFav.setText("");
        jbtDelFromFav.setFocusable(false);
        jbtDelFromFav.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelFromFav.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrFavChannels.add(jbtDelFromFav);

        JButton jbtDelAllFromFav = new JButton();
        jbtDelAllFromFav.setAction(acDelAllFromFav);
        jbtDelAllFromFav.setText("");
        jbtDelAllFromFav.setFocusable(false);
        jbtDelAllFromFav.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtDelAllFromFav.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrFavChannels.add(jbtDelAllFromFav);

        jtbrFavChannels.add(new JToolBar.Separator());

        JButton jbtEdtFavChannel = new JButton();
        jbtEdtFavChannel.setAction(acEdtFavChannel);
        jbtEdtFavChannel.setText("");
        jbtEdtFavChannel.setFocusable(false);
        jbtEdtFavChannel.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtEdtFavChannel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrFavChannels.add(jbtEdtFavChannel);

        jtbrFavChannels.add(new JToolBar.Separator());

        jtfCorrection = new JTextField();
        jtfCorrection.setActionCommand("cmd_Correction");
        jtfCorrection.setHorizontalAlignment(JTextField.RIGHT);
        jtfCorrection.setText("0");
        jtfCorrection.setToolTipText(Messages.getString("StrLbCorrection"));
        jtfCorrection.setPreferredSize(new Dimension(60, 28));
        jtfCorrection.setMaximumSize(new Dimension(60, 28));
        jtfCorrection.setMinimumSize(new Dimension(60, 28));
        jtbrFavChannels.add(jtfCorrection);

        JButton jbtSetCorrection = new JButton();
        jbtSetCorrection.setAction(acSetCorrection);
        jbtSetCorrection.setText("");
        jbtSetCorrection.setFocusable(false);
        jbtSetCorrection.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtSetCorrection.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrFavChannels.add(jbtSetCorrection);

        jtbrFavChannels.setPreferredSize(new Dimension(300, 30));

        jpnRight.add(jtbrFavChannels, BorderLayout.PAGE_START);

        //Right table

        JScrollPane jspRight = new JScrollPane();
        
        jtbFavoritesChannels = new JTable();
                
        DBTableModelFavoritesChannels favoritesChannelsModel = new DBTableModelFavoritesChannels(DBUtils.SQL_FAVORITES_CHANNELS);
        
        jtbFavoritesChannels.setModel(favoritesChannelsModel);
        CommonTypes.setTableProperties(jtbFavoritesChannels);
        jtbFavoritesChannels.setDefaultRenderer(String.class, new DBTableRenderFavoritesChannels());
        jtbFavoritesChannels.setDefaultRenderer(Integer.class, new DBTableRenderFavoritesChannels());

        jtbFavoritesChannels.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                if (jtbChannels.getSelectedRow() != -1) {
                    onSelectChannelsRow();
                }
        		if (e.getClickCount() == 2) {
        			acEdtFavChannel.onExecute();		
        		}	
        	}
        });

        JPopupMenu jpmFavoritesChannels = new JPopupMenu();

        JMenuItem jmiDelFromFav = new JMenuItem(acDelFromFav);
        jmiDelFromFav.setToolTipText("");
        jpmFavoritesChannels.add(jmiDelFromFav);

        jpmFavoritesChannels.add(new JSeparator());

        JMenuItem jmiEdtFavChannel = new JMenuItem(acEdtFavChannel);
        jmiEdtFavChannel.setToolTipText("");
        jpmFavoritesChannels.add(jmiEdtFavChannel);

        jtbFavoritesChannels.setComponentPopupMenu(jpmFavoritesChannels);

        jspRight.setViewportView(jtbFavoritesChannels);
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

        refreshTableChannels(0, jcbLang.getSelectedIndex());
        refreshTableFavChannels(0);

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
    
    private void onChangeLang() {
    	int selectedIndex = jcbLang.getSelectedIndex();
    	refreshTableChannels(0, selectedIndex);   	
    }

    private void onSelectChannelsRow() {
        int row = jtbChannels.getSelectedRow();
        TableModel tm = jtbChannels.getModel();        
        if (row != -1) {
            Object fav = tm.getValueAt(row, DBUtils.INDEX_IS_FAV);
            acAddToFav.setEnabled(fav == null);
        }
    }

    private void refreshTableChannels(int row, int filter) {
    	DBTableModel tm = (DBTableModel) jtbChannels.getModel();
    	String sflString = "all";
    	try {
	    	if (filter == 1) {
	    		sflString = "rus";
	    	}
	    	if (filter == 2) {
	    		sflString = "ukr";
	    	}
	    	DBParams[] aParams = new DBParams[2];
	        aParams[0] = new DBParams(1, sflString, CommonTypes.DBType.STRING);
	        aParams[1] = new DBParams(2, sflString, CommonTypes.DBType.STRING);
	        tm.refreshContentForParams(aParams);	
    	} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
        jtbChannels.setVisible(false);
        jtbChannels.setVisible(true);
        if (jtbChannels.getRowCount() != 0) {
            acAddToFav.setEnabled(true);
            acAddAllToFav.setEnabled(true);
            acEdtChannel.setEnabled(true);
            acDelChannel.setEnabled(true);
            acDelAllChannels.setEnabled(true);
            jtbChannels.setRowSelectionInterval(row, row);
        } else {
            acAddToFav.setEnabled(false);
            acAddAllToFav.setEnabled(false);
            acEdtChannel.setEnabled(false);
            acDelChannel.setEnabled(false);
            acDelAllChannels.setEnabled(false);
        }
    }

    private void refreshTableFavChannels(int row) {
    	DBTableModel tm = (DBTableModel) jtbFavoritesChannels.getModel();
    	try {
        	tm.refreshContent();
	    } catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
    	jtbFavoritesChannels.setVisible(false);
    	jtbFavoritesChannels.setVisible(true);
        if (jtbFavoritesChannels.getRowCount() != 0) {
            jtfCorrection.setEnabled(true);
            acDelFromFav.setEnabled(true);
            acDelAllFromFav.setEnabled(true);
            acEdtFavChannel.setEnabled(true);
            acSetCorrection.setEnabled(true);
            jtbFavoritesChannels.setRowSelectionInterval(row, row);
        } else {
            jtfCorrection.setEnabled(false);
            acDelFromFav.setEnabled(false);
            acDelAllFromFav.setEnabled(false);
            acEdtFavChannel.setEnabled(false);
            acSetCorrection.setEnabled(false);
        }
    }

    private class ActionAddToFav extends AbstractAction {
    	
    	public ActionAddToFav() {
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
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_CHANNEL));
                DBParams[] aParams = new DBParams[2];
                aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                aParams[1] = new DBParams(2, id, CommonTypes.DBType.INTEGER);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_ADD_CHANNEL, aParams) != -1) {
                    refreshTableChannels(row, jcbLang.getSelectedIndex());
                    refreshTableFavChannels(0);
                }
            }  		
		}
    }
    
    private class ActionAddAllToFav extends AbstractAction {
    	
    	public ActionAddAllToFav() {
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
                refreshTableChannels(row, jcbLang.getSelectedIndex());
                refreshTableFavChannels(0);
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
                DBParams[] aParams = new DBParams[4];
                aParams[0] = new DBParams(1, new Integer(edtChannel.getIndex()), CommonTypes.DBType.INTEGER);
                aParams[1] = new DBParams(2, edtChannel.getCName(), CommonTypes.DBType.STRING);
                aParams[2] = new DBParams(3, edtChannel.getIcon(), CommonTypes.DBType.STRING);
                aParams[3] = new DBParams(4, edtChannel.getLang(), CommonTypes.DBType.STRING);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_INS_CHANNEL, aParams) != -1) {
                    refreshTableChannels(tm.getRowCount(), jcbLang.getSelectedIndex());
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
                edtChannel.setIndex((String) tm.getValueAt(row, DBUtils.INDEX_CHANNEL));
                edtChannel.setCName((String) tm.getValueAt(row, DBUtils.INDEX_CHANNEL_NAME));
                edtChannel.setIcon((String) tm.getValueAt(row, DBUtils.INDEX_CHANNEL_ICON_STR));
                edtChannel.setLang((String) tm.getValueAt(row, DBUtils.INDEX_LANG));
                edtChannel.setVisible(true);
                if (edtChannel.getModalResult() != 0) {
                    DBParams[] aParams = new DBParams[5];
                    aParams[0] = new DBParams(1, new Integer(edtChannel.getIndex()), CommonTypes.DBType.INTEGER);
                    aParams[1] = new DBParams(2, edtChannel.getCName(), CommonTypes.DBType.STRING);
                    aParams[2] = new DBParams(3, edtChannel.getIcon(), CommonTypes.DBType.STRING);
                    aParams[3] = new DBParams(4, edtChannel.getLang(), CommonTypes.DBType.STRING);
                    aParams[4] = new DBParams(5, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_EDT_CHANNEL, aParams) != -1) {
                        refreshTableChannels(row, jcbLang.getSelectedIndex());
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
                        refreshTableChannels(row, jcbLang.getSelectedIndex());
                        refreshTableFavChannels(0);
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
                    refreshTableChannels(0, jcbLang.getSelectedIndex());
                    refreshTableFavChannels(0);
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
                refreshTableChannels(0, jcbLang.getSelectedIndex());
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
                refreshTableChannels(0, jcbLang.getSelectedIndex());
            }
		}
    }    

    private class ActionDelFromFav extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionDelFromFav(Dialog parent) {
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
            int row = jtbFavoritesChannels.getSelectedRow();
            TableModel tm = jtbFavoritesChannels.getModel();
            if (row != -1) {
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
                if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDelFav"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                    DBParams[] aParams = new DBParams[1];
                    aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DEL_FAVCHANNELS, aParams) != -1) {
                        if (row != 0) {row--;}
                        refreshTableChannels(crow, jcbLang.getSelectedIndex());
                        refreshTableFavChannels(row);
                    }
                }
            } 
		}
    }
    
    private class ActionDelAllFromFav extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionDelAllFromFav(Dialog parent) {
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
            if (JOptionPane.showConfirmDialog(parent, Messages.getString("StrConfirmDelFavAll"), Messages.getString("StrTitleDel"), JOptionPane.YES_NO_OPTION) == 0) {
                if (DBUtils.getExecuteUpdate(DBUtils.SQL_DEL_ALLFAVCHANNELS) != -1) {
                    refreshTableChannels(row, jcbLang.getSelectedIndex());
                    refreshTableFavChannels(0);
                }
            }
		}
    }
    
    private class ActionEdtFavChannel extends AbstractAction {
    	private Dialog parent;
    	
    	public ActionEdtFavChannel(Dialog parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrTitleEdt"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrTitleEdt"));
            putValue(SMALL_ICON, new ImageIcon(ChannelsProperty.class.getResource("/resources/fav_channel_edt.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
			int row = jtbFavoritesChannels.getSelectedRow();
            TableModel tm = jtbFavoritesChannels.getModel();
            if (row != -1) {
                Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
                EdtFavChannel edtFavChannel = new EdtFavChannel(parent);
                edtFavChannel.setCName((String) tm.getValueAt(row, DBUtils.INDEX_FCHANNEL_NAME));
                edtFavChannel.setIcon((String) tm.getValueAt(row, DBUtils.INDEX_FCHANNEL_ICON_STR));
                edtFavChannel.setCorrection((String) tm.getValueAt(row, DBUtils.INDEX_FCHANNEL_CORRECTION));
                edtFavChannel.setVisible(true);
                if (edtFavChannel.getModalResult() != 0) {
                    DBParams[] aParams = new DBParams[4];
                    aParams[0] = new DBParams(1, edtFavChannel.getCName(), CommonTypes.DBType.STRING);
                    aParams[1] = new DBParams(2, edtFavChannel.getIcon(), CommonTypes.DBType.STRING);
                    aParams[2] = new DBParams(3, new Integer(edtFavChannel.getCorrection()), CommonTypes.DBType.INTEGER);
                    aParams[3] = new DBParams(4, id, CommonTypes.DBType.INTEGER);
                    if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_EDT_FAVCHANNELS, aParams) != -1) {
                        refreshTableFavChannels(row);
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
			int row = jtbFavoritesChannels.getSelectedRow();
            Integer correction = new Integer(jtfCorrection.getText());
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, correction, CommonTypes.DBType.INTEGER);
            if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SET_CORRECTIONFAVCHANNELS, aParams) != -1) {
                refreshTableFavChannels(row);	
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