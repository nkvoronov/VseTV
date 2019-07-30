package common;

import java.sql.*;

public class DBUtils {

	public static final String CLASS_NAME = "org.sqlite.JDBC";
    public static final String URL_PRE = "jdbc:sqlite:";
    public static final String DB_DEST = "vsetv.db";

    public static final String SQL_CATEGORY = "select * from categorys";
    public static final String SQL_CATEGORY_ID = "select id from categorys where name_en=? and name_ru=?";
    public static final String SQL_CATEGORY_INSERT = "insert into categorys (name_en,name_ru) values(?,?)";
    public static final String SQL_CATEGORY_UPDATE ="update categorys set name_en=?, name_ru=?, dictionary=?, color=? where id=?";
    public static final String SQL_DESCRIPTION_ID = "select id from description where description=?";
    public static final String SQL_DESCRIPTION_INSERT =
            "insert into description " +
            "(description, image, country, date, rating) " +
            "values (?, ?, ?, ?, ?)";

    public static final String SQL_GENRE_ID = "select id from genres where name=?";
    public static final String SQL_GENRE_INSERT = "insert into genres (name) values(?)";
    public static final String SQL_GENRE_DESCRIPTION_INSERT = "insert into description_genres (description,genre) values(?,?)";

    public static final String SQL_CREDITS_ID = "select id from credits where name=? and type=?";
    public static final String SQL_CREDITS_INSERT = "insert into credits (name,type) values(?,?)";
    public static final String SQL_CREDITS_DESCRIPTION_INSERT = "insert into description_credits (description,credit) values(?,?)";

    public static final String SQL_MAINUSERCHANNELS =
            "select uchn.id as id, " +
            "uchn.icon as picon, " +
            "uchn.name as uname, " +
            "case when ((julianday('now')-julianday(uchn.upd_date))>" + CommonTypes.COUNT_DAY +") or (uchn.upd_date is null) then 0 else 1 end as isupd " +            
            "from user_channels uchn " +
            "order by uchn.id";

    public static final String SQL_MAINUSERCHANNELS_UPDDATE = "update user_channels set upd_date=datetime('now') where id=(select uchn.id from user_channels uchn join channels chn on (chn.id=uchn.channel) where chn.cindex=?)";

    public static final String SQL_MAINUSERCHANNELS_ID = "select uchn.id as id, chn.cindex as idx from user_channels uchn join channels chn on (chn.id=uchn.channel) where chn.cindex=?";

    public static final String SQL_MAINSCHEDULE_CLEAR = "delete from schedule";

    public static final String SQL_MAINSCHEDULE_ID = "select id from schedule where starting=? and ending=? and title=?";

    public static final String SQL_MAINSCHEDULE =
            "select " +
            "sch.id as id, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then '1' else '0' end as isdesc, " +
            "case when (select sf.id from schedule_favorites sf where sf.schedule=sch.id) is not null then '1' else '0' end as isfav, " +
			"case when (sch.starting <= datetime('now','localtime')) and (sch.ending >= datetime('now','localtime')) then \"NOW\" when sch.starting <= datetime('now','localtime') then \"OLD\" else \"NEW\" end as timetype, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "where sch.channel=? " +
            "order by sch.starting";
    
    
    public static final String SQL_NOWSCHEDULE =
            "select " +
            "sch.id as id, " +
            "usch.icon as picon, " +
            "usch.name as uname, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then '1' else '0' end as isdesc, " +
            "case when (select sf.id from schedule_favorites sf where sf.schedule=sch.id) is not null then '1' else '0' end as isfav, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "join user_channels usch on (usch.id=sch.channel) " +
            "where (sch.starting <= datetime('now','localtime')) and (sch.ending > datetime('now','localtime')) " +
            "order by usch.id";
    
    public static final String SQL_NEXTSCHEDULE =
            "select " +
            "sch.id as id, " +
            "usch.icon as picon, " +
            "usch.name as uname, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then '1' else '0' end as isdesc, " +
            "case when (select sf.id from schedule_favorites sf where sf.schedule=sch.id) is not null then '1' else '0' end as isfav, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "join user_channels usch on (usch.id=sch.channel) " +
            "where (sch.starting > datetime('now','localtime')) " +
            "group by usch.id HAVING min(sch.starting)";
    
    public static final String SQL_FAVORITES =
            "select " +
            "sch.id as id, " +
            "usch.icon as picon, " +
            "usch.name as uname, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then '1' else '0' end as isdesc, " +
            "case when (select sf.id from schedule_favorites sf where sf.schedule=sch.id) is not null then '1' else '0' end as isfav, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "join user_channels usch on (usch.id=sch.channel) " +
            "join schedule_favorites sf on (sf.schedule=sch.id)";
    
    public static final String SQL_SCHEDULE_FAVORITES_INSERT =
            "insert into schedule_favorites " +
            "(schedule) " +
            "values (?)";
    
    public static final String SQL_DEL_SCHEDULE_FAVORITES = "delete from schedule_favorites where id=?";

    public static final String SQL_DEL_ALL_SCHEDULE_FAVORITES = "delete from schedule_favorites";
    
