package common;

public class DBFieldsSetMainChannels extends DBFieldsSet{

    public DBFieldsSetMainChannels() {
        super();
        DBField fld = new DBField("idx", UtilStrings.FIELD_INDEX);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("sicon", UtilStrings.FIELD_SICON);
        this.getFieldSet().add(fld);
        fld = new DBField("oname", UtilStrings.FIELD_ONAME);
        this.getFieldSet().add(fld);
        fld = new DBField("correction", UtilStrings.FIELD_CORRECTION);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("isupd", UtilStrings.FIELD_ISUPD);
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", UtilStrings.FIELD_PICON);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("uname", UtilStrings.FIELD_NAME);
        this.getFieldSet().add(fld);
    }
}
