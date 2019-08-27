package common;

import java.sql.*;

public class DBUtils {
	
	public static final int INDEX_ID = 0;
	public static final int INDEX_SCHELUDE_FAV = 1;
	
	public static final int INDEX_CHANNEL = 1;
	public static final int INDEX_CHANNEL_ICON_STR = 2;
	public static final int INDEX_IS_USER = 3;
	public static final int INDEX_CHANNEL_ICON = 4;
	public static final int INDEX_CHANNEL_NAME = 5;
	public static final int INDEX_LANG = 6;
	
	public static final int INDEX_UCHANNEL_ICON_STR = 1;
	public static final int INDEX_UCHANNEL_ICON = 2;
	public static final int INDEX_UCHANNEL_NAME = 3;
	public static final int INDEX_UCHANNEL_CORRECTION = 4;
		
	public static final int INDEX_MCHANNEL_ICON = 2;
	public static final int INDEX_MCHANNEL_NAME = 3;
	public static final int INDEX_MCHANNEL_UPD = 4;
	
	public static final int INDEX_ASCHELUDE_SDATE = 2;
	public static final int INDEX_ASCHELUDE_EDATE = 3;
	public static final int INDEX_ASCHELUDE_DURATION = 4;
	public static final int INDEX_ASCHELUDE_TITLE = 5;
	public static final int INDEX_ASCHELUDE_ISDESC = 6;
	public static final int INDEX_ASCHELUDE_TIMETYPE = 7;
	public static final int INDEX_ASCHELUDE_CAT_EN = 8;
	public static final int INDEX_ASCHELUDE_CAT_RU = 9;
	
	public static final int INDEX_NSCHELUDE_CICON = 2;
	public static final int INDEX_NSCHELUDE_CNAME = 3;
	public static final int INDEX_NSCHELUDE_SDATE = 4;
	public static final int INDEX_NSCHELUDE_EDATE = 5;
	public static final int INDEX_NSCHELUDE_DURATION = 6;
	public static final int INDEX_NSCHELUDE_TITLE = 7;
	public static final int INDEX_NSCHELUDE_ISDESC = 8;
	public static final int INDEX_NSCHELUDE_CAT_EN = 9;
	public static final int INDEX_NSCHELUDE_CAT_RU = 10;
	
	public static final int INDEX_FAVORITES_CICON = 1;
	public static final int INDEX_FAVORITES_CNAME = 2;
	public static final int INDEX_FAVORITES_SDATE = 3;
	public static final int INDEX_FAVORITES_EDATE = 4;
	public static final int INDEX_FAVORITES_DURATION = 5;
	public static final int INDEX_FAVORITES_TITLE = 6;
	public static final int INDEX_FAVORITES_ISDESC = 7;
	public static final int INDEX_FAVORITES_CAT_EN = 8;
	public static final int INDEX_FAVORITES_CAT_RU = 9;
	
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
            "uchn.channel as channel, " +
            "\"" + CommonTypes.TYPE_SOURCE_IMAGE_FILE + "\" || uchn.icon as uicon, " +
            "uchn.name as uname, " +
            "case when (select count(sch.id) from schedule sch where (sch.channel=uchn.channel) and (sch.starting>=datetime('now','localtime')))=0 then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "update_big.png\" else \"\" end as isupd " +            
            "from user_channels uchn " +
            "order by uchn.name";
    
    public static final String SQL_DBCHANNELS =
    		"select uchn.id as id, " +
    		"chn.channel as idx, " +
    	    "chn.name as oname, " +	
    		"uchn.name as uname, " +    		
    	    "uchn.icon as sicon, " +    		
    	    "uchn.correction as correction, " +
    	    "chn.lang as lang " +
    	    "from user_channels uchn " +
    	    "join channels chn on (chn.channel=uchn.channel)";

    public static final String SQL_MAINSCHEDULE_CLEAR = "delete from schedule";

    public static final String SQL_MAINSCHEDULE_ID = "select id from schedule where starting=? and ending=? and title=?";

    public static final String SQL_MAINSCHEDULE =
            "select " +
            "sch.id as id, " +
            "case when (select sf.id from schedule_favorites sf where sf.schedule=sch.id) is not null then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "favorites_big.png\" else \"\" end as isfav, " +		
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "description_big.png\" else \"\" end as isdesc, " +            
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
            "case when (select sf.id from schedule_favorites sf where sf.schedule=sch.id) is not null then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "favorites_big.png\" else \"\" end as isfav, " +
            "\"" + CommonTypes.TYPE_SOURCE_IMAGE_FILE + "\" || usch.icon as picon, " +
            "usch.name as uname, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "description_big.png\" else \"\" end as isdesc, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "join user_channels usch on (usch.channel=sch.channel) " +
            "where (sch.starting <= datetime('now','localtime')) and (sch.ending > datetime('now','localtime')) " +
            "order by usch.name";
    
