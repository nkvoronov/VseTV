package Common;

@SuppressWarnings("serial")
public class DBTableModelMainChannels extends DBTableModelUserChannels {

    public DBTableModelMainChannels(String Query) {
        super(Query);
        this.fieldsSet = new DBFieldsSetMainChannels();
    }
}
