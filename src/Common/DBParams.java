package Common;

public class DBParams {
    private int Number;
    private Object Value;
    private Common.DBType ParamType;

    public int getNumber() {
        return Number;
    }

    public Object getValue() {
        return Value;
    }

    public Common.DBType getParamType() {
        return ParamType;
    }

    public DBParams(int Number, Object Value, Common.DBType ParamType) {
        this.Number = Number;
        this.Value = Value;
        this.ParamType = ParamType;
    }
}
