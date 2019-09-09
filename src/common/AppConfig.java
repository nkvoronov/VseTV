package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppConfig {
    private static final int DEF_ID = 1;
    public static final int COUNT_DAY = 7;
    public static final boolean FULL_DESC = false;
    
    private int coutDays;
    private boolean fullDesc;
    
    public AppConfig() {
        initConfig();
    }
    
    private void initConfig() {
        this.coutDays = COUNT_DAY;
        this.fullDesc = FULL_DESC;
        int id;
        Connection connection = DBUtils.getConnection(DBUtils.DB_DEST);
        if (connection != null) {
            try {
            	PreparedStatement pstmt = connection.prepareStatement(DBUtils.SQL_CONFIG);
                ResultSet rs = pstmt.executeQuery();
                try {
                	while (rs.next()) {
                		id = rs.getInt(1);
	            		this.coutDays = rs.getInt(2);
	            		this.fullDesc = rs.getInt(3) == 1; 
	            		if (id == DEF_ID) {
	            			break;
	            		}
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

	public int getCoutDays() {
		return coutDays;
	}

	public void setCoutDays(int coutDays) {
		this.coutDays = coutDays;
		updateConfig();
	}

	public boolean isFullDesc() {
		return fullDesc;
	}

	public void setFullDesc(boolean fullDesc) {
		this.fullDesc = fullDesc;
		updateConfig();
	}

	private void updateConfig() {
		DBParams[] aParams = new DBParams[3];
		aParams[0] = new DBParams(1, this.coutDays, CommonTypes.DBType.INTEGER);
		if (this.fullDesc) {
			aParams[1] = new DBParams(2, 1, CommonTypes.DBType.INTEGER);
		} else {
			aParams[1] = new DBParams(2, 0, CommonTypes.DBType.INTEGER);
		}
		aParams[2] = new DBParams(3, DEF_ID, CommonTypes.DBType.INTEGER);
        DBUtils.getExecutePreparedUpdate(DBUtils.SQL_UPD_CONFIG, aParams);
	}
    
}
