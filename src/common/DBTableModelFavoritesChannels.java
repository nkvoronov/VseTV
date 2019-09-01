package common;

@SuppressWarnings("serial")
public class DBTableModelFavoritesChannels extends DBTableModel {

    public DBTableModelFavoritesChannels(String aQuery){
        super(aQuery);
        this.fieldsSet = new DBFieldsSetFavoritesChannels();
    }

}
