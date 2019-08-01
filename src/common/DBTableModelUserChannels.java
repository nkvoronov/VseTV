package common;

@SuppressWarnings("serial")
public class DBTableModelUserChannels extends DBTableModel {

    public DBTableModelUserChannels(String aQuery){
        super(aQuery);
        this.fieldsSet = new DBFieldsSetUserChannels();
    }

}
