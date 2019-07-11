package common;

@SuppressWarnings("serial")
public class DBTableModelMainSchedule extends DBTableModel {

    public DBTableModelMainSchedule(String aQuery) {
        super(aQuery);
        this.fieldsSet = new DBFielsSetMainSchedule();
    }
}
