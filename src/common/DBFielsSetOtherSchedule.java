package common;

import gui.Messages;

public class DBFielsSetOtherSchedule extends DBFieldsSet {
	
    public DBFielsSetOtherSchedule() {
        super();
        DBField fld = new DBField("picon", "");
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("uname", Messages.getString("FIELD_NAME"));
        this.getFieldSet().add(fld);
        fld = new DBField("sdate", Messages.getString("FIELD_SDATE"));
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
        fld = new DBField("isfav", "");
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_en", Messages.getString("FIELD_CANEN"));
        this.getFieldSet().add(fld);
        fld = new DBField("cat_ru", Messages.getString("FIELD_CATRU"));
        this.getFieldSet().add(fld);
    }

}
