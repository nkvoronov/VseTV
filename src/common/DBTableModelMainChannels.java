package common;

@SuppressWarnings("serial")
public class DBTableModelMainChannels extends DBTableModelUserChannels {

    public DBTableModelMainChannels(String aQuery) {
        super(aQuery);
        this.fieldsSet = new DBFieldsSetMainChannels();
    }
}
