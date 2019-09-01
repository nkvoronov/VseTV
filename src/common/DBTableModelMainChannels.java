package common;

@SuppressWarnings("serial")
public class DBTableModelMainChannels extends DBTableModelFavoritesChannels {

    public DBTableModelMainChannels(String aQuery) {
        super(aQuery);
        this.fieldsSet = new DBFieldsSetMainChannels();
    }
}
