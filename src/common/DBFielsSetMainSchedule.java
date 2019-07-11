package common;

public class DBFielsSetMainSchedule  extends DBFieldsSet {

    public DBFielsSetMainSchedule() {
        super();
        DBField fld = new DBField("sdate", UtilStrings.FIELD_SDATE);
        this.getFieldSet().add(fld);
        fld = new DBField("edate", UtilStrings.FIELD_EDATE);
        this.getFieldSet().add(fld);
        fld = new DBField("duration", UtilStrings.FIELD_DURATION);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("title", UtilStrings.FIELD_TITLE);
        this.getFieldSet().add(fld);
        fld = new DBField("isdesc", UtilStrings.FIELD_ISDESC);
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_en", UtilStrings.FIELD_CANEN);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_ru", UtilStrings.FIELD_CATRU);
        this.getFieldSet().add(fld);
    }

}
