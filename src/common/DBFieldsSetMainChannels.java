package common;

import gui.Messages;

public class DBFieldsSetMainChannels extends DBFieldsSet{

    public DBFieldsSetMainChannels() {
        super();
        DBField fld = new DBField("idx", Messages.getString("FIELD_INDEX"));
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("sicon", "");
        this.getFieldSet().add(fld);
        fld = new DBField("oname", "");
        this.getFieldSet().add(fld);
        fld = new DBField("correction", Messages.getString("FIELD_CORRECTION"));
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("isupd", "");
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", "");
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("uname", Messages.getString("FIELD_NAME"));
        this.getFieldSet().add(fld);
    }
}
