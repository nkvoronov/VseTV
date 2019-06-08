package Common;

@SuppressWarnings("serial")
public class DBTableModelMainSchedule extends DBTableModel {

    public DBTableModelMainSchedule(String Query) {
        super(Query);
        this.fieldsSet = new DBFielsSetMainSchedule();
    }
}
