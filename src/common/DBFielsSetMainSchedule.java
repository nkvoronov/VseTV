package common;

import gui.Messages;

public class DBFielsSetMainSchedule  extends DBFieldsSet {

    public DBFielsSetMainSchedule() {
        super();
        DBField fld = new DBField("sdate", Messages.getString("FIELD_SDATE"));
        this.getFieldSet().add(fld);
        fld = new DBField("edate", "");
        this.getFieldSet().add(fld);
        fld = new DBField("duration", Messages.getString("FIELD_DURATION"));
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("title", Messages.getString("FIELD_TITLE"));
        this.getFieldSet().add(fld);
        fld = new DBField("isdesc", "");
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_en", Messages.getString("FIELD_CANEN"));
        this.getFieldSet().add(fld);
        fld = new DBField("cat_ru", Messages.getString("FIELD_CATRU"));
        this.getFieldSet().add(fld);
    }

}
