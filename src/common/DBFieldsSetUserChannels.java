package common;

import gui.Messages;

public class DBFieldsSetUserChannels extends DBFieldsSet {

    public DBFieldsSetUserChannels() {
        super();
        DBField fld = new DBField("sicon", "", 0);
        this.getFieldSet().add(fld);
        fld = new DBField("picon", "", 0);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", Messages.getString("FIELD_NAME"), 28);
        this.getFieldSet().add(fld);
        fld = new DBField("correction", Messages.getString("FIELD_CORRECTION"), 70);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
    }

}
