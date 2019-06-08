package Parser;

import Common.DBParams;
import Common.DBUtils;
import Common.ProgressMonitor;
import Common.Common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;

import static Common.DBUtils.*;
import static Common.Strings.*;

public class ProgrammeList {
    private ArrayList<Programme> Data;
    private Formatter format;

    public ProgrammeList() {
        this.Data = new ArrayList<>();
    }

    public ArrayList<Programme> getData() {
        return Data;
    }

    public void saveToDB(ProgressMonitor Monitor) {
        int UserChannelId;
        int ScheduleID;
        int CategoryID;
        int i = 1;
        Monitor.setTotal(this.Data.size());
        SimpleDateFormat ftdt = new SimpleDateFormat(DateFormatTime);
        for (Programme prg : this.Data) {
            UserChannelId = getUserChannelID(prg.getChannelIdx());
            CategoryID = saveToDBCategory(prg);
            DBParams[] Params = new DBParams[5];
            Params[0] = new DBParams(1, UserChannelId, Common.DBType.INTEGER);
            Params[1] = new DBParams(2, CategoryID, Common.DBType.INTEGER);
            Params[2] = new DBParams(3, ftdt.format(prg.getStart()), Common.DBType.STRING);
            Params[3] = new DBParams(4, ftdt.format(prg.getStop()), Common.DBType.STRING);
            Params[4] = new DBParams(5, prg.getTitle(), Common.DBType.STRING);
            getExecutePreparedUpdate(sqlInsertSchedule, Params);
            ScheduleID = getInsertScheduleID(prg);
            saveToDBDescription(ScheduleID,prg);
            format = new Formatter();
            format.format(StrSaveToBD, i, this.Data.size());
            Monitor.setCurrent(format.toString(), i);
            i++;
        }
    }

    private int getCategoryID(String name_en, String name_ru) {
        DBParams[] Params = new DBParams[2];
        Params[0] = new DBParams(1, name_en, Common.DBType.STRING);
        Params[1] = new DBParams(2, name_ru, Common.DBType.STRING);
        return getIdForPreparedStatement(sqlCategoryGetId,Params);
    }

    private int saveToDBCategory(Programme prg) {
        int CategoryID = 0;
        if (prg.getCategoryLangEN() != null && prg.getCategoryLangRU() != null) {
            CategoryID = getCategoryID(prg.getCategoryLangEN(), prg.getCategoryLangRU());
            if (CategoryID == -1) {
                DBParams[] Params = new DBParams[2];
                Params[0] = new DBParams(1, prg.getCategoryLangEN(), Common.DBType.STRING);
                Params[1] = new DBParams(2, prg.getCategoryLangRU(), Common.DBType.STRING);
                getExecutePreparedUpdate(sqlCategoryInsert, Params);
                CategoryID = getCategoryID(prg.getCategoryLangEN(), prg.getCategoryLangRU());
            }
        }
        return CategoryID;
    }

    private int getDescriptionID(String Desc) {
        DBParams[] Params = new DBParams[1];
        Params[0] = new DBParams(1, Desc, Common.DBType.STRING);
        return getIdForPreparedStatement(sqlDescriptionGetId,Params);
    }

    private void saveToDBDescription(int ScheduleID, Programme prg) {
        int DescriptionID;
        if (prg.getDesc() != null) {
            DescriptionID = getDescriptionID(prg.getDesc());
            if (DescriptionID == -1) {
                DBParams[] Params = new DBParams[5];
                Params[0] = new DBParams(1, prg.getDesc(), Common.DBType.STRING);
                if (prg.getImage() != null) {
                    Params[1] = new DBParams(2, prg.getImage(), Common.DBType.STRING);
                } else {
                    Params[1] = new DBParams(2, "", Common.DBType.STRING);
                }
                if (prg.getCountry() != null) {
                    Params[2] = new DBParams(3, prg.getCountry(), Common.DBType.STRING);
                } else {
                    Params[2] = new DBParams(3, "", Common.DBType.STRING);
                }
                if (prg.getDate() != null) {
                    Params[3] = new DBParams(4, prg.getDate(), Common.DBType.STRING);
                } else {
                    Params[3] = new DBParams(4, "", Common.DBType.STRING);
                }
                if (prg.getStarrating() != null) {
                    Params[4] = new DBParams(5, prg.getStarrating(), Common.DBType.STRING);
                } else {
                    Params[4] = new DBParams(5, "", Common.DBType.STRING);
                }
                getExecutePreparedUpdate(sqlDescriptionInsert, Params);
                DescriptionID = getDescriptionID(prg.getDesc());
            }
            saveToDBGenre(DescriptionID, prg);
            saveToDBCredits(DescriptionID, prg);
            DBParams[] Params = new DBParams[2];
            Params[0] = new DBParams(1, ScheduleID, Common.DBType.INTEGER);
            Params[1] = new DBParams(2, DescriptionID, Common.DBType.INTEGER);
            getExecutePreparedUpdate(sqlScheduleDescriptionInsert,Params);
        }
    }

