package gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.sql.*;
import java.io.File;
import java.util.Objects;

import common.CommonTypes;
import common.DBParams;
import common.DBTableModelMainChannels;
import common.DBTableModelMainSchedule;
import common.DBTableRender;
import common.DBTableRenderMainSchedule;
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
    private JMenuBar jmbMain;
    private JMenu jmnFile;
    private JMenuItem jmiSaveXmlTV;
    private JMenuItem jmiExit;
    private JMenu jmnView;
    private JCheckBoxMenuItem jcmiViewStatuslBar;
    private JCheckBoxMenuItem jcmiViewToolBar;
    private JMenu jmnActions;
    private JMenuItem jmiUpdateProgramme;
    private JMenu jmnSettings;
    private JMenuItem jmiChannelsList;
    private JMenuItem jmiOptions;
    private JMenu jmnHelp;
    private JMenuItem jmiAbout;
    private JToolBar jtbrMain;
    private JButton jbtUpdateProgramme;
    private JButton jbtChannelsList;
    private JButton jbtOptions;
    private JTabbedPane jtpMain;
    private JPanel jpnScheludeAll;
    private JSplitPane jslScheludeAllMain;
    private JScrollPane jspUserChannels;
    private JTable jtbMainChannels;
    private DBTableModelMainChannels mainChannelsModel;
    private DBTableModelMainSchedule scheludeAllModel;
    private JSplitPane jslScheludeAll;
    private JScrollPane jspScheludeAll;
    private JTable jtbScheludeAll;
    private JScrollPane jspDescriptionAll;
    private JPanel jpnImageAll;
    private JSplitPane jslDescriptionAll;
    private JTextArea jtaDescriptionAll;
    private JPanel jpnScheludeNext;
    private JPanel jpnScheludeNow;
    private JPanel jpnScheludeReminders;
    private JPanel jpnStatus;
    private JProgressBar jpbUpdate;
    private JLabel jlbStatus;
    
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

        jmbMain = new JMenuBar();

        jmnFile = new JMenu();
        jmnFile.setMnemonic('\u0424');
        jmnFile.setText(Messages.getString("StrMenuFile"));
        jmnFile.setToolTipText("");

        jmiSaveXmlTV = new JMenuItem();
        jmiSaveXmlTV.setText(Messages.getString("StrActionSaveXML"));
        jmiSaveXmlTV.setAction(acSaveXmlTV);
        jmiSaveXmlTV.setToolTipText("");
        jmnFile.add(jmiSaveXmlTV);
        
        jmiExit = new JMenuItem();
        jmiExit.setText(Messages.getString("StrActionExit"));
        jmiExit.setAction(acExit);
        jmiExit.setToolTipText("");
        jmnFile.add(jmiExit);

        jmbMain.add(jmnFile);

        jmnView = new JMenu();
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

        jmnActions = new JMenu();
        jmnActions.setMnemonic('\u0414');
        jmnActions.setText(Messages.getString("StrMenuExecute"));
        jmnActions.setToolTipText("");

        jmiUpdateProgramme = new JMenuItem();
        jmiUpdateProgramme.setText(Messages.getString("StrActionUpdate"));
        jmiUpdateProgramme.setAction(acUpdateProgramme);
        jmiUpdateProgramme.setToolTipText("");
        jmnActions.add(jmiUpdateProgramme);

        jmbMain.add(jmnActions);

        jmnSettings = new JMenu();
        jmnSettings.setMnemonic('\u041d');
        jmnSettings.setText(Messages.getString("StrMenuSettings"));
        jmnSettings.setToolTipText("");

        jmiChannelsList = new JMenuItem();
        jmiChannelsList.setText(Messages.getString("StrActionChannels"));
        jmiChannelsList.setAction(acChannelsList);
        jmiChannelsList.setToolTipText("");
        jmnSettings.add(jmiChannelsList);

        jmiOptions = new JMenuItem();
        jmiOptions.setText(Messages.getString("StrActionOptions"));
        jmiOptions.setAction(acOptions);
        jmiChannelsList.setToolTipText("");
        jmnSettings.add(jmiOptions);

        jmbMain.add(jmnSettings);

        jmnHelp = new JMenu();
        jmnHelp.setMnemonic('\u043e');
        jmnHelp.setText(Messages.getString("StrMenuHelp"));

        jmiAbout = new JMenuItem();
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

        jbtUpdateProgramme = new JButton();
        jbtUpdateProgramme.setAction(acUpdateProgramme);
        jbtUpdateProgramme.setText("");
        jbtUpdateProgramme.setFocusable(false);
        jbtUpdateProgramme.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtUpdateProgramme.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrMain.add(jbtUpdateProgramme);

        jtbrMain.add(new JToolBar.Separator());

        jbtChannelsList = new JButton();
        jbtChannelsList.setAction(acChannelsList);
        jbtChannelsList.setText("");
        jbtChannelsList.setFocusable(false);
        jbtChannelsList.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtChannelsList.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrMain.add(jbtChannelsList);

        jbtOptions = new JButton();
        jbtOptions.setAction(acOptions);
        jbtOptions.setText("");
        jbtOptions.setFocusable(false);
        jbtOptions.setHorizontalTextPosition(SwingConstants.CENTER);
        jbtOptions.setVerticalTextPosition(SwingConstants.BOTTOM);
        jtbrMain.add(jbtOptions);

        jtbrMain.add(new JToolBar.Separator());

        getContentPane().add(jtbrMain, BorderLayout.PAGE_START);

        jtpMain = new JTabbedPane();
        jtpMain.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jpnScheludeAll = new JPanel();
        jpnScheludeAll.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageProgramme"), jpnScheludeAll);

        jslScheludeAllMain = new javax.swing.JSplitPane();
        jslScheludeAllMain.setBorder(null);
        jslScheludeAllMain.setDividerLocation(360);
        jslScheludeAllMain.setDividerSize(3);
        jslScheludeAllMain.setAutoscrolls(true);

        jpnScheludeAll.add(jslScheludeAllMain, BorderLayout.CENTER);

        jtbMainChannels = new JTable();
        mainChannelsModel = new DBTableModelMainChannels(DBUtils.SQL_MAINUSERCHANNELS);
        jtbMainChannels.setModel(mainChannelsModel);

        jtbMainChannels.setFillsViewportHeight(true);
        jtbMainChannels.setFocusable(false);
        jtbMainChannels.setRowHeight(28);
        jtbMainChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbMainChannels.setShowHorizontalLines(true);
        jtbMainChannels.setShowVerticalLines(false);
        jtbMainChannels.getTableHeader().setResizingAllowed(false);
        jtbMainChannels.getTableHeader().setReorderingAllowed(false);
        jtbMainChannels.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbMainChannels.setDefaultRenderer(Object.class, new DBTableRender());
        jtbMainChannels.setGridColor(Color.LIGHT_GRAY);
        jtbMainChannels.setIntercellSpacing(new Dimension(0, 1));

        if (jtbMainChannels.getColumnModel().getColumnCount() > 0) {
        	jtbMainChannels.getColumnModel().getColumn(0).setMinWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(0).setPreferredWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(0).setMaxWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(1).setMinWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(1).setPreferredWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(1).setMaxWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(2).setMinWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(2).setPreferredWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(2).setMaxWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(3).setMinWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(3).setPreferredWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(3).setMaxWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(4).setMinWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(4).setPreferredWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(4).setMaxWidth(0);
        	jtbMainChannels.getColumnModel().getColumn(5).setMinWidth(28);
        	jtbMainChannels.getColumnModel().getColumn(5).setPreferredWidth(28);
        	jtbMainChannels.getColumnModel().getColumn(5).setMaxWidth(28);
        	jtbMainChannels.getColumnModel().getColumn(6).setMinWidth(28);
        	jtbMainChannels.getColumnModel().getColumn(6).setPreferredWidth(28);
        	jtbMainChannels.getColumnModel().getColumn(6).setMaxWidth(28);
        }

        jtbMainChannels.getSelectionModel().addListSelectionListener(e -> {
            if (jtbMainChannels.getSelectedRow() != -1) {
                onSelectChannelsRow();
            }
        });

        jspUserChannels = new JScrollPane();
        jspUserChannels.setViewportView(jtbMainChannels);
        jslScheludeAllMain.setLeftComponent(jspUserChannels);

        jslScheludeAll = new JSplitPane();
        jslScheludeAll.setBorder(null);
        jslScheludeAll.setDividerLocation(400);
        jslScheludeAll.setDividerSize(3);
        jslScheludeAll.setAutoscrolls(true);
        jslScheludeAll.setOrientation(JSplitPane.VERTICAL_SPLIT);

        jspScheludeAll = new JScrollPane();
        jtbScheludeAll = new JTable();
        scheludeAllModel = new DBTableModelMainSchedule(DBUtils.SQL_MAINSCHEDULE);
        jtbScheludeAll.setModel(scheludeAllModel);

        jtbScheludeAll.setFillsViewportHeight(true);
        jtbScheludeAll.setFocusable(false);
        jtbScheludeAll.setRowHeight(28);
        jtbScheludeAll.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbScheludeAll.setShowHorizontalLines(true);
        jtbScheludeAll.setShowVerticalLines(false);
        jtbScheludeAll.getTableHeader().setResizingAllowed(false);
        jtbScheludeAll.getTableHeader().setReorderingAllowed(false);
        jtbScheludeAll.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtbScheludeAll.setDefaultRenderer(Object.class, new DBTableRenderMainSchedule());
        jtbScheludeAll.setGridColor(Color.LIGHT_GRAY);
        jtbScheludeAll.setIntercellSpacing(new Dimension(0, 1));

        DefaultTableCellRenderer centerRenderer = new DBTableRenderMainSchedule();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        if (jtbScheludeAll.getColumnModel().getColumnCount() > 0) {
        	jtbScheludeAll.getColumnModel().getColumn(0).setMinWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(0).setPreferredWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(0).setMaxWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(1).setMinWidth(100);
        	jtbScheludeAll.getColumnModel().getColumn(1).setPreferredWidth(100);
        	jtbScheludeAll.getColumnModel().getColumn(1).setMaxWidth(100);
        	jtbScheludeAll.getColumnModel().getColumn(2).setMinWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(2).setPreferredWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(2).setMaxWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(3).setMinWidth(90);
        	jtbScheludeAll.getColumnModel().getColumn(3).setPreferredWidth(90);
        	jtbScheludeAll.getColumnModel().getColumn(3).setMaxWidth(90);
        	jtbScheludeAll.getColumnModel().getColumn(5).setMinWidth(28);
        	jtbScheludeAll.getColumnModel().getColumn(5).setPreferredWidth(28);
        	jtbScheludeAll.getColumnModel().getColumn(5).setMaxWidth(28);
        	jtbScheludeAll.getColumnModel().getColumn(6).setMinWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(6).setPreferredWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(6).setMaxWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(7).setMinWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(7).setPreferredWidth(0);
        	jtbScheludeAll.getColumnModel().getColumn(7).setMaxWidth(0);

        	jtbScheludeAll.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        	jtbScheludeAll.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        }

        jtbScheludeAll.getSelectionModel().addListSelectionListener(e -> {
            if (jtbScheludeAll.getSelectedRow() != -1) {
                onSelectScheludeAllRow();
            }
        });

        jspScheludeAll.setViewportView(jtbScheludeAll);

        jslScheludeAll.setTopComponent(jspScheludeAll);

        jslDescriptionAll = new JSplitPane();
        jslDescriptionAll.setBorder(null);
        jslDescriptionAll.setDividerLocation(100);
        jslDescriptionAll.setDividerSize(3);
        jslDescriptionAll.setAutoscrolls(true);

        jpnImageAll = new JPanel();
        jpnImageAll.setBorder(null);
        jpnImageAll.setLayout(new BorderLayout());
        jslDescriptionAll.setLeftComponent(jpnImageAll);

        jspDescriptionAll = new JScrollPane();
        jtaDescriptionAll = new JTextArea();
        jtaDescriptionAll.setFont(new Font("default", 0, 14));
        jtaDescriptionAll.setEditable(false);
        jtaDescriptionAll.setLineWrap(true);
        jtaDescriptionAll.setColumns(20);
        jtaDescriptionAll.setRows(5);
        jtaDescriptionAll.setFocusable(false);
        jspDescriptionAll.setViewportView(jtaDescriptionAll);

        jslDescriptionAll.setRightComponent(jspDescriptionAll);

        jslScheludeAll.setBottomComponent(jslDescriptionAll);

        jslScheludeAllMain.setRightComponent(jslScheludeAll);

        jpnScheludeNow = new JPanel();
        jpnScheludeNow.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageNow"), jpnScheludeNow);

        jpnScheludeNext = new JPanel();
        jpnScheludeNext.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageNext"), jpnScheludeNext);

        jpnScheludeReminders = new JPanel();
        jpnScheludeReminders.setLayout(new BorderLayout(4, 4));
        jtpMain.addTab(Messages.getString("StrPageReminders"), jpnScheludeReminders);

        getContentPane().add(jtpMain, BorderLayout.CENTER);

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

        refreshTableMainChannels(0);

        setSize(new Dimension(906, 609));
        setLocationRelativeTo(null);

        this.addComponentListener(new java.awt.event.ComponentAdapter() { 
        	
        	@Override
            public void componentResized(ComponentEvent e) {
                onResize();
            }
        });
		
	}

    private void onSelectChannelsRow() {
        int row1 = jtbMainChannels.getSelectedRow();
        TableModel tm = jtbMainChannels.getModel();
        if (row1 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row1, 0));
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
            refreshTableMainSchedule(aParams);
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
            refreshTableMainChannels(0);
        }
    }

    private void onSelectScheludeAllRow() {
        int row1 = jtbScheludeAll.getSelectedRow();
        TableModel tm = jtbScheludeAll.getModel();
        if (row1 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row1, 0));
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, id, CommonTypes.DBType.INTEGER);
            Connection conn = DBUtils.getConnection(DBUtils.DB_DEST);
            if (conn != null) {
                try {
                    PreparedStatement pstmt = conn.prepareStatement(DBUtils.SQL_MAINSCHEDULE_DESCRIPTION);
                    DBUtils.setParams(pstmt, aParams);
                    ResultSet rs = pstmt.executeQuery();
                    try {
                        if (rs.next()) {
                            if (CommonTypes.FULL_DESC) {
                                jtaDescriptionAll.setText(rs.getString(1));
                            } else {
                                jtaDescriptionAll.setText(rs.getString(1));
                            }
                        } else jtaDescriptionAll.setText("");
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
    }

    private void onResize() {
        jslScheludeAll.setDividerLocation((jslScheludeAll.getHeight() / 5) * 4);
        jslDescriptionAll.setDividerLocation(jslScheludeAll.getHeight() / 5);
    }

    private void refreshTableMainChannels(int row) {
        mainChannelsModel.refreshContent();
        jtbMainChannels.setVisible(false);
        jtbMainChannels.setVisible(true);
        if (jtbMainChannels.getRowCount() != 0) {
            jtbMainChannels.setRowSelectionInterval(row, row);
            onSelectChannelsRow();
        }

    }

    private void scheludeAllSelectCurTime() {
        int rowCount = jtbScheludeAll.getRowCount();
        Boolean find = false;
        int i;
        for (i = 0; i < rowCount; i++) {
            int tl = CommonTypes.isTimeLine((String)jtbScheludeAll.getModel().getValueAt(i,1),(String)jtbScheludeAll.getModel().getValueAt(i,2));
            if (tl == 1) {
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

    private void refreshTableMainSchedule(DBParams[] aParams) {
        scheludeAllModel.refreshContentForParams(aParams);
        try {
            jtbScheludeAll.setVisible(false);
            if (jtbScheludeAll.getRowCount() != 0) {
                scheludeAllSelectCurTime();
                onSelectScheludeAllRow();
            }
        } finally {
            jtbScheludeAll.setVisible(true);
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
                refreshTableMainChannels(0);
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