package Common;

public class DBFieldsSetUserChannels extends DBFieldsSet {

    public DBFieldsSetUserChannels() {
        super();
        DBField fld = new DBField("sicon", Strings.FieldSIcon);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", Strings.FieldPIcon);
        fld.setType(Common.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", Strings.FieldName);
        this.getFieldSet().add(fld);
        fld = new DBField("correction", Strings.FieldCorrection);
        fld.setType(Common.DBType.INTEGER);
        this.getFieldSet().add(fld);
    }

}
