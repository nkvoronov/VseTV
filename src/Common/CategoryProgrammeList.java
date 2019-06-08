package Common;

import java.sql.*;
import java.util.ArrayList;
import static Common.DBUtils.*;

public class CategoryProgrammeList {
    private ArrayList<CategoryProgramme> Data;

    public CategoryProgrammeList() {
        this.Data = new ArrayList<>();
    }

    public ArrayList<CategoryProgramme> getData() {
        return Data;
    }

    public void saveToDB() {
        for (CategoryProgramme cp : getData()) {
            DBParams[] Params = new DBParams[5];
            Params[0] = new DBParams(1, cp.getName_en(), Common.DBType.STRING);
            Params[1] = new DBParams(2, cp.getName_ru(), Common.DBType.STRING);
            Params[2] = new DBParams(3, cp.getDictionary(), Common.DBType.STRING);
            Params[3] = new DBParams(4, cp.getColor(), Common.DBType.STRING);
            Params[4] = new DBParams(5, cp.getId(), Common.DBType.INTEGER);
            getExecutePreparedUpdate(sqlCategoryUpdate, Params);
        }
    }

    public void loadFromDB() {
        getData().clear();
        Connection conn = getConnection(DBUtils.DBDest);
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(DBUtils.sqlCategory);
                try {
                    while (rs.next()) {
                        CategoryProgramme cp = new CategoryProgramme(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                        getData().add(cp);
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
}