    public static final String SQL_MAINSCHEDULE_DESCRIPTION =
            "select desc.description, desc.image, desc.country, desc.date, desc.rating from description desc " +
            "where desc.id=(select sd.description from schedule_description sd where sd.schedule=? )";

    public static final String SQL_SCHEDULE_INSERT =
            "insert into schedule " +
            "(channel, category, starting, ending, title) " +
            "values (?, ?, ?, ?, ?)";
    
    public static final String SQL_SCHEDULE_DESCRIPTION_INSERT = "insert into schedule_description (schedule, description) values (?,?)";

    public static final String SQL_FINDINCHANNELS_INDEX = "select * from channels where cindex=?";

    public static final String SQL_CHANNELS =
            "select chn.id as id, " +
            "chn.cindex as idx, " +
            "chn.icon as sicon, " +
            "case when (select usr.id from user_channels usr where usr.channel=chn.id) is null then 0 else 1 end as users, " +
            "chn.icon as picon, " +
            "chn.name as name " +
            "from channels chn " +
            "order by chn.cindex";

    public static final String SQL_ADD_CHANNEL =
            "insert into user_channels (channel, name, icon) " +
            "select id, name, '" + CommonTypes.getIconsPatch() + "' || cindex || '.gif' as sicon from channels " +
            "where id=? and (select usr.id from user_channels usr where usr.channel=?) is null";

    public static final String SQL_ADD_ALLCHANNELS =
            "insert into user_channels (channel, name, icon) " +
            "select id, name, '" + CommonTypes.getIconsPatch() + "' || cindex || '.gif' as sicon from channels " +
            "where id not in (select usr.channel from user_channels usr)";

    public static final String SQL_INS_CHANNEL =
            "insert into channels (cindex, name, icon) " +
            "values(?,?,?)";

    public static final String SQL_EDT_CHANNEL =
            "update channels " +
            "set cindex=?, name=?, icon=?, upd_date=datetime('now') " +
            "where id=?";

    public static final String SQL_UPD_CHANNELNAME =
            "update channels " +
            "set name=?, icon=?, upd_date=datetime('now') " +
            "where cindex=?";

    public static final String SQL_UPD_CHANNELDATE =
            "update channels " +
            "set upd_date=datetime('now') " +
            "where cindex=?";

    public static final String SQL_UPD_CHANNELDATE_NULL = "update channels set upd_date=null";

    public static final String SQL_DEL_CHANNELDATE_NULL = "delete from channels where upd_date is null";

    public static final String SQL_DEL_CHANNELS = "delete from channels where id=?";

    public static final String SQL_DEL_ALLCHANNELS = "delete from channels";

    public static final String SQL_USERCHANNELS =
            "select uchn.id as id, " +
            "uchn.icon as sicon, " +
            "uchn.icon as picon, " +
            "uchn.name as name, " +
            "uchn.correction as correction " +
            "from user_channels uchn " +
            "order by uchn.id";

    public static final String SQL_DEL_USERCHANNELS = "delete from user_channels where id=?";

    public static final String SQL_DEL_ALLUSERCHANNELS = "delete from user_channels";

    public static final String SQL_EDT_USERCHANNELS =
            "update user_channels " +
            "set name=?, icon=?, correction=? " +
            "where id=?";

    public static final String SQL_SET_CORRECTIONUSERCHANNELS = "update user_channels set correction=?";
    
    
    private DBUtils() {
    	// do not instantiate
	}

    public static Connection getConnection(String url) {
        Connection conn = null;
        String furl = URL_PRE + url;
        try {
            try {
                Class.forName(CLASS_NAME);
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
        Connection conn = getConnection(DB_DEST);
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

    public static void setParams(PreparedStatement pStatement, DBParams[] aParams) throws SQLException {
        for (DBParams Par:aParams) {
            if (Par.getParamType() == CommonTypes.DBType.INTEGER) {
            	pStatement.setInt(Par.getNumber(), (Integer) Par.getValue());
            } else if (Par.getParamType() == CommonTypes.DBType.STRING) {
            	pStatement.setString(Par.getNumber(), (String) Par.getValue());
            } else if (Par.getParamType() == CommonTypes.DBType.BOOL) {
            	pStatement.setBoolean(Par.getNumber(), (Boolean) Par.getValue());
            } else if (Par.getParamType() == CommonTypes.DBType.DATETIME) {
            	pStatement.setDate(Par.getNumber(), (Date) Par.getValue());
            } else if (Par.getParamType() == CommonTypes.DBType.TIME) {
            	pStatement.setDate(Par.getNumber(), (Date) Par.getValue());
            }
        }
    }

    public static int getIdForPreparedStatement(String sSQL, DBParams[] aParams) {
        int res = -1;
        ResultSet rs;
        PreparedStatement pstmt;
        Connection conn = getConnection(DB_DEST);
        if (conn != null) {
            try {
                pstmt = conn.prepareStatement(sSQL);
                setParams(pstmt, aParams);
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

    public static int getExecutePreparedUpdate(String sSQL, DBParams[] aParams) {
        int res = -1;
        Connection conn = getConnection(DB_DEST);
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(sSQL);
                setParams(pstmt, aParams);
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
