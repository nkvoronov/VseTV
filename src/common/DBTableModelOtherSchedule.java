package common;

@SuppressWarnings("serial")
public class DBTableModelOtherSchedule extends DBTableModel {
	
    public DBTableModelOtherSchedule(String aQuery){
        super(aQuery);
        this.fieldsSet = new DBFielsSetOtherSchedule();
    }

}
