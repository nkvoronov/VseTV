package common;

public class DBFieldsSetUserChannels extends DBFieldsSet {

    public DBFieldsSetUserChannels() {
        super();
        DBField fld = new DBField("sicon", UtilStrings.FIELD_SICON);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", UtilStrings.FIELD_PICON);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", UtilStrings.FIELD_NAME);
        this.getFieldSet().add(fld);
        fld = new DBField("correction", UtilStrings.FIELD_CORRECTION);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
    }

}
