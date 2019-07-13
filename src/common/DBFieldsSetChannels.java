package common;

import gui.Messages;

public class DBFieldsSetChannels extends DBFieldsSet{

    public DBFieldsSetChannels() {
        super();
        DBField fld = new DBField("idx", Messages.getString("FIELD_INDEX"));
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("sicon", "");
        this.getFieldSet().add(fld);
        fld = new DBField("users", "");
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", "");
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", Messages.getString("FIELD_NAME"));
        this.getFieldSet().add(fld);
    }
}
