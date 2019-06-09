package GUI;

import Common.*;
import Parser.ParserVseTV;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.sql.*;
import java.io.File;
import java.util.Objects;

import static Common.DBUtils.getConnection;
import static Common.DBUtils.setParams;
import static Common.Common.loadConfigs;
import static Common.Strings.*;

@SuppressWarnings("serial")
public class VseTV  extends JFrame implements ChangeListener {
    private ParserVseTV Parser;
    private ActionSaveXmlTV acSaveXmlTV;    
    private ActionExit acExit;
    private ActionChannelsList acChannelsList;
    private ActionOptions acOptions;
    private ActionAbout acAbout;
    private ActionUpdateProgramme acUpdateProgramme;
    private ActionViewToolBar acViewToolBar;
    private ActionViewStatusBar acViewStatusBar;     
    private JMenuBar mbMain;
    private JMenu mFile;
    private JMenuItem miSaveXmlTV;
    private JMenuItem miExit;
    private JMenu mView;
    private JCheckBoxMenuItem miViewStatuslBar;
    private JCheckBoxMenuItem miViewToolBar;
    private JMenu mActions;
    private JMenuItem miUpdateProgramme;
    private JMenu mSettings;
    private JMenuItem miChannelsList;
    private JMenuItem miOptions;
    private JMenu mHelp;
    private JMenuItem miAbout;
    private JToolBar tbMain;
    private JButton btUpdateProgramme;
    private JButton btChannelsList;
    private JButton btOptions;
    private JTabbedPane tpMain;
    private JPanel tabScheludeAll;
    private JSplitPane spScheludeAllMain;
    private JScrollPane scpUserChannels;
    private JTable tblMainChannels;
    private DBTableModelMainChannels MainChannelsModel;
    private DBTableModelMainSchedule ScheludeAllModel;
    private JSplitPane spScheludeAll;
    private JScrollPane scpScheludeAll;
    private JTable tblScheludeAll;
    private JScrollPane scpDescriptionAll;
    private JPanel pnImageAll;
    private JSplitPane spDescriptionAll;
    private JTextArea taDescriptionAll;
    private JPanel tabScheludeNext;
    private JPanel tabScheludeNow;
    private JPanel tabScheludeReminders;
    private JPanel pnStatus;
    private JProgressBar pbUpdate;
    private JLabel lbStatus;
    
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        loadConfigs();
        EventQueue.invokeLater(() -> new VseTV().setVisible(true));	
	}

    public VseTV() {
        Parser = new ParserVseTV(Common.XMLOut, Common.Lang, Common.CountDay, Common.FullDesc);
        Parser.getMonitor().addChangeListener(this);
        
        acSaveXmlTV = new ActionSaveXmlTV("");        
        acExit = new ActionExit("");
        acChannelsList = new ActionChannelsList("");
        acOptions = new ActionOptions("");
        acAbout = new ActionAbout("");
        acUpdateProgramme = new ActionUpdateProgramme("");
        acViewToolBar = new ActionViewToolBar("");
        acViewStatusBar = new ActionViewStatusBar("");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(StrTitleMain);
        setIconImage(Toolkit.getDefaultToolkit().getImage(VseTV.class.getResource("/Resources/main.png")));

        mbMain = new JMenuBar();

        mFile = new JMenu();
        mFile.setMnemonic('\u0424');
        mFile.setText(StrMenuFile);
        mFile.setToolTipText("");

        miSaveXmlTV = new JMenuItem();
        miSaveXmlTV.setAction(acSaveXmlTV);
        miSaveXmlTV.setToolTipText("");
        mFile.add(miSaveXmlTV);
        
        miExit = new JMenuItem();
        miExit.setAction(acExit);
        miExit.setToolTipText("");
        mFile.add(miExit);

        mbMain.add(mFile);

        mView = new JMenu();
        mView.setMnemonic('\u0438');
        mView.setText(StrMenuView);
        mView.setToolTipText("");

        miViewToolBar = new JCheckBoxMenuItem();
        miViewToolBar.setState(true);
        miViewToolBar.setAction(acViewToolBar);
        miViewToolBar.setToolTipText("");
        mView.add(miViewToolBar);

        miViewStatuslBar = new JCheckBoxMenuItem();
        miViewStatuslBar.setState(true);
        miViewStatuslBar.setAction(acViewStatusBar);
        miViewStatuslBar.setToolTipText("");
        mView.add(miViewStatuslBar);

        mbMain.add(mView);

        mActions = new JMenu();
        mActions.setMnemonic('\u0414');
        mActions.setText(StrMenuExecute);
        mActions.setToolTipText("");

        miUpdateProgramme = new JMenuItem();
        miUpdateProgramme.setAction(acUpdateProgramme);
        miUpdateProgramme.setToolTipText("");
        mActions.add(miUpdateProgramme);

        mbMain.add(mActions);

        mSettings = new JMenu();
        mSettings.setMnemonic('\u041d');
        mSettings.setText(StrMenuSettings);
        mSettings.setToolTipText("");

        miChannelsList = new JMenuItem();
        miChannelsList.setAction(acChannelsList);
        miChannelsList.setToolTipText("");
        mSettings.add(miChannelsList);

        miOptions = new JMenuItem();
        miOptions.setAction(acOptions);
        miChannelsList.setToolTipText("");
        mSettings.add(miOptions);

        mbMain.add(mSettings);

        mHelp = new JMenu();
        mHelp.setMnemonic('\u043e');
        mHelp.setText(StrMenuHelp);

        miAbout = new JMenuItem();
        miAbout.setAction(acAbout);
        miAbout.setToolTipText("");
        mHelp.add(miAbout);

        mbMain.add(mHelp);
        
        setJMenuBar(mbMain);

        tbMain = new JToolBar();
        tbMain.setBorder(BorderFactory.createEtchedBorder());
        tbMain.setFloatable(false);
        tbMain.setRollover(true);

        btUpdateProgramme = new JButton();
        btUpdateProgramme.setAction(acUpdateProgramme);
        btUpdateProgramme.setText("");
        btUpdateProgramme.setFocusable(false);
        btUpdateProgramme.setHorizontalTextPosition(SwingConstants.CENTER);
        btUpdateProgramme.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbMain.add(btUpdateProgramme);

        tbMain.add(new JToolBar.Separator());

        btChannelsList = new JButton();
        btChannelsList.setAction(acChannelsList);
        btChannelsList.setText("");
        btChannelsList.setFocusable(false);
        btChannelsList.setHorizontalTextPosition(SwingConstants.CENTER);
        btChannelsList.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbMain.add(btChannelsList);

        btOptions = new JButton();
        btOptions.setAction(acOptions);
        btOptions.setText("");
        btOptions.setFocusable(false);
        btOptions.setHorizontalTextPosition(SwingConstants.CENTER);
        btOptions.setVerticalTextPosition(SwingConstants.BOTTOM);
        tbMain.add(btOptions);

        tbMain.add(new JToolBar.Separator());

        getContentPane().add(tbMain, BorderLayout.PAGE_START);

        tpMain = new JTabbedPane();
        tpMain.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        tabScheludeAll = new JPanel();
        tabScheludeAll.setLayout(new BorderLayout(4, 4));
        tpMain.addTab(StrPageProgramme, tabScheludeAll);

        spScheludeAllMain = new javax.swing.JSplitPane();
        spScheludeAllMain.setBorder(null);
        spScheludeAllMain.setDividerLocation(360);
        spScheludeAllMain.setDividerSize(3);
        spScheludeAllMain.setAutoscrolls(true);

        tabScheludeAll.add(spScheludeAllMain, BorderLayout.CENTER);

        tblMainChannels = new JTable();
        MainChannelsModel = new DBTableModelMainChannels(DBUtils.sqlMainUserChannels);
        tblMainChannels.setModel(MainChannelsModel);

        tblMainChannels.setFillsViewportHeight(true);
        tblMainChannels.setFocusable(false);
        tblMainChannels.setRowHeight(28);
        tblMainChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMainChannels.setShowHorizontalLines(true);
        tblMainChannels.setShowVerticalLines(false);
        tblMainChannels.getTableHeader().setResizingAllowed(false);
        tblMainChannels.getTableHeader().setReorderingAllowed(false);
        tblMainChannels.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMainChannels.setDefaultRenderer(Object.class, new DBTableRender());
        tblMainChannels.setGridColor(Color.LIGHT_GRAY);
        tblMainChannels.setIntercellSpacing(new Dimension(0, 1));

        if (tblMainChannels.getColumnModel().getColumnCount() > 0) {
            tblMainChannels.getColumnModel().getColumn(0).setMinWidth(0);
            tblMainChannels.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblMainChannels.getColumnModel().getColumn(0).setMaxWidth(0);
            tblMainChannels.getColumnModel().getColumn(1).setMinWidth(0);
            tblMainChannels.getColumnModel().getColumn(1).setPreferredWidth(0);
            tblMainChannels.getColumnModel().getColumn(1).setMaxWidth(0);
            tblMainChannels.getColumnModel().getColumn(2).setMinWidth(0);
            tblMainChannels.getColumnModel().getColumn(2).setPreferredWidth(0);
            tblMainChannels.getColumnModel().getColumn(2).setMaxWidth(0);
            tblMainChannels.getColumnModel().getColumn(3).setMinWidth(0);
            tblMainChannels.getColumnModel().getColumn(3).setPreferredWidth(0);
            tblMainChannels.getColumnModel().getColumn(3).setMaxWidth(0);
            tblMainChannels.getColumnModel().getColumn(4).setMinWidth(0);
            tblMainChannels.getColumnModel().getColumn(4).setPreferredWidth(0);
            tblMainChannels.getColumnModel().getColumn(4).setMaxWidth(0);
            tblMainChannels.getColumnModel().getColumn(5).setMinWidth(28);
            tblMainChannels.getColumnModel().getColumn(5).setPreferredWidth(28);
            tblMainChannels.getColumnModel().getColumn(5).setMaxWidth(28);
            tblMainChannels.getColumnModel().getColumn(6).setMinWidth(28);
            tblMainChannels.getColumnModel().getColumn(6).setPreferredWidth(28);
            tblMainChannels.getColumnModel().getColumn(6).setMaxWidth(28);
        }

        tblMainChannels.getSelectionModel().addListSelectionListener(e -> {
            if (tblMainChannels.getSelectedRow() != -1) {
                onSelectChannelsRow();
            }
        });

        scpUserChannels = new JScrollPane();
        scpUserChannels.setViewportView(tblMainChannels);
        spScheludeAllMain.setLeftComponent(scpUserChannels);

        spScheludeAll = new JSplitPane();
        spScheludeAll.setBorder(null);
        spScheludeAll.setDividerLocation(400);
        spScheludeAll.setDividerSize(3);
        spScheludeAll.setAutoscrolls(true);
        spScheludeAll.setOrientation(JSplitPane.VERTICAL_SPLIT);

        scpScheludeAll = new JScrollPane();
        tblScheludeAll = new JTable();
        ScheludeAllModel = new DBTableModelMainSchedule(DBUtils.sqlMainSchedule);
        tblScheludeAll.setModel(ScheludeAllModel);

        tblScheludeAll.setFillsViewportHeight(true);
        tblScheludeAll.setFocusable(false);
        tblScheludeAll.setRowHeight(28);
        tblScheludeAll.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblScheludeAll.setShowHorizontalLines(true);
        tblScheludeAll.setShowVerticalLines(false);
        tblScheludeAll.getTableHeader().setResizingAllowed(false);
        tblScheludeAll.getTableHeader().setReorderingAllowed(false);
        tblScheludeAll.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblScheludeAll.setDefaultRenderer(Object.class, new DBTableRenderMainSchedule());
        tblScheludeAll.setGridColor(Color.LIGHT_GRAY);
        tblScheludeAll.setIntercellSpacing(new Dimension(0, 1));

        DefaultTableCellRenderer centerRenderer = new DBTableRenderMainSchedule();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        if (tblScheludeAll.getColumnModel().getColumnCount() > 0) {
            tblScheludeAll.getColumnModel().getColumn(0).setMinWidth(0);
            tblScheludeAll.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblScheludeAll.getColumnModel().getColumn(0).setMaxWidth(0);
            tblScheludeAll.getColumnModel().getColumn(1).setMinWidth(100);
            tblScheludeAll.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblScheludeAll.getColumnModel().getColumn(1).setMaxWidth(100);
            tblScheludeAll.getColumnModel().getColumn(2).setMinWidth(0);
            tblScheludeAll.getColumnModel().getColumn(2).setPreferredWidth(0);
            tblScheludeAll.getColumnModel().getColumn(2).setMaxWidth(0);
            tblScheludeAll.getColumnModel().getColumn(3).setMinWidth(90);
            tblScheludeAll.getColumnModel().getColumn(3).setPreferredWidth(90);
            tblScheludeAll.getColumnModel().getColumn(3).setMaxWidth(90);
            tblScheludeAll.getColumnModel().getColumn(5).setMinWidth(28);
            tblScheludeAll.getColumnModel().getColumn(5).setPreferredWidth(28);
            tblScheludeAll.getColumnModel().getColumn(5).setMaxWidth(28);
            tblScheludeAll.getColumnModel().getColumn(6).setMinWidth(0);
            tblScheludeAll.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblScheludeAll.getColumnModel().getColumn(6).setMaxWidth(0);
            tblScheludeAll.getColumnModel().getColumn(7).setMinWidth(0);
            tblScheludeAll.getColumnModel().getColumn(7).setPreferredWidth(0);
            tblScheludeAll.getColumnModel().getColumn(7).setMaxWidth(0);

            tblScheludeAll.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            tblScheludeAll.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        }

        tblScheludeAll.getSelectionModel().addListSelectionListener(e -> {
            if (tblScheludeAll.getSelectedRow() != -1) {
                onSelectScheludeAllRow();
            }
        });

        scpScheludeAll.setViewportView(tblScheludeAll);

        spScheludeAll.setTopComponent(scpScheludeAll);

        spDescriptionAll = new JSplitPane();
        spDescriptionAll.setBorder(null);
        spDescriptionAll.setDividerLocation(100);
        spDescriptionAll.setDividerSize(3);
        spDescriptionAll.setAutoscrolls(true);

        pnImageAll = new JPanel();
        pnImageAll.setBorder(null);
        pnImageAll.setLayout(new BorderLayout());
        spDescriptionAll.setLeftComponent(pnImageAll);

        scpDescriptionAll = new JScrollPane();
        taDescriptionAll = new JTextArea();
        taDescriptionAll.setFont(new Font("default", 0, 14));
        taDescriptionAll.setEditable(false);
        taDescriptionAll.setLineWrap(true);
        taDescriptionAll.setColumns(20);
        taDescriptionAll.setRows(5);
        taDescriptionAll.setFocusable(false);
        scpDescriptionAll.setViewportView(taDescriptionAll);

        spDescriptionAll.setRightComponent(scpDescriptionAll);

        spScheludeAll.setBottomComponent(spDescriptionAll);

        spScheludeAllMain.setRightComponent(spScheludeAll);

        tabScheludeNow = new JPanel();
        tabScheludeNow.setLayout(new BorderLayout(4, 4));
        tpMain.addTab(StrPageNow, tabScheludeNow);

        tabScheludeNext = new JPanel();
        tabScheludeNext.setLayout(new BorderLayout(4, 4));
        tpMain.addTab(StrPageNext, tabScheludeNext);

        tabScheludeReminders = new JPanel();
        tabScheludeReminders.setLayout(new BorderLayout(4, 4));
        tpMain.addTab(StrPageReminders, tabScheludeReminders);

        getContentPane().add(tpMain, BorderLayout.CENTER);

        pnStatus = new JPanel();
        pnStatus.setBorder(BorderFactory.createEtchedBorder());
        pnStatus.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));

        pbUpdate = new JProgressBar();
        pbUpdate.setMaximum(0);
        pbUpdate.setMinimum(0);
        pbUpdate.setValue(0);
        pnStatus.add(pbUpdate, BorderLayout.CENTER);
        pbUpdate.setVisible(false);

        lbStatus = new JLabel();
        lbStatus.setText(" ");
        pnStatus.add(lbStatus, BorderLayout.LINE_END);

        getContentPane().add(pnStatus, BorderLayout.PAGE_END);

        RefreshTableMainChannels(0);

        setSize(new Dimension(906, 609));
        setLocationRelativeTo(null);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                onResize();
            }
        });
    }
    
    private void onSaveXmlTV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(StrActionSaveFile);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setSelectedFile(new File(Common.XMLOut));
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Parser.setXMLOut(fc.getSelectedFile().getAbsolutePath());
            Parser.runParser();
        }
    }

    private void onExit() {
        setVisible(false);
        dispose();
    }

    private void onUpdateProgramme() {
        pbUpdate.setVisible(true);
        new Thread(Parser).start();
    }

    private void onChannelsList() {
        ChannelsProperty chnprop = new ChannelsProperty(this);
        chnprop.setVisible(true);
        if (chnprop.getModalResult() != 0) {
            RefreshTableMainChannels(0);
        }
    }

    private void onOptions() {
        MainProperty mp = new MainProperty(this);
        mp.setVisible(true);
    }

    private void onAbout() {
        AboutForm af = new AboutForm(this);
        af.setVisible(true);
    }

    private void onSelectChannelsRow() {
        int row1 = tblMainChannels.getSelectedRow();
        TableModel tm = tblMainChannels.getModel();
        if (row1 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row1, 0));
            DBParams[] Params = new DBParams[1];
            Params[0] = new DBParams(1, id, Common.DBType.INTEGER);
            RefreshTableMainSchedule(Params);
        }
    }

    private void onViewToolBar() {
        tbMain.setVisible(miViewToolBar.getState());
    }

    private void onViewStatusBar() {
        pnStatus.setVisible(miViewStatuslBar.getState());
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        pbUpdate.setIndeterminate(Parser.getMonitor().isIndeterminate());
        pbUpdate.setMaximum(Parser.getMonitor().getTotal());
        pbUpdate.setValue(Parser.getMonitor().getCurrent());

        if (!Objects.equals(Parser.getMonitor().getStatus(), "")) {
            lbStatus.setText(Parser.getMonitor().getStatus());
        }

        if (Parser.getMonitor().getCurrent() == -2) {
            lbStatus.setText(" ");
            pbUpdate.setVisible(false);
            RefreshTableMainChannels(0);
        }
    }

    private void onSelectScheludeAllRow() {
        int row1 = tblScheludeAll.getSelectedRow();
        TableModel tm = tblScheludeAll.getModel();
        if (row1 != -1) {
            Integer id = new Integer((String) tm.getValueAt(row1, 0));
            DBParams[] Params = new DBParams[1];
            Params[0] = new DBParams(1, id, Common.DBType.INTEGER);
            Connection conn = getConnection(DBUtils.DBDest);
            if (conn != null) {
                try {
                    PreparedStatement pstmt = conn.prepareStatement(DBUtils.sqlMainScheduleDesc);
                    setParams(pstmt, Params);
                    ResultSet rs = pstmt.executeQuery();
                    try {
                        if (rs.next()) {
                            if (Common.FullDesc) {
                                taDescriptionAll.setText(rs.getString(1));
                            } else {
                                taDescriptionAll.setText(rs.getString(1));
                            }
                        } else taDescriptionAll.setText("");
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
        spScheludeAll.setDividerLocation((spScheludeAll.getHeight() / 5) * 4);
        spDescriptionAll.setDividerLocation(spScheludeAll.getHeight() / 5);
    }

    private void RefreshTableMainChannels(int row) {
        MainChannelsModel.refreshContent();
        tblMainChannels.setVisible(false);
        tblMainChannels.setVisible(true);
        if (tblMainChannels.getRowCount() != 0) {
            tblMainChannels.setRowSelectionInterval(row, row);
            onSelectChannelsRow();
        }

    }

    private void ScheludeAllSelectCurTime() {
        int rowCount = tblScheludeAll.getRowCount();
        Boolean find = false;
        int i;
        //int x;
        
        for (i = 0; i < rowCount; i++) {
            int tl = Common.isTimeLine((String)tblScheludeAll.getModel().getValueAt(i,1),(String)tblScheludeAll.getModel().getValueAt(i,2));
            if (tl == 1) {
                tblScheludeAll.setRowSelectionInterval(i, i);
                find = true;
                break;
            }
        }
        if (find) {
            tblScheludeAll.setRowSelectionInterval(i, i);
            Rectangle rect = new Rectangle(tblScheludeAll.getCellRect(i, 0, false));            
            tblScheludeAll.scrollRectToVisible(rect);
            //x = (tblScheludeAll.getSize().height / rowCount) * tblScheludeAll.getSelectedRow();
            //scpScheludeAll.getVerticalScrollBar().setValue(x);
        } else tblScheludeAll.setRowSelectionInterval(0, 0);

    }

    private void RefreshTableMainSchedule(DBParams[] Params) {
        ScheludeAllModel.refreshContentForParams(Params);
        try {
            tblScheludeAll.setVisible(false);
            if (tblScheludeAll.getRowCount() != 0) {
                ScheludeAllSelectCurTime();
                onSelectScheludeAllRow();
            }
        } finally {
            tblScheludeAll.setVisible(true);
        }

    }
    
    public class ActionSaveXmlTV extends AbstractAction
    {
    	public ActionSaveXmlTV(String text) {
    		putValue(NAME, StrActionSaveXML);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, StrActionSaveXML);
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/Resources/savexmltv.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onSaveXmlTV();    		
    	}
    }
    
    public class ActionExit extends AbstractAction
    {
    	public ActionExit(String text) {
    		putValue(NAME, StrActionExit);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, StrActionExit);
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/Resources/exit.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onExit();    		
    	}
    }
    
    public class ActionChannelsList extends AbstractAction
    {
    	public ActionChannelsList(String text) {
    		putValue(NAME, StrActionChannels);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, StrActionChannels);
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/Resources/channelslist.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onChannelsList();    		
    	}
    }
    
    public class ActionOptions extends AbstractAction
    {
    	public ActionOptions(String text) {
    		putValue(NAME, StrActionOptions);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));
            putValue(SHORT_DESCRIPTION, StrActionOptions);
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/Resources/options.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onOptions();   		
    	}
    }
    
    public class ActionAbout extends AbstractAction
    {
    	public ActionAbout(String text) {
    		putValue(NAME, StrActionAbout);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionAbout);
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/Resources/about.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onAbout();   		
    	}
    }
    
    public class ActionUpdateProgramme extends AbstractAction
    {
    	public ActionUpdateProgramme(String text) {
    		putValue(NAME, StrActionUpdate);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionUpdate);
            putValue(SMALL_ICON, new ImageIcon(VseTV.class.getResource("/Resources/update_prg.png")));		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onUpdateProgramme();    		
    	}
    }
    
    public class ActionViewToolBar extends AbstractAction
    {
    	public ActionViewToolBar(String text) {
    		putValue(NAME, StrActionTools);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionTools);
            putValue(SMALL_ICON, null);		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onViewToolBar();   		
    	}
    }
    
    public class ActionViewStatusBar extends AbstractAction
    {
    	public ActionViewStatusBar(String text) {
    		putValue(NAME, StrActionStatus);
            putValue(ACCELERATOR_KEY, null);
            putValue(SHORT_DESCRIPTION, StrActionStatus);
            putValue(SMALL_ICON, null);		
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		onViewStatusBar();   		
    	}
    }
}