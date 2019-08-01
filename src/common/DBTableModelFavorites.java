package common;

@SuppressWarnings("serial")
public class DBTableModelFavorites extends DBTableModel {
	
    public DBTableModelFavorites(String aQuery){
        super(aQuery);
        this.fieldsSet = new DBFielsSetFavorites();
    }

}
