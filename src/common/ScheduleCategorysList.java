package common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleCategorysList {
    private List<ScheduleCategory> data;

    public ScheduleCategorysList() {
        this.data = new ArrayList<>();
    }

    public List<ScheduleCategory> getData() {
        return data;
    }

    public void saveToDB() {
        for (ScheduleCategory cp : getData()) {
            DBParams[] aParams = new DBParams[5];
            aParams[0] = new DBParams(1, cp.getNameEN(), CommonTypes.DBType.STRING);
            aParams[1] = new DBParams(2, cp.getNameRU(), CommonTypes.DBType.STRING);
            aParams[2] = new DBParams(3, cp.getDictionary(), CommonTypes.DBType.STRING);
            aParams[3] = new DBParams(4, cp.getColor(), CommonTypes.DBType.STRING);
            aParams[4] = new DBParams(5, cp.getId(), CommonTypes.DBType.INTEGER);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_CATEGORY_UPDATE, aParams);
        }
    }

    public void loadFromDB() {
        getData().clear();
        Connection connection = DBUtils.getConnection(DBUtils.DB_DEST);
        if (connection != null) {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(DBUtils.SQL_CATEGORY);
                try {
                    while (rs.next()) {
                        ScheduleCategory cp = new ScheduleCategory(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                        getData().add(cp);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                } finally {
                	connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}