    public static final String SQL_NEXTSCHEDULE =
            "select " +
            "sch.id as id, " +
            "case when (select sf.id from schedule_favorites sf where sf.schedule=sch.id) is not null then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "favorites_big.png\" else \"\" end as isfav, " +
            "\"" + CommonTypes.TYPE_SOURCE_IMAGE_FILE + "\" || usch.icon as picon, " +
            "usch.name as uname, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "description_big.png\" else \"\" end as isdesc, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "join user_channels usch on (usch.channel=sch.channel) " +
            "where (sch.starting > datetime('now','localtime')) " +
            "group by usch.id HAVING min(sch.starting) " + 
    		"order by usch.name";
    
    public static final String SQL_FAVORITES =
            "select " +
            "sch.id as id, " +
            "\"" + CommonTypes.TYPE_SOURCE_IMAGE_FILE + "\" || usch.icon as picon, " +
            "usch.name as uname, " +
            "strftime('%Y-%m-%d %H:%M',sch.starting) as sdate, " +
            "strftime('%Y-%m-%d %H:%M',sch.ending) as edate, " +
            "strftime('%s', sch.ending)-strftime('%s', sch.starting) as duration, " +
            "sch.title, " +
            "case when (select sd.id from schedule_description sd where sd.schedule=sch.id) is not null then \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "description_big.png\" else \"\" end as isdesc, " +
            "cat.name_en as cat_en, " +
            "cat.name_ru as cat_ru " +
            "from schedule sch " +
            "join categorys cat on (cat.id=sch.category) " +
            "join user_channels usch on (usch.channel=sch.channel) " +
            "join schedule_favorites sf on (sf.schedule=sch.id) " +
    		"order by usch.name";
    
    public static final String SQL_SCHEDULE_FAVORITES_INSERT =
            "insert into schedule_favorites " +
            "(schedule) " +
            "values (?)";
    
    public static final String SQL_DEL_SCHEDULE_FAVORITES = "delete from schedule_favorites where schedule=?";

    public static final String SQL_DEL_ALL_SCHEDULE_FAVORITES = "delete from schedule_favorites";
    
    public static final String SQL_MAINSCHEDULE_DESCRIPTION =
            "select desc.description, desc.image, desc.country, desc.date, desc.rating from description desc " +
            "where desc.id=(select sd.description from schedule_description sd where sd.schedule=? )";

    public static final String SQL_SCHEDULE_INSERT =
            "insert into schedule " +
            "(channel, category, starting, ending, title) " +
            "values (?, ?, ?, ?, ?)";
    
    public static final String SQL_SCHEDULE_DESCRIPTION_INSERT = "insert into schedule_description (schedule, description) values (?,?)";

    public static final String SQL_FINDINCHANNELS_INDEX = "select * from channels where channel=?";

    public static final String SQL_CHANNELS =
            "select chn.id as id, " +
            "chn.channel as channel, " +
            "chn.icon as ciconstr, " +
            "case when (select usr.id from user_channels usr where usr.channel=chn.channel) is null then \"\" else \"" + CommonTypes.TYPE_SOURCE_IMAGE_RES + "favorites_big.png\" end as isusr, " +
            "chn.icon as cicon, " +
            "chn.name as cname, " +
            "chn.lang as clang " +
            "from channels chn " +
            "where chn.lang=? or ?='all' " +
            "order by chn.channel";

    public static final String SQL_ADD_CHANNEL =
            "insert into user_channels (channel, name, icon) " +
            "select channel, name, '" + CommonTypes.getIconsPatch() + "' || channel || '.gif' as sicon from channels " +
            "where channel=? and (select usr.id from user_channels usr where usr.channel=?) is null";

    public static final String SQL_ADD_ALLCHANNELS =
            "insert into user_channels (channel, name, icon) " +
            "select channel, name, '" + CommonTypes.getIconsPatch() + "' || channel || '.gif' as sicon from channels " +
            "where id not in (select usr.channel from user_channels usr)";

    public static final String SQL_INS_CHANNEL =
            "insert into channels (channel, name, icon, lang) " +
            "values(?,?,?,?)";

    public static final String SQL_EDT_CHANNEL =
            "update channels " +
            "set channel=?, name=?, icon=?, lang=?, upd_date=datetime('now') " +
            "where id=?";

    public static final String SQL_UPD_CHANNELNAME =
            "update channels " +
            "set name=?, icon=?, lang=?, upd_date=datetime('now') " +
            "where channel=?";

    public static final String SQL_UPD_CHANNELDATE =
            "update channels " +
            "set upd_date=datetime('now') " +
            "where channel=?";

    public static final String SQL_UPD_CHANNELDATE_NULL = "update channels set upd_date=null";

    public static final String SQL_DEL_CHANNELDATE_NULL = "delete from channels where upd_date is null";

    public static final String SQL_DEL_CHANNELS = "delete from channels where id=?";

    public static final String SQL_DEL_ALLCHANNELS = "delete from channels";

    public static final String SQL_USERCHANNELS =
            "select uchn.id as id, " +
            "uchn.icon as uiconstr, " +
            "\"" + CommonTypes.TYPE_SOURCE_IMAGE_FILE + "\" || uchn.icon as uicon, " +
            "uchn.name as uname, " +
            "uchn.correction as ucorrection " +
            "from user_channels uchn " +
            "order by uchn.name";

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
            Class.forName(CLASS_NAME);
            conn = DriverManager.getConnection(furl);
        } catch (ClassNotFoundException|SQLException e) {
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
