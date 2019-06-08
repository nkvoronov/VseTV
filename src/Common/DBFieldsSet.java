package Common;

import java.util.ArrayList;

public class DBFieldsSet {
    private ArrayList<DBField> fieldSet;

    public DBFieldsSet() {
        fieldSet = new ArrayList<>();
        DBField fld = new DBField("id", "id");
        fld.setType(Common.DBType.INTEGER);
        fld.setVisible(false);
        fieldSet.add(fld);
    }

    public ArrayList<DBField> getFieldSet() {
        return fieldSet;
    }
}
