package Common;

public class DBFieldsSetChannels extends DBFieldsSet{

    public DBFieldsSetChannels() {
        super();
        DBField fld = new DBField("idx", Strings.FieldIndex);
        fld.setType(Common.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("sicon", Strings.FieldSIcon);
        this.getFieldSet().add(fld);
        fld = new DBField("users", "");
        fld.setType(Common.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", Strings.FieldPIcon);
        fld.setType(Common.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", Strings.FieldName);
        this.getFieldSet().add(fld);
    }
}
