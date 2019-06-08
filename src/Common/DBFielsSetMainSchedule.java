package Common;

public class DBFielsSetMainSchedule  extends DBFieldsSet {

    public DBFielsSetMainSchedule() {
        super();
        DBField fld = new DBField("sdate", Strings.FieldSDate);
        this.getFieldSet().add(fld);
        fld = new DBField("edate", Strings.FieldEDate);
        this.getFieldSet().add(fld);
        fld = new DBField("duration", Strings.FieldDuration);
        fld.setType(Common.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("title", Strings.FieldTitle);
        this.getFieldSet().add(fld);
        fld = new DBField("isdesc", Strings.FieldIsDesc);
        fld.setType(Common.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_en", Strings.FieldCatEN);
        this.getFieldSet().add(fld);
        fld = new DBField("cat_ru", Strings.FieldCatRU);
        this.getFieldSet().add(fld);
    }

}
