package common;

public class DBField {

    private String title;
    private String name;
    private CommonTypes.DBType type = CommonTypes.DBType.STRING;
    private int width = 0;

    public DBField(String name, String title, int width) {
        this.name = name;
        this.title = title;
        this.width = width;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonTypes.DBType getType() {
        return type;
    }

    public void setType(CommonTypes.DBType type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
