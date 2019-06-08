package Common;

public class DBField {

    private String title;
    private String name;
    private Common.DBType type = Common.DBType.STRING;
    private Boolean visible = true;

    public DBField(String name, String title) {
        this.name = name;
        this.title = title;
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

    public Common.DBType getType() {
        return type;
    }

    public void setType(Common.DBType type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
