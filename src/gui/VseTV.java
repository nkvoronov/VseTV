package gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableModel;
import java.sql.*;
import java.io.File;
import java.util.Objects;

import common.CommonTypes;
import common.DBParams;
import common.DBTableModelMainChannels;
import common.DBTableModelMainSchedule;
import common.DBTableModelOtherSchedule;
import common.DBTableRender;
import common.DBTableRenderMainSchedule;
import common.DBTableRenderOtherSchedule;
import common.DBTableModel;
import common.DBUtils;
import parser.ParserVseTV;

@SuppressWarnings("serial")
public class VseTV  extends JFrame implements ChangeListener {
    private ParserVseTV parser;
    private ActionSaveXmlTV acSaveXmlTV;    
    private ActionExit acExit;
    private ActionChannelsList acChannelsList;
    private ActionOptions acOptions;
    private ActionAbout acAbout;
    private ActionUpdateProgramme acUpdateProgramme;
    private ActionViewToolBar acViewToolBar;
    private ActionViewStatusBar acViewStatusBar; 
    private JToolBar jtbrMain;
    private JCheckBoxMenuItem jcmiViewToolBar;
    private JCheckBoxMenuItem jcmiViewStatuslBar;
    private JTabbedPane jtpMain;
    private JSplitPane jslScheludeAllMain;
    private JTable jtbMainChannels;
    private JTable jtbScheludeAll;
    private JTable jtbScheludeNow;
    private JTable jtbScheludeNext;
    private JTable jtbScheludeReminders;    
    private JTextArea jtaDescription;
    private JPanel jpnStatus;
    private JProgressBar jpbUpdate;
    private JLabel jlbStatus;
    private JSplitPane jslDescription;
    
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        CommonTypes.loadConfigs();
        EventQueue.invokeLater(() -> new VseTV().setVisible(true));	
	}

    public VseTV() {
        parser = new ParserVseTV(CommonTypes.ICON_FOLDER, CommonTypes.LANG, CommonTypes.COUNT_DAY, CommonTypes.FULL_DESC);
        parser.getMonitor().addChangeListener(this);
        
        initActions();
        initGUI();
    }
    
    private void initActions() {
    	acSaveXmlTV = new ActionSaveXmlTV(this);        
        acExit = new ActionExit(this);
        acChannelsList = new ActionChannelsList(this);
        acOptions = new ActionOptions(this);
        acAbout = new ActionAbout(this);
        acUpdateProgramme = new ActionUpdateProgramme();
        acViewToolBar = new ActionViewToolBar();
        acViewStatusBar = new ActionViewStatusBar();		
	}
    
    private void initGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Messages.getString("StrTitleMain"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(VseTV.class.getResource("/resources/main.png")));

        JMenuBar jmbMain = new JMenuBar();

        JMenu jmnFile = new JMenu();
        jmnFile.setMnemonic('\u0424');
        jmnFile.setText(Messages.getString("StrMenuFile"));
        jmnFile.setToolTipText("");

        JMenuItem jmiSaveXmlTV = new JMenuItem();
        jmiSaveXmlTV.setText(Messages.getString("StrActionSaveXML"));
        jmiSaveXmlTV.setAction(acSaveXmlTV);
        jmiSaveXmlTV.setToolTipText("");
        jmnFile.add(jmiSaveXmlTV);
        
        JMenuItem jmiExit = new JMenuItem();
        jmiExit.setText(Messages.getString("StrActionExit"));
        jmiExit.setAction(acExit);
        jmiExit.setToolTipText("");
        jmnFile.add(jmiExit);

        jmbMain.add(jmnFile);

        JMenu jmnView = new JMenu();
        jmnView.setMnemonic('\u0438');
        jmnView.setText(Messages.getString("StrMenuView"));
        jmnView.setToolTipText("");

        jcmiViewToolBar = new JCheckBoxMenuItem();
        jcmiViewToolBar.setText(Messages.getString("StrActionTools"));
        jcmiViewToolBar.setState(true);
        jcmiViewToolBar.setAction(acViewToolBar);
        jcmiViewToolBar.setToolTipText("");
        jmnView.add(jcmiViewToolBar);

        jcmiViewStatuslBar = new JCheckBoxMenuItem();
        jcmiViewStatuslBar.setText(Messages.getString("StrActionStatus"));
        jcmiViewStatuslBar.setState(true);
        jcmiViewStatuslBar.setAction(acViewStatusBar);
        jcmiViewStatuslBar.setToolTipText("");
        jmnView.add(jcmiViewStatuslBar);

        jmbMain.add(jmnView);

        JMenu jmnActions = new JMenu();
        jmnActions.setMnemonic('\u0414');
        jmnActions.setText(Messages.getString("StrMenuExecute"));
        jmnActions.setToolTipText("");

        JMenuItem jmiUpdateProgramme = new JMenuItem();
        jmiUpdateProgramme.setText(Messages.getString("StrActionUpdate"));
        jmiUpdateProgramme.setAction(acUpdateProgramme);
        jmiUpdateProgramme.setToolTipText("");
        jmnActions.add(jmiUpdateProgramme);

        jmbMain.add(jmnActions);

        JMenu jmnSettings = new JMenu();
        jmnSettings.setMnemonic('\u041d');
        jmnSettings.setText(Messages.getString("StrMenuSettings"));
        jmnSettings.setToolTipText("");

        JMenuItem jmiChannelsList = new JMenuItem();
        jmiChannelsList.setText(Messages.getString("StrActionChannels"));
        jmiChannelsList.setAction(acChannelsList);
        jmiChannelsList.setToolTipText("");
        jmnSettings.add(jmiChannelsList);

        JMenuItem jmiOptions = new JMenuItem();
        jmiOptions.setText(Messages.getString("StrActionOptions"));
        jmiOptions.setAction(acOptions);
        jmiChannelsList.setToolTipText("");
        jmnSettings.add(jmiOptions);

        jmbMain.add(jmnSettings);

        JMenu jmnHelp = new JMenu();
        jmnHelp.setMnemonic('\u043e');
        jmnHelp.setText(Messages.getString("StrMenuHelp"));

        JMenuItem jmiAbout = new JMenuItem();
        jmiAbout.setText(Messages.getString("StrActionAbout"));
        jmiAbout.setAction(acAbout);
        jmiAbout.setToolTipText("");
        jmnHelp.add(jmiAbout);

        jmbMain.add(jmnHelp);
        
        setJMenuBar(jmbMain);

        jtbrMain = new JToolBar();
        jtbrMain.setBorder(BorderFactory.createEtchedBorder());
        jtbrMain.setFloatable(false);
        jtbrMain.setRollover(true);

        JButton jbtUpdateProgramme = new JButton();
        jbtUpdateProgramme.setAction(acUpdateProgramme);
        jbtUpdateProgramme.setText("");
        jbtUpdateProgramme.setFocusable(false);
        jbtUpdateProgramme.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtUpdateProgramme.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrMain.add(jbtUpdateProgramme);

        jtbrMain.add(new JToolBar.Separator());

        JButton jbtChannelsList = new JButton();
        jbtChannelsList.setAction(acChannelsList);
        jbtChannelsList.setText("");
        jbtChannelsList.setFocusable(false);
        jbtChannelsList.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtChannelsList.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrMain.add(jbtChannelsList);

        JButton jbtOptions = new JButton();
        jbtOptions.setAction(acOptions);
        jbtOptions.setText("");
        jbtOptions.setFocusable(false);
        jbtOptions.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtOptions.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrMain.add(jbtOptions);

        jtbrMain.add(new JToolBar.Separator());

        getContentPane().add(jtbrMain, BorderLayout.PAGE_START);
        
        JSplitPane jslMain = new JSplitPane();
        jslMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jslMain.setBorder(null);
        jslMain.setDividerLocation(360);
        jslMain.setDividerSize(3);
        jslMain.setAutoscrolls(true);

        jtpMain = new JTabbedPane();

        JPanel jpnScheludeAll = new JPanel();
        jpnScheludeAll.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageProgramme"), jpnScheludeAll);

        jslScheludeAllMain = new javax.swing.JSplitPane();
        jslScheludeAllMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jslScheludeAllMain.setBorder(null);
        jslScheludeAllMain.setDividerLocation(200);
        jslScheludeAllMain.setDividerSize(3);
        jslScheludeAllMain.setAutoscrolls(true);

        jpnScheludeAll.add(jslScheludeAllMain, BorderLayout.CENTER);

        jtbMainChannels = new JTable();
        
        DBTableModelMainChannels mainChannelsModel = new DBTableModelMainChannels(DBUtils.SQL_MAINUSERCHANNELS);
        
        jtbMainChannels.setModel(mainChannelsModel);
        CommonTypes.setTableProperties(jtbMainChannels);
        jtbMainChannels.setDefaultRenderer(Object.class, new DBTableRender());

        jtbMainChannels.getSelectionModel().addListSelectionListener(e -> {
            if (jtbMainChannels.getSelectedRow() != -1) {
                onSelectChannelsRow();
            }
        });

        JScrollPane jspUserChannels = new JScrollPane();
        jspUserChannels.setViewportView(jtbMainChannels);
        jslScheludeAllMain.setLeftComponent(jspUserChannels);        

        JScrollPane jspScheludeAll = new JScrollPane();        
        
        jtbScheludeAll = new JTable();
        
        DBTableModelMainSchedule scheludeAllModel = new DBTableModelMainSchedule(DBUtils.SQL_MAINSCHEDULE);
        
        jtbScheludeAll.setModel(scheludeAllModel);
        CommonTypes.setTableProperties(jtbScheludeAll);
        jtbScheludeAll.setDefaultRenderer(String.class, new DBTableRenderMainSchedule());
        jtbScheludeAll.setDefaultRenderer(Integer.class, new DBTableRenderMainSchedule());
                                
        jtbScheludeAll.getSelectionModel().addListSelectionListener(e -> {
            if (jtbScheludeAll.getSelectedRow() != -1) {
                onSelectScheludeRow(jtbScheludeAll);
            }
        });
                                
        jspScheludeAll.setViewportView(jtbScheludeAll);
        jslScheludeAllMain.setRightComponent(jspScheludeAll);

        JPanel jpnScheludeNow = new JPanel();
        jpnScheludeNow.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageNow"), jpnScheludeNow);
        
        JScrollPane jspScheludeNow = new JScrollPane();
        jpnScheludeNow.add(jspScheludeNow, BorderLayout.CENTER);
        
        jtbScheludeNow = new JTable();
        
        DBTableModelOtherSchedule scheludeNowModel = new DBTableModelOtherSchedule(DBUtils.SQL_NOWSCHEDULE);
        
        jtbScheludeNow.setModel(scheludeNowModel);
        CommonTypes.setTableProperties(jtbScheludeNow);
        jtbScheludeNow.setDefaultRenderer(String.class, new DBTableRenderOtherSchedule());
        jtbScheludeNow.setDefaultRenderer(Integer.class, new DBTableRenderOtherSchedule());

        jtbScheludeNow.getSelectionModel().addListSelectionListener(e -> {
            if (jtbScheludeNow.getSelectedRow() != -1) {
                onSelectScheludeRow(jtbScheludeNow);
            }
        });
               
        jspScheludeNow.setViewportView(jtbScheludeNow);

        JPanel jpnScheludeNext = new JPanel();
        jpnScheludeNext.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageNext"), jpnScheludeNext);
        
        JScrollPane jspScheludeNext = new JScrollPane();
        jpnScheludeNext.add(jspScheludeNext, BorderLayout.CENTER);
        
        jtbScheludeNext = new JTable();
        
        DBTableModelOtherSchedule scheludeNextModel = new DBTableModelOtherSchedule(DBUtils.SQL_NEXTSCHEDULE);
        
        jtbScheludeNext.setModel(scheludeNextModel);
        CommonTypes.setTableProperties(jtbScheludeNext);
        jtbScheludeNext.setDefaultRenderer(String.class, new DBTableRenderOtherSchedule());
        jtbScheludeNext.setDefaultRenderer(Integer.class, new DBTableRenderOtherSchedule());
        
        jtbScheludeNext.getSelectionModel().addListSelectionListener(e -> {
            if (jtbScheludeNext.getSelectedRow() != -1) {
                onSelectScheludeRow(jtbScheludeNext);
            }
        });
        
        jspScheludeNext.setViewportView(jtbScheludeNext);

        JPanel jpnScheludeReminders = new JPanel();
        jpnScheludeReminders.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageReminders"), jpnScheludeReminders);
        
        JScrollPane jspScheludeReminders = new JScrollPane();
        jpnScheludeReminders.add(jspScheludeReminders, BorderLayout.CENTER);
        
        jtbScheludeReminders = new JTable();
        
        DBTableModelOtherSchedule scheludeRemindersModel = new DBTableModelOtherSchedule(DBUtils.SQL_FAVORITES);
        
        jtbScheludeReminders.setModel(scheludeRemindersModel);
        CommonTypes.setTableProperties(jtbScheludeReminders);
        jtbScheludeReminders.setDefaultRenderer(String.class, new DBTableRenderOtherSchedule());
        jtbScheludeReminders.setDefaultRenderer(Integer.class, new DBTableRenderOtherSchedule());

        jtbScheludeReminders.getSelectionModel().addListSelectionListener(e -> {
            if (jtbScheludeReminders.getSelectedRow() != -1) {
                onSelectScheludeRow(jtbScheludeReminders);
            }
        });
        
        jspScheludeReminders.setViewportView(jtbScheludeReminders);

        jslDescription = new JSplitPane();
        jslDescription.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jslDescription.setBorder(null);
        jslDescription.setDividerLocation(100);
        jslDescription.setDividerSize(3);
        jslDescription.setAutoscrolls(true);
        
        JPanel jpnImage = new JPanel();
        jpnImage.setBorder(null);
        jpnImage.setLayout(new BorderLayout());
        jslDescription.setLeftComponent(jpnImage);
        
        JScrollPane jspDescription = new JScrollPane();
        
        jtaDescription = new JTextArea();
        jtaDescription.setFont(new Font("default", 0, 14));
        jtaDescription.setEditable(false);
        jtaDescription.setLineWrap(true);
        jtaDescription.setColumns(20);
        jtaDescription.setRows(5);
        jtaDescription.setFocusable(false);
        jspDescription.setViewportView(jtaDescription);
        
        jslDescription.setRightComponent(jspDescription); 
        
        jslMain.setTopComponent(jtpMain);
        jslMain.setBottomComponent(jslDescription);
        getContentPane().add(jslMain, BorderLayout.CENTER);       

        jpnStatus = new JPanel();
        jpnStatus.setBorder(BorderFactory.createEtchedBorder());
        jpnStatus.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));

        jpbUpdate = new JProgressBar();
        jpbUpdate.setMaximum(0);
        jpbUpdate.setMinimum(0);
        jpbUpdate.setValue(0);
        jpnStatus.add(jpbUpdate, BorderLayout.CENTER);
        jpbUpdate.setVisible(false);

        jlbStatus = new JLabel();
        jlbStatus.setText(" ");
        jpnStatus.add(jlbStatus, BorderLayout.LINE_END);

        getContentPane().add(jpnStatus, BorderLayout.PAGE_END);
        
        refreshTable(jtbMainChannels, 0);
        
        jtpMain.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				onTabChanged(e);				
			}
		});

        setSize(new Dimension(906, 609));
        setLocationRelativeTo(null);

        this.addComponentListener(new java.awt.event.ComponentAdapter() { 
        	
        	@Override
            public void componentResized(ComponentEvent e) {
                onResize();
            }
        });
		
	}
    
	public void onTabChanged(ChangeEvent event) {
		jtaDescription.setText("");
		switch (jtpMain.getSelectedIndex()) {
		case 0:
			refreshTable(jtbMainChannels, 0);
			break;
		case 1:
			refreshTable(jtbScheludeNow, 0);
			break;
		case 2:
			refreshTable(jtbScheludeNext, 0);
			break;
		case 3:
			refreshTable(jtbScheludeReminders, 0);
			break;
		default:
			break;
		}
	}

    private void onSelectChannelsRow() {
        int row = jtbMainChannels.getSelectedRow();
        TableModel tm = jtbMainChannels.getModel();
        if (row != -1) {
            Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
            refreshTableForParams(jtbScheludeAll, aParams);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        jpbUpdate.setIndeterminate(parser.getMonitor().isIndeterminate());
        jpbUpdate.setMaximum(parser.getMonitor().getTotal());
        jpbUpdate.setValue(parser.getMonitor().getCurrent());

        if (!Objects.equals(parser.getMonitor().getStatus(), "")) {
            jlbStatus.setText(parser.getMonitor().getStatus());
        }

        if (parser.getMonitor().getCurrent() == -2) {
            jlbStatus.setText(" ");
            jpbUpdate.setVisible(false);
            refreshTable(jtbMainChannels, 0);
        }
    }

    private void onSelectScheludeRow(JTable table) {
        int row = table.getSelectedRow();
        TableModel tm = table.getModel();
        if (row != -1) {
            Integer id = new Integer((String) tm.getValueAt(row, DBUtils.INDEX_ID));
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
            updateDescription(aParams);
        }
    }
    
    private void updateDescription(DBParams[] aParams) {
        Connection conn = DBUtils.getConnection(DBUtils.DB_DEST);
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(DBUtils.SQL_MAINSCHEDULE_DESCRIPTION);
                DBUtils.setParams(pstmt, aParams);
                ResultSet rs = pstmt.executeQuery();
                try {
                    if (rs.next()) {
                        if (CommonTypes.FULL_DESC) {
                            jtaDescription.setText(rs.getString(1));
                        } else {
                            jtaDescription.setText(rs.getString(1));
                        }
                    } else jtaDescription.setText("");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }    	
    }

    private void onResize() {
        jslScheludeAllMain.setDividerLocation((jslScheludeAllMain.getWidth() / 5) * 1);
        jslDescription.setDividerLocation(jslDescription.getHeight());
    }
    
    private void refreshTable(JTable table, int row) {
    	DBTableModel tm = (DBTableModel) table.getModel();
    	tm.refreshContent();
    	table.setVisible(false);
    	table.setVisible(true);
        if (table.getRowCount() != 0) {
        	table.setRowSelectionInterval(row, row);
        	if (table == jtbMainChannels) {
        		onSelectChannelsRow();
        	} else {
        		onSelectScheludeRow(table);
        	}
        }

    }

    private void scheludeAllSelectCurTime() {
        int rowCount = jtbScheludeAll.getRowCount();
        Boolean find = false;
        String isOld;
        int i;
        for (i = 0; i < rowCount; i++) {
            isOld = (String) jtbScheludeAll.getModel().getValueAt(i, DBUtils.INDEX_ASCHELUDE_TIMETYPE);
            if (isOld.equals("NOW")) {
                jtbScheludeAll.setRowSelectionInterval(i, i);
                find = true;
                break;
            }
        }
        if (find) {
            jtbScheludeAll.setRowSelectionInterval(i, i);
            Rectangle rect = new Rectangle(jtbScheludeAll.getCellRect(i, 0, false));            
            jtbScheludeAll.scrollRectToVisible(rect);
        } else jtbScheludeAll.setRowSelectionInterval(0, 0);

    }

    private void refreshTableForParams(JTable table, DBParams[] aParams) {
    	DBTableModel tm = (DBTableModel) table.getModel();
        tm.refreshContentForParams(aParams);
        try {
            table.setVisible(false);
            if (table.getRowCount() != 0) {
                scheludeAllSelectCurTime();
                onSelectScheludeRow(table);
            }
        } finally {
        	table.setVisible(true);
        }

    }
    
    private class ActionSaveXmlTV extends AbstractAction {
    	private Frame parent;
    	    	
    	public ActionSaveXmlTV(Frame parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionSaveXML"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionSaveXML"));
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/resources/savexmltv.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
       	public void onExecute() {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(Messages.getString("StrActionSaveFile"));
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            fc.setSelectedFile(new File(CommonTypes.OUT_XML));
            int returnVal = fc.showSaveDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                parser.setOutXML(fc.getSelectedFile().getAbsolutePath());
                parser.runParser();
            }
    	}
    }
    
    private class ActionExit extends AbstractAction {
    	private Frame parent;
    	
    	public ActionExit(Frame parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionExit"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionExit"));
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/resources/exit.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
       	public void onExecute() {
       		parent.setVisible(false);
       		parent.dispose();
    	}
    }
    
    private class ActionChannelsList extends AbstractAction {
    	private Frame parent;
    	
    	public ActionChannelsList(Frame parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionChannels"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionChannels"));
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/resources/channelslist.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();    		
    	}
    	
       	public void onExecute() {
            ChannelsProperty channelsProperty = new ChannelsProperty(parent);
            channelsProperty.setVisible(true);
            if (channelsProperty.getModalResult() != 0) {
                refreshTable(jtbMainChannels, 0);
            } 
    	}
    }
    
    private class ActionOptions extends AbstractAction {
    	private Frame parent;
    	
    	public ActionOptions(Frame parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionOptions"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionOptions"));
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/resources/options.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
       	public void onExecute() {
       		MainProperty mainProperty = new MainProperty(parent);
       		mainProperty.setVisible(true);
    	}
    }
    
    private class ActionAbout extends AbstractAction {
    	private Frame parent;
    	
    	public ActionAbout(Frame parent) {
    		this.parent = parent;
    		putValue(NAME, Messages.getString("StrActionAbout"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionAbout"));
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/resources/about.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();   		
    	}
    	
       	public void onExecute() {
       		AboutForm aboutForm = new AboutForm(parent);
       		aboutForm.setVisible(true);
    	}
    }
    
    private class ActionUpdateProgramme extends AbstractAction {
    	
    	public ActionUpdateProgramme() {
    		putValue(NAME, Messages.getString("StrActionUpdate"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionUpdate"));
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/resources/update_prg.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();    		
    	}
    	
       	public void onExecute() {
            jpbUpdate.setVisible(true);
            new Thread(parser).start();
    	} 
    }
    
    private class ActionViewToolBar extends AbstractAction {
    	
    	public ActionViewToolBar() {
    		putValue(NAME, Messages.getString("StrActionTools"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionTools"));
            putValue(SMALL_ICON, null);		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();   		
    	}
    	
       	public void onExecute() {
       		jtbrMain.setVisible(jcmiViewToolBar.getState());
    	}    	
    }
    
    private class ActionViewStatusBar extends AbstractAction {
    	
    	public ActionViewStatusBar() {
    		putValue(NAME, Messages.getString("StrActionStatus"));
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, Messages.getString("StrActionStatus"));
            putValue(SMALL_ICON, null);		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExecute();
    	}
    	
    	public void onExecute() {
    		jpnStatus.setVisible(jcmiViewStatuslBar.getState());
    	}
    }
}