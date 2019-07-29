package common;

import gui.Messages;

public class DBFieldsSetMainChannels extends DBFieldsSet{

    public DBFieldsSetMainChannels() {
        super();
        DBField fld = new DBField("isupd", "");
        fld.setType(CommonTypes.DBType.BOOL);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", "");
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("uname", Messages.getString("FIELD_NAME"));
        this.getFieldSet().add(fld);
        
    }
}
