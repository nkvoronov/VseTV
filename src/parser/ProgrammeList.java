package parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import common.CommonTypes;
import common.DBParams;
import common.DBUtils;
import common.ProgressMonitor;
import common.UtilStrings;
import gui.Messages;

public class ProgrammeList {
    private List<Programme> data;
    private Formatter format;

    public ProgrammeList() {
        this.data = new ArrayList<>();
    }

    public List<Programme> getData() {
        return data;
    }

    public void saveToDB(ProgressMonitor monitor) {
        int userChannelId;
        int scheduleID;
        int categoryID;
        int i = 1;
        monitor.setTotal(this.data.size());
        SimpleDateFormat ftdt = new SimpleDateFormat(UtilStrings.DATE_FORMATTIME);
        for (Programme prg : this.data) {
            userChannelId = getUserChannelID(prg.getChannelIdx());
            categoryID = saveToDBCategory(prg);
            DBParams[] aParams = new DBParams[5];
            aParams[0] = new DBParams(1, userChannelId, CommonTypes.DBType.INTEGER);
            aParams[1] = new DBParams(2, categoryID, CommonTypes.DBType.INTEGER);
            aParams[2] = new DBParams(3, ftdt.format(prg.getStart()), CommonTypes.DBType.STRING);
            aParams[3] = new DBParams(4, ftdt.format(prg.getStop()), CommonTypes.DBType.STRING);
            aParams[4] = new DBParams(5, prg.getTitle(), CommonTypes.DBType.STRING);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SCHEDULE_INSERT, aParams);
            scheduleID = getInsertScheduleID(prg);
            saveToDBDescription(scheduleID, prg);
            format = new Formatter();
            format.format(Messages.getString("StrSaveToBD"), i, this.data.size());
            monitor.setCurrent(format.toString(), i);
            i++;
        }
    }

    private int getCategoryID(String nameEN, String nameRU) {
        DBParams[] aParams = new DBParams[2];
        aParams[0] = new DBParams(1, nameEN, CommonTypes.DBType.STRING);
        aParams[1] = new DBParams(2, nameRU, CommonTypes.DBType.STRING);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_CATEGORY_ID, aParams);
    }

    private int saveToDBCategory(Programme prg) {
        int categoryID = 0;
        if (prg.getCategoryLangEN() != null && prg.getCategoryLangRU() != null) {
            categoryID = getCategoryID(prg.getCategoryLangEN(), prg.getCategoryLangRU());
            if (categoryID == -1) {
                DBParams[] aParams = new DBParams[2];
                aParams[0] = new DBParams(1, prg.getCategoryLangEN(), CommonTypes.DBType.STRING);
                aParams[1] = new DBParams(2, prg.getCategoryLangRU(), CommonTypes.DBType.STRING);
                DBUtils.getExecutePreparedUpdate(DBUtils.SQL_CATEGORY_INSERT, aParams);
                categoryID = getCategoryID(prg.getCategoryLangEN(), prg.getCategoryLangRU());
            }
        }
        return categoryID;
    }

    private int getDescriptionID(String desc) {
        DBParams[] aParams = new DBParams[1];
        aParams[0] = new DBParams(1, desc, CommonTypes.DBType.STRING);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_DESCRIPTION_ID, aParams);
    }

    private void saveToDBDescription(int scheduleID, Programme prg) {
        int descriptionID;
        if (prg.getDesc() != null) {
            descriptionID = getDescriptionID(prg.getDesc());
            if (descriptionID == -1) {
                DBParams[] aParams = new DBParams[5];
                aParams[0] = new DBParams(1, prg.getDesc(), CommonTypes.DBType.STRING);
                if (prg.getImage() != null) {
                    aParams[1] = new DBParams(2, prg.getImage(), CommonTypes.DBType.STRING);
                } else {
                    aParams[1] = new DBParams(2, "", CommonTypes.DBType.STRING);
                }
                if (prg.getCountry() != null) {
                    aParams[2] = new DBParams(3, prg.getCountry(), CommonTypes.DBType.STRING);
                } else {
                    aParams[2] = new DBParams(3, "", CommonTypes.DBType.STRING);
                }
                if (prg.getDate() != null) {
                    aParams[3] = new DBParams(4, prg.getDate(), CommonTypes.DBType.STRING);
                } else {
                    aParams[3] = new DBParams(4, "", CommonTypes.DBType.STRING);
                }
                if (prg.getStarrating() != null) {
                    aParams[4] = new DBParams(5, prg.getStarrating(), CommonTypes.DBType.STRING);
                } else {
                    aParams[4] = new DBParams(5, "", CommonTypes.DBType.STRING);
                }
                DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DESCRIPTION_INSERT, aParams);
                descriptionID = getDescriptionID(prg.getDesc());
            }
            saveToDBGenre(descriptionID, prg);
            saveToDBCredits(descriptionID, prg);
            DBParams[] aParams = new DBParams[2];
            aParams[0] = new DBParams(1, scheduleID, CommonTypes.DBType.INTEGER);
            aParams[1] = new DBParams(2, descriptionID, CommonTypes.DBType.INTEGER);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SCHEDULE_DESCRIPTION_INSERT, aParams);
        }
    }

    private int getGenreID(String name) {
        DBParams[] aParams = new DBParams[1];
        aParams[0] = new DBParams(1, name, CommonTypes.DBType.STRING);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_GENRE_ID, aParams);
    }

    private void saveToDBCategoryList(String[] list, int descriptionID) {
        int genreID;
        for (String str:list) {
            genreID = getGenreID(str);
            if (genreID == -1) {
                DBParams[] aParams = new DBParams[1];
                aParams[0] = new DBParams(1, str, CommonTypes.DBType.STRING);
                DBUtils.getExecutePreparedUpdate(DBUtils.SQL_GENRE_INSERT, aParams);
                genreID = getGenreID(str);
            }
            DBParams[] aParams = new DBParams[2];
            aParams[0] = new DBParams(1, descriptionID, CommonTypes.DBType.INTEGER);
            aParams[1] = new DBParams(2, genreID, CommonTypes.DBType.INTEGER);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_GENRE_DESCRIPTION_INSERT, aParams);
        }
    }

    private void saveToDBGenre(int descriptionID, Programme prg) {
        if (prg.getGenres() != null) {
            String[] listgr = prg.getGenres().split(Programme.SEP_LIST);
            saveToDBCategoryList(listgr, descriptionID);
        }
    }

    private int getCreditID(String name, int type) {
        DBParams[] aParams = new DBParams[2];
        aParams[0] = new DBParams(1, name, CommonTypes.DBType.STRING);
        aParams[1] = new DBParams(2, type, CommonTypes.DBType.INTEGER);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_CREDITS_ID, aParams);
    }

    private void saveToDBCreditList(String[] list, int type, int descriptionID) {
        int creditID;
        for (String str:list) {
            creditID = getCreditID(str, type);
            if (creditID == -1) {
                DBParams[] aParams = new DBParams[2];
                aParams[0] = new DBParams(1, str, CommonTypes.DBType.STRING);
                aParams[1] = new DBParams(2, type, CommonTypes.DBType.INTEGER);
                DBUtils.getExecutePreparedUpdate(DBUtils.SQL_CREDITS_INSERT, aParams);
                creditID = getCreditID(str, type);
            }
            DBParams[] aParams = new DBParams[2];
            aParams[0] = new DBParams(1, descriptionID, CommonTypes.DBType.INTEGER);
            aParams[1] = new DBParams(2, creditID, CommonTypes.DBType.INTEGER);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_CREDITS_DESCRIPTION_INSERT, aParams);
        }
    }

    private void saveToDBCredits(int descriptionID, Programme prg) {
        if (prg.getDirectors() != null) {
            String[] listdir = prg.getDirectors().split(Programme.SEP_LIST);
            saveToDBCreditList(listdir, 0, descriptionID);
        }
        if (prg.getActors() != null) {
            String[] listact = prg.getActors().split(Programme.SEP_LIST);
            saveToDBCreditList(listact, 1, descriptionID);
        }
    }

    private int getInsertScheduleID(Programme prg) {
        SimpleDateFormat ftdt = new SimpleDateFormat(UtilStrings.DATE_FORMATTIME);
        DBParams[] aParams = new DBParams[3];
        aParams[0] = new DBParams(1, ftdt.format(prg.getStart()), CommonTypes.DBType.STRING);
        aParams[1] = new DBParams(2, ftdt.format(prg.getStop()), CommonTypes.DBType.STRING);
        aParams[2] = new DBParams(3, prg.getTitle(), CommonTypes.DBType.STRING);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_MAINSCHEDULE_ID, aParams);
    }

    private int getUserChannelID(int channelIdx) {
        DBParams[] aParams = new DBParams[1];
        aParams[0] = new DBParams(1, channelIdx, CommonTypes.DBType.INTEGER);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_MAINUSERCHANNELS_ID, aParams);
    }

    public void setProgrammeStop() {
        int i = 0;
        while (i != this.data.size()) {
            Programme prg1 = this.data.get(i);
            Programme prg2;
            if (i + 1 != this.data.size()) {
                prg2 = this.data.get(i+1);
                if (prg1.getChannelIdx() == prg2.getChannelIdx()) {
                    prg1.setStop(prg2.getStart());
                }
            }
            i++;
        }
    }

    public int clearDBSchedule() {
        return DBUtils.getExecuteUpdate(DBUtils.SQL_MAINSCHEDULE_CLEAR);
    }

    public Programme getProgrammeForUrl(String url) {
        for (Programme prg : this.data) {
            if (prg.getUrlFullDesc().equals(url)) {
                return prg;
            }
        }
        return null;
    }

    public void print() {
        for (Programme prg : this.data) {
            prg.print();
        }
    }

    public void getXML(org.w3c.dom.Document document, org.w3c.dom.Element element) {
        for (Programme prg : this.data) {
            prg.getXML(document, element);
        }
    }
}
