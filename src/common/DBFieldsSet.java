package common;

import java.util.ArrayList;
import java.util.List;

public class DBFieldsSet {
    private List<DBField> fieldSet;

    public DBFieldsSet() {
        fieldSet = new ArrayList<>();
        DBField fld = new DBField("id", "", 0);
        fld.setType(CommonTypes.DBType.INTEGER);
        fieldSet.add(fld);
    }

    public List<DBField> getFieldSet() {
        return fieldSet;
    }
}
