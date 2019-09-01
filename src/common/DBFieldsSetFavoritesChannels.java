package common;

import gui.Messages;

public class DBFieldsSetFavoritesChannels extends DBFieldsSet {

    public DBFieldsSetFavoritesChannels() {
        super();
        DBField fld = new DBField("uiconstr", "", 0);
        this.getFieldSet().add(fld);
        fld = new DBField("uicon", "", 28);
        fld.setType(CommonTypes.DBType.OBJECT);
        this.getFieldSet().add(fld);
        fld = new DBField("uname", Messages.getString("FIELD_NAME"), -1);
        this.getFieldSet().add(fld);
        fld = new DBField("ucorrection", Messages.getString("FIELD_CORRECTION"), 70);
        fld.setType(CommonTypes.DBType.INTEGER);
        this.getFieldSet().add(fld);
    }

}
