package common;

import gui.Messages;

public class DBFieldsSetUserChannels extends DBFieldsSet {

    public DBFieldsSetUserChannels() {
        super();
        DBField fld = new DBField("sicon", "");
        this.getFieldSet().add(fld);
        fld = new DBField("picon", "");
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("name", Messages.getString("FIELD_NAME"));
        this.getFieldSet().add(fld);
        fld = new DBField("correction", Messages.getString("FIELD_CORRECTION"));
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
    }

}
