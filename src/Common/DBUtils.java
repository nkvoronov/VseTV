package Common;

import java.sql.*;

import static Common.Common.getIconsPatch;

public class DBUtils {

    public static final String urlPre = "jdbc:sqlite:";
    public static final String DBDest = "vsetv.db";

    public static final String sqlCategory = "select * from categorys";
    public static final String sqlCategoryGetId = "select id from categorys where name_en=? and name_ru=?";
    public static final String sqlCategoryInsert = "insert into categorys (name_en,name_ru) values(?,?)";
    public static final String sqlCategoryUpdate =
            "update categorys " +
            "set name_en=?, name_ru=?, dictionary=?, color=? " +
            "where id=?";

    public static final String sqlDescriptionGetId = "select id from description where description=?";
    public static final String sqlDescriptionInsert =
            "insert into description " +
            "(description, image, country, date, rating) " +
            "values (?, ?, ?, ?, ?)";

    public static final String sqlGenreGetId = "select id from genres where name=?";
    public static final String sqlGenreInsert = "insert into genres (name) values(?)";
    public static final String sqlGenreDescriptionInsert = "insert into description_genres (description,genre) values(?,?)";

    public static final String sqlCreditsGetId = "select id from credits where name=? and type=?";
    public static final String sqlCreditsInsert = "insert into credits (name,type) values(?,?)";
    public static final String sqlCreditsDescriptionInsert = "insert into description_credits (description,credit) values(?,?)";

    public static final String sqlMainUserChannels =
            "select uchn.id as id, " +
            "chn.cindex as idx, " +
            "chn.icon as sicon, " +
            "chn.name as oname, " +
            "uchn.correction as correction, " +
            "case when ((julianday('now')-julianday(uchn.upd_date))>" + Common.CountDay +") or (uchn.upd_date is null) then 0 else 1 end as isupd, " +
            "uchn.icon as picon, " +
            "uchn.name as uname " +
            "from user_channels uchn " +
            "join channels chn on (chn.id=uchn.channel)";

    public static final String sqlMainUserChannelsUpdDate = "update user_channels set upd_date=datetime('now') where id=(select uchn.id from user_channels uchn join channels chn on (chn.id=uchn.channel) where chn.cindex=?)";

    public static final String sqlMainUserChannelsGetId = "select uchn.id as id, chn.cindex as idx from user_channels uchn join channels chn on (chn.id=uchn.channel) where chn.cindex=?";

    public static final String sqlMainScheduleClear = "delete from schedule";

    public static final String sqlMainScheduleGetId = "select id from schedule where starting=? and ending=? and title=?";

    public static final String sqlMainSchedule =
            "select " +
            "sch.id as id, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then '1' else '0' end as isdesc, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "where sch.channel=?";

    public static final String sqlMainScheduleDesc =
            "select desc.description, desc.image, desc.country, desc.date, desc.rating from description desc " +
            "where desc.id=(select sd.description from schedule_description sd where sd.schedule=? )";

    public static final String sqlInsertSchedule =
            "insert into schedule " +
            "(channel, category, starting, ending, title) " +
            "values (?, ?, ?, ?, ?)";

    public static final String sqlScheduleDescriptionInsert = "insert into schedule_description (schedule, description) values (?,?)";

    public static final String sqlFindInChannelsIndex = "select * from channels where cindex=?";

    public static final String sqlChannels =
            "select chn.id as id, " +
            "chn.cindex as idx, " +
            "chn.icon as sicon, " +
            "case when (select usr.id from user_channels usr where usr.channel=chn.id) is null then 0 else 1 end as users, " +
            "chn.icon as picon, " +
            "chn.name as name " +
            "from channels chn " +
            "order by chn.cindex";

    public static final String sqlAddChannel =
            "insert into user_channels (channel, name, icon) " +
            "select id, name, '" + getIconsPatch() + "' || cindex || '.gif' as sicon from channels " +
            "where id=? and (select usr.id from user_channels usr where usr.channel=?) is null";

    public static final String sqlAddAllChannels =
            "insert into user_channels (channel, name, icon) " +
            "select id, name, '" + getIconsPatch() + "' || cindex || '.gif' as sicon from channels " +
            "where id not in (select usr.channel from user_channels usr)";

    public static final String sqlInsChannel =
            "insert into channels (cindex, name, icon) " +
            "values(?,?,?)";

    public static final String sqlEdtChannel =
            "update channels " +
            "set cindex=?, name=?, icon=?, upd_date=datetime('now') " +
            "where id=?";

    public static final String sqlUpdChannelName =
            "update channels " +
            "set name=?, icon=?, upd_date=datetime('now') " +
            "where cindex=?";

    public static final String sqlUpdChannelDate =
            "update channels " +
            "set upd_date=datetime('now') " +
            "where cindex=?";

    public static final String sqlUpdChannelDateNull = "update channels set upd_date=null";

    public static final String sqlDelChannelDateNull = "delete from channels where upd_date is null";

    public static final String sqlDelChannels = "delete from channels where id=?";

    public static final String sqlDelAllChannels = "delete from channels";

    public static final String sqlUserChannels =
            "select uchn.id as id, " +
            "uchn.icon as sicon, " +
            "uchn.icon as picon, " +
            "uchn.name as name, " +
            "uchn.correction as correction " +
            "from user_channels uchn";

    public static final String sqlDelUserChannels = "delete from user_channels where id=?";

    public static final String sqlDelAllUserChannels = "delete from user_channels";

    public static final String sqlEdtUserChannels =
            "update user_channels " +
            "set name=?, icon=?, correction=? " +
            "where id=?";

    public static final String sqlSetCorrectionUserChannels = "update user_channels set correction=?";

    public static Connection getConnection(String url) {
        Connection conn = null;
        String furl = urlPre + url;
        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(furl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static int getExecuteUpdate(String sSQL) {
        int res = -1;
        Connection conn = getConnection(DBDest);
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                res = stmt.executeUpdate(sSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    public static void setParams(PreparedStatement Stmt, DBParams[] Params) throws SQLException {
        for (DBParams Par:Params) {
            if (Par.getParamType() == Common.DBType.INTEGER) {
                Stmt.setInt(Par.getNumber(), (Integer) Par.getValue());
            } else if (Par.getParamType() == Common.DBType.STRING) {
                Stmt.setString(Par.getNumber(), (String) Par.getValue());
            } else if (Par.getParamType() == Common.DBType.BOOL) {
                Stmt.setBoolean(Par.getNumber(), (Boolean) Par.getValue());
            } else if (Par.getParamType() == Common.DBType.DATETIME) {
                Stmt.setDate(Par.getNumber(), (Date) Par.getValue());
            } else if (Par.getParamType() == Common.DBType.TIME) {
                Stmt.setDate(Par.getNumber(), (Date) Par.getValue());
            }
        }
    }

    public static int getIdForPreparedStatement(String sSQL, DBParams[] Params) {
        int res = -1;
        ResultSet rs;
        PreparedStatement pstmt;
        Connection conn = getConnection(DBDest);
        if (conn != null) {
            try {
                pstmt = conn.prepareStatement(sSQL);
                setParams(pstmt, Params);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    res = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    public static int getExecutePreparedUpdate(String sSQL, DBParams[] Params) {
        int res = -1;
        Connection conn = getConnection(DBDest);
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(sSQL);
                setParams(pstmt, Params);
                res = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}
