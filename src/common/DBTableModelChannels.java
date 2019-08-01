package common;

@SuppressWarnings("serial")
public class DBTableModelChannels extends DBTableModel{

    public DBTableModelChannels(String aQuery){
        super(aQuery);
        this.fieldsSet = new DBFieldsSetChannels();
    }

}
