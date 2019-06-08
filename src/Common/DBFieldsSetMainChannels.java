package Common;

public class DBFieldsSetMainChannels extends DBFieldsSet{

    public DBFieldsSetMainChannels() {
        super();
        DBField fld = new DBField("idx", Strings.FieldIndex);
        fld.setType(Common.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("sicon", Strings.FieldSIcon);
        this.getFieldSet().add(fld);
        fld = new DBField("oname", Strings.FieldOName);
        this.getFieldSet().add(fld);
        fld = new DBField("correction", Strings.FieldCorrection);
        fld.setType(Common.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("isupd", Strings.FieldIsUpd);
        fld.setType(Common.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", Strings.FieldPIcon);
        fld.setType(Common.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("uname", Strings.FieldName);
        this.getFieldSet().add(fld);
    }
}
