package common;

import gui.Messages;

public class DBFieldsSetMainChannels extends DBFieldsSet{

    public DBFieldsSetMainChannels() {
        super();
        DBField fld = new DBField("isupd", "", 0);
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", "", 0);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("uname", Messages.getString("FIELD_NAME"), -1);
        this.getFieldSet().add(fld);
        
    }
}
