package common;

import gui.Messages;

public class DBFieldsSetChannels extends DBFieldsSet{

    public DBFieldsSetChannels() {
        super();
        DBField fld = new DBField("channel", "", 0);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
        fld = new DBField("ciconstr", "", 0);
        this.getFieldSet().add(fld);        
        fld = new DBField("isusr", "", 28);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("cicon", "", 28);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("cname", Messages.getString("FIELD_NAME"), -1);
        this.getFieldSet().add(fld);
        fld = new DBField("clang", "", 0);
        this.getFieldSet().add(fld);
    }
}
