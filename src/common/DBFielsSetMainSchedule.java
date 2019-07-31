package common;

import gui.Messages;

public class DBFielsSetMainSchedule  extends DBFieldsSet {

    public DBFielsSetMainSchedule() {
        super();
        DBField fld = new DBField("sdate", Messages.getString("FIELD_SDATE"), 100);
        this.getFieldSet().add(fld);
        fld = new DBField("edate", "", 0);
        this.getFieldSet().add(fld);
        fld = new DBField("duration", Messages.getString("FIELD_DURATION"), 90);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("title", Messages.getString("FIELD_TITLE"), -1);
        this.getFieldSet().add(fld);
        fld = new DBField("isdesc", "", 28);
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("isfav", "", 28);
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("timetype", "", 0);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_en", Messages.getString("FIELD_CANEN"), 0);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_ru", Messages.getString("FIELD_CATRU"), 0);
        this.getFieldSet().add(fld);
    }

}
