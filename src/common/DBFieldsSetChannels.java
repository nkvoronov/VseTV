package common;

public class DBFieldsSetChannels extends DBFieldsSet{

    public DBFieldsSetChannels() {
        super();
        DBField fld = new DBField("idx", UtilStrings.FIELD_INDEX);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("sicon", UtilStrings.FIELD_SICON);
        this.getFieldSet().add(fld);
        fld = new DBField("users", "");
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", UtilStrings.FIELD_PICON);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", UtilStrings.FIELD_NAME);
        this.getFieldSet().add(fld);
    }
}
