package parser;

import java.util.ArrayList;
import java.util.List;
import common.CommonTypes;
import common.DBParams;
import common.DBUtils;
import common.ProgressMonitor;
import common.UtilStrings;
import gui.Messages;

public class ProgrammeList {
    private List<Programme> data;

    public ProgrammeList() {
        this.data = new ArrayList<>();
    }

    public List<Programme> getData() {
        return data;
    }
    
    public Programme get(int position) {
        return getData().get(position);
    }
    
    public int size() {
        return getData().size();
    }

    public void clear() {
        getData().clear();
    }

    public void add(Programme programme) {
        getData().add(programme);
    }

    public void saveToDB(ProgressMonitor monitor) {
        int i = 1;
        monitor.setTotal(this.data.size());
        for (Programme programme : getData()) {
            DBParams[] aParams = new DBParams[5];
            aParams[0] = new DBParams(1, programme.getIndex(), CommonTypes.DBType.INTEGER);
            aParams[1] = new DBParams(2, programme.getCategory(), CommonTypes.DBType.INTEGER);
            aParams[2] = new DBParams(3, CommonTypes.getDateFormat(programme.getStart(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
            aParams[3] = new DBParams(4, CommonTypes.getDateFormat(programme.getStop(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
            aParams[4] = new DBParams(5, programme.getTitle(), CommonTypes.DBType.STRING);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SCHEDULE_INSERT, aParams);
            int scheduleID = getInsertScheduleID(programme);
            saveToDBDescription(scheduleID, programme);
            monitor.setCurrent(String.format(Messages.getString("StrSaveToBD"), i, this.size()), i);
            i++;
        }
    }

    private int getDescriptionID(Programme programme) {
        DBParams[] aParams = new DBParams[2];
        aParams[0] = new DBParams(1, programme.getType(), CommonTypes.DBType.STRING);
        aParams[1] = new DBParams(2, programme.getCatalog(), CommonTypes.DBType.INTEGER);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_DESCRIPTION_ID, aParams);
    }

    private void saveToDBDescription(int scheduleID, Programme programme) {
        int descriptionID;
        if (programme.getDescription() != null) {
            descriptionID = getDescriptionID(programme);
            if (descriptionID == -1) {
                DBParams[] aParams = new DBParams[7];
                aParams[0] = new DBParams(1, programme.getDescription(), CommonTypes.DBType.STRING);
                if (programme.getImage() != null) {
                    aParams[1] = new DBParams(2, programme.getImage(), CommonTypes.DBType.STRING);
                } else {
                    aParams[1] = new DBParams(2, "", CommonTypes.DBType.STRING);
                }
                if (programme.getCountry() != null) {
                    aParams[2] = new DBParams(3, programme.getCountry(), CommonTypes.DBType.STRING);
                } else {
                    aParams[2] = new DBParams(3, "", CommonTypes.DBType.STRING);
                }
                if (programme.getYear() != null) {
                    aParams[3] = new DBParams(4, programme.getYear(), CommonTypes.DBType.STRING);
                } else {
                    aParams[3] = new DBParams(4, "", CommonTypes.DBType.STRING);
                }
                if (programme.getRating() != null) {
                    aParams[4] = new DBParams(5, programme.getRating(), CommonTypes.DBType.STRING);
                } else {
                    aParams[4] = new DBParams(5, "", CommonTypes.DBType.STRING);
                }
                if (programme.getType() != null) {
                    aParams[5] = new DBParams(6, programme.getType(), CommonTypes.DBType.STRING);
                } else {
                    aParams[5] = new DBParams(6, "", CommonTypes.DBType.STRING);
                }               
                aParams[6] = new DBParams(7, programme.getCatalog(), CommonTypes.DBType.INTEGER);
                DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DESCRIPTION_INSERT, aParams);
                descriptionID = getDescriptionID(programme);
            }
            saveToDBGenre(descriptionID, programme);
            saveToDBCredits(descriptionID, programme);
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

    private void saveToDBGenre(int descriptionID, Programme programme) {
        if (programme.getGenres() != null) {
            String[] listgr = programme.getGenres().split(Programme.SEP_LIST);
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

    private void saveToDBCredits(int descriptionID, Programme programme) {
        if (programme.getDirectors() != null) {
            String[] listdir = programme.getDirectors().split(Programme.SEP_LIST);
            saveToDBCreditList(listdir, 0, descriptionID);
        }
        if (programme.getActors() != null) {
            String[] listact = programme.getActors().split(Programme.SEP_LIST);
            saveToDBCreditList(listact, 1, descriptionID);
        }
    }

    private int getInsertScheduleID(Programme programme) {
        DBParams[] aParams = new DBParams[3];
        aParams[0] = new DBParams(1, CommonTypes.getDateFormat(programme.getStart(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
        aParams[1] = new DBParams(2, CommonTypes.getDateFormat(programme.getStop(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
        aParams[2] = new DBParams(3, programme.getTitle(), CommonTypes.DBType.STRING);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_MAINSCHEDULE_ID, aParams);
    }

    public void setProgrammeStop() {
        int i = 0;
        while (i != this.data.size()) {
            Programme programme1 = this.data.get(i);
            Programme programme2;
            if (i + 1 != this.data.size()) {
            	programme2 = this.data.get(i+1);
                if (programme1.getIndex() == programme2.getIndex()) {
                	programme1.setStop(programme2.getStart());
                }
            }
            i++;
        }
    }

    public int clearDBSchedule() {
        return DBUtils.getExecuteUpdate(DBUtils.SQL_MAINSCHEDULE_CLEAR);
    }

    public Programme getProgrammeForUrl(String type, int catalog) {
        for (Programme programme : this.data) {
            if (programme.getType().equals(type) && programme.getCatalog() == catalog) {
                return programme;
            }
        }
        return null;
    }

    public void print() {
        for (Programme programme : this.data) {
        	programme.print();
        }
    }

    public void getXML(org.w3c.dom.Document document, org.w3c.dom.Element element) {
        for (Programme programme : this.data) {
        	programme.getXML(document, element);
        }
    }
}
