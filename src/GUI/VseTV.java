package GUI;

import Common.*;
import Parser.ParserVseTV;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static Common.Common.getImagesPatch;
import static Common.DBUtils.getConnection;
import static Common.DBUtils.setParams;
import static Common.Common.loadConfigs;
import static Common.Strings.*;

@SuppressWarnings("serial")
public class VseTV  extends JFrame implements ChangeListener {

    private ParserVseTV Parser;

    private MainAction acSaveXmlTV;
    private MainAction acExit;
    private MainAction acViewToolBar;
    private MainAction acViewStatuslBar;
    private MainAction acUpdateProgramme;
    private MainAction acChannelsList;
    private MainAction acOptions;
    private MainAction acAbout;

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
    
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        loadConfigs();
        EventQueue.invokeLater(() -> new VseTV().setVisible(true));	
	}

	/**
	 * Create the frame.
	 */

    public VseTV() {
        Parser = new ParserVseTV(Common.XMLOut, Common.Lang, Common.CountDay, Common.FullDesc);
        Parser.getMonitor().addChangeListener(this);
        createActions();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(StrTitleMain);
        setIconImage(new ImageIcon(getImagesPatch() + "main.png").getImage());

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
        miViewStatuslBar.setAction(acViewStatuslBar);
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

        add(tbMain, BorderLayout.PAGE_START);

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

        add(tpMain, BorderLayout.CENTER);

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

        add(pnStatus, BorderLayout.PAGE_END);

        RefreshTableMainChannels(0);

        setSize(new Dimension(906, 609));
        setLocationRelativeTo(null);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                onResize();
            }
        });
    }
    
    private void createActions() {
        acSaveXmlTV = new MainAction(StrActionSaveXML, new ImageIcon(getImagesPatch() + "savexmltv.png"), StrActionSaveXML, (int) '\u0445', KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_MASK), "cmd_Save_XmlTV");
        acExit = new MainAction(StrActionExit, new ImageIcon(getImagesPatch() + "exit.png"), StrActionExit, (int) '\u0412', KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK), "cmd_Exit");
        acChannelsList = new MainAction(StrActionChannels, new ImageIcon(getImagesPatch() + "channelslist.png"), StrActionChannels, (int) '\u0421', KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK), "cmd_Channels_List");
        acOptions = new MainAction(StrActionOptions, new ImageIcon(getImagesPatch() + "options.png"), StrActionOptions, (int) '\u041f', KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK), "cmd_Options");
        acAbout = new MainAction(StrActionAbout, new ImageIcon(getImagesPatch() + "about.png"), StrActionAbout, (int) '\u0440', null, "cmd_About");
        acUpdateProgramme = new MainAction(StrActionUpdate, new ImageIcon(getImagesPatch() + "update_prg.png"), StrActionUpdate, null, null, "cmd_Update_Programme");
        acViewToolBar = new MainAction(StrActionTools, null, StrActionTools, null, null, "cmd_View_ToolBar");
        acViewStatuslBar = new MainAction(StrActionStatus, null, StrActionStatus, null, null, "cmd_View_StatusBar");
    }

    private void onSaveXmlTV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(StrActionSaveFile);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setSelectedFile(new File(Common.XMLOut));
        int returnVal = fc.showOpenDialog(this);
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

    class MainAction extends AbstractAction {

        public MainAction(String text, ImageIcon icon, String desc, Integer mnemonic, KeyStroke accelerator, String actioncommand) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(ACCELERATOR_KEY, accelerator);
            putValue(ACTION_COMMAND_KEY, actioncommand);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "cmd_Save_XmlTV":
                    onSaveXmlTV();
                    break;
                case "cmd_Exit":
                    onExit();
                    break;
                case "cmd_View_ToolBar":
                    onViewToolBar();
                    break;
                case "cmd_View_StatusBar":
                    onViewStatusBar();
                    break;
                case "cmd_Update_Programme":
                    onUpdateProgramme();
                    break;
                case "cmd_Channels_List":
                    onChannelsList();
                    break;
                case "cmd_Options":
                    onOptions();
                    break;
                case "cmd_About":
                    onAbout();
                    break;
            }
        }
    }
}