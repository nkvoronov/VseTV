package common;

import gui.Messages;

public class DBFieldsSetChannels extends DBFieldsSet{

    public DBFieldsSetChannels() {
        super();
        DBField fld = new DBField("idx", Messages.getString("FIELD_INDEX"), 0);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("sicon", "", 0);
        this.getFieldSet().add(fld);
        fld = new DBField("users", "", 0);
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", "", 28);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", Messages.getString("FIELD_NAME"), 28);
        this.getFieldSet().add(fld);
    }
}
