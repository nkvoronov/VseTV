package common;

public class DBParams {
    private int number;
    private Object value;
    private CommonTypes.DBType paramType;

    public int getNumber() {
        return number;
    }

    public Object getValue() {
        return value;
    }

    public CommonTypes.DBType getParamType() {
        return paramType;
    }

    public DBParams(int aNumber, Object aValue, CommonTypes.DBType paramType) {
        this.number = aNumber;
        this.value = aValue;
        this.paramType = paramType;
    }
}
