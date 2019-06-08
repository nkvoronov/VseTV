package Common;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

import static Common.DBUtils.getConnection;
import static Common.DBUtils.setParams;

@SuppressWarnings("serial")
public class DBTableModel extends AbstractTableModel {

    private int countRows;
    protected DBFieldsSet fieldsSet;
    protected ArrayList<String[]> tableContent;
    protected String sQuery;

    public ArrayList<String[]> getTableContent() {
        return tableContent;
    }

    public void setTableContent(ArrayList<String[]> tableContent) {
        this.tableContent = tableContent;
    }

    public String getsQuery() {
        return sQuery;
    }

    public void setsQuery(String Query) {
        this.sQuery = Query;
    }

    public void refreshContent() {
        countRows = 0;
        tableContent.clear();
        int colcount = getColumnCount();
        Connection conn = getConnection(DBUtils.DBDest);
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

    public void refreshContentForParams(DBParams[] Params) {
        countRows = 0;
        tableContent.clear();
        int colcount = getColumnCount();
        Connection conn = getConnection(DBUtils.DBDest);
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(sQuery);
                setParams(pstmt, Params);
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

    public DBTableModel(String Query) {
        super();
        this.sQuery = Query;
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
        Common.DBType type = fieldsSet.getFieldSet().get(columnIndex).getType();
        if (type == Common.DBType.BOOL) {
            String val = tableContent.get(rowIndex)[columnIndex];
            return !val.equals("0");
        } else if (type == Common.DBType.OBJECT) {
            return getIconObject(tableContent.get(rowIndex)[columnIndex]);
        } else {
            return tableContent.get(rowIndex)[columnIndex];
        }
    }

    public String getColumnName(int column){
        return fieldsSet.getFieldSet().get(column).getTitle();
    }

    public Object getIconObject(Object obj) {
        return obj;
    }

    public Class<?> getColumnClass(int columnIndex) {
    	Common.DBType type = fieldsSet.getFieldSet().get(columnIndex).getType();

        if (type == Common.DBType.STRING) {
            return java.lang.String.class;
        } else if (type == Common.DBType.INTEGER) {
            return java.lang.Integer.class;
        } else if (type == Common.DBType.BOOL) {
            return java.lang.Boolean.class;
        } else if (type == Common.DBType.OBJECT) {
            return Icon.class;
        } else {
            return java.lang.Object.class;
        }
    }
}
