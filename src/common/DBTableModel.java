package common;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DBTableModel extends AbstractTableModel {

    private int countRows;
    protected DBFieldsSet fieldsSet;
    protected List<String[]> tableContent;
    protected String sQuery;

    public List<String[]> getTableContent() {
        return tableContent;
    }

    public void setTableContent(List<String[]> tableContent) {
        this.tableContent = tableContent;
    }

    public String getsQuery() {
        return sQuery;
    }

    public void setsQuery(String aQuery) {
        this.sQuery = aQuery;
    }

    public void refreshContent() {
        countRows = 0;
        tableContent.clear();
        int colcount = getColumnCount();
        Connection conn = DBUtils.getConnection(DBUtils.DB_DEST);
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sQuery);
                try {
                    while (rs.next()) {
                        String[] rdata = new String[colcount];
                        for (int c = 0; c < colcount; c++) {
                            rdata[c] = rs.getString(fieldsSet.getFieldSet().get(c).getName());
                        }
                        tableContent.add(rdata);
                        countRows++;
                    }
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

    public void refreshContentForParams(DBParams[] aParams) {
        countRows = 0;
        tableContent.clear();
        int colcount = getColumnCount();
        Connection conn = DBUtils.getConnection(DBUtils.DB_DEST);
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(sQuery);
                DBUtils.setParams(pstmt, aParams);
                ResultSet rs = pstmt.executeQuery();
                try {
                    while (rs.next()) {
                        String[] rdata = new String[colcount];
                        for (int c = 0; c < colcount; c++) {
                            rdata[c] = rs.getString(fieldsSet.getFieldSet().get(c).getName());
                        }
                        tableContent.add(rdata);
                        countRows++;
                    }
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

    public DBTableModel(String aQuery) {
        super();
        this.sQuery = aQuery;
        tableContent = new ArrayList<>();
        fieldsSet = new DBFieldsSet();
    }

    @Override
    public int getRowCount() {
        return countRows;
    }

    @Override
    public int getColumnCount() {
        return fieldsSet.getFieldSet().size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CommonTypes.DBType type = fieldsSet.getFieldSet().get(columnIndex).getType();
        if (type == CommonTypes.DBType.BOOL) {
            String val = tableContent.get(rowIndex)[columnIndex];
            return !val.equals("0");
        } else if (type == CommonTypes.DBType.OBJECT) {
            return getIconObject(tableContent.get(rowIndex)[columnIndex]);
        } else {
            return tableContent.get(rowIndex)[columnIndex];
        }
    }

    @Override
    public String getColumnName(int column){
        return fieldsSet.getFieldSet().get(column).getTitle();
    }

    public Object getIconObject(Object obj) {
        return obj;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
    	CommonTypes.DBType type = fieldsSet.getFieldSet().get(columnIndex).getType();

        if (type == CommonTypes.DBType.STRING) {
            return java.lang.String.class;
        } else if (type == CommonTypes.DBType.INTEGER) {
            return java.lang.Integer.class;
        } else if (type == CommonTypes.DBType.BOOL) {
            return java.lang.Boolean.class;
        } else if (type == CommonTypes.DBType.OBJECT) {
            return Icon.class;
        } else {
            return java.lang.Object.class;
        }
    }
}