    private int getGenreID(String name) {
        DBParams[] Params = new DBParams[1];
        Params[0] = new DBParams(1, name, Common.DBType.STRING);
        return getIdForPreparedStatement(sqlGenreGetId,Params);
    }

    private void saveToDBCategoryList(String[] list, int DescriptionID) {
        int GenreID;
        for (String str:list) {
            GenreID = getGenreID(str);
            if (GenreID == -1) {
                DBParams[] Params = new DBParams[1];
                Params[0] = new DBParams(1, str, Common.DBType.STRING);
                getExecutePreparedUpdate(sqlGenreInsert,Params);
                GenreID = getGenreID(str);
            }
            DBParams[] Params = new DBParams[2];
            Params[0] = new DBParams(1, DescriptionID, Common.DBType.INTEGER);
            Params[1] = new DBParams(2, GenreID, Common.DBType.INTEGER);
            getExecutePreparedUpdate(sqlGenreDescriptionInsert,Params);
        }
    }

    private void saveToDBGenre(int DescriptionID, Programme prg) {
        if (prg.getGenres() != null) {
            String[] listgr = prg.getGenres().split(Programme.sepLists);
            saveToDBCategoryList(listgr, DescriptionID);
        }
    }

    private int getCreditID(String name, int type) {
        DBParams[] Params = new DBParams[2];
        Params[0] = new DBParams(1, name, Common.DBType.STRING);
        Params[1] = new DBParams(2, type, Common.DBType.INTEGER);
        return getIdForPreparedStatement(sqlCreditsGetId,Params);
    }

    private void saveToDBCreditList(String[] list, int type, int DescriptionID) {
        int CreditID;
        for (String str:list) {
            CreditID = getCreditID(str, type);
            if (CreditID == -1) {
                DBParams[] Params = new DBParams[2];
                Params[0] = new DBParams(1, str, Common.DBType.STRING);
                Params[1] = new DBParams(2, type, Common.DBType.INTEGER);
                getExecutePreparedUpdate(sqlCreditsInsert,Params);
                CreditID = getCreditID(str, type);
            }
            DBParams[] Params = new DBParams[2];
            Params[0] = new DBParams(1, DescriptionID, Common.DBType.INTEGER);
            Params[1] = new DBParams(2, CreditID, Common.DBType.INTEGER);
            getExecutePreparedUpdate(sqlCreditsDescriptionInsert,Params);
        }
    }

    private void saveToDBCredits(int DescriptionID, Programme prg) {
        if (prg.getDirectors() != null) {
            String[] listdir = prg.getDirectors().split(Programme.sepLists);
            saveToDBCreditList(listdir, 0, DescriptionID);
        }
        if (prg.getActors() != null) {
            String[] listact = prg.getActors().split(Programme.sepLists);
            saveToDBCreditList(listact, 1, DescriptionID);
        }
    }

    private int getInsertScheduleID(Programme prg) {
        SimpleDateFormat ftdt = new SimpleDateFormat(DateFormatTime);
        DBParams[] Params = new DBParams[3];
        Params[0] = new DBParams(1, ftdt.format(prg.getStart()), Common.DBType.STRING);
        Params[1] = new DBParams(2, ftdt.format(prg.getStop()), Common.DBType.STRING);
        Params[2] = new DBParams(3, prg.getTitle(), Common.DBType.STRING);
        return getIdForPreparedStatement(sqlMainScheduleGetId,Params);
    }

    private int getUserChannelID(int ChannelIdx) {
        DBParams[] Params = new DBParams[1];
        Params[0] = new DBParams(1, ChannelIdx, Common.DBType.INTEGER);
        return getIdForPreparedStatement(sqlMainUserChannelsGetId,Params);
    }

    public void setProgrammeStop() {
        int i = 0;
        while (i != this.Data.size()) {
            Programme prg1 = this.Data.get(i);
            Programme prg2;
            if (i + 1 != this.Data.size()) {
                prg2 = this.Data.get(i+1);
                if (prg1.getChannelIdx() == prg2.getChannelIdx()) {
                    prg1.setStop(prg2.getStart());
                }
            }
            i++;
        }
    }

    public int clearDBSchedule() {
        return getExecuteUpdate(DBUtils.sqlMainScheduleClear);
    }

    public Programme getProgrammeForUrl(String Url) {
        for (Programme prg : this.Data) {
            if (prg.getUrlFullDesc().equals(Url)) {
                return prg;
            }
        }
        return null;
    }

    public void print() {
        for (Programme prg : this.Data) {
            prg.print();
        }
    }

    public void getXML(org.w3c.dom.Document document, org.w3c.dom.Element element) {
        for (Programme prg : this.Data) {
            prg.getXML(document, element);
        }
    }
}
