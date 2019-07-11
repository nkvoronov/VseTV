package common;

public class CategoryProgramme {
    private int id;
    private String nameEN;
    private String nameRU;
    private String dictionary;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getNameRU() {
        return nameRU;
    }

    public void setNameRU(String nameRU) {
        this.nameRU = nameRU;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CategoryProgramme(int id, String nameEN, String nameRU, String dictionary, String color) {
        this.id = id;
        this.nameEN = nameEN;
        this.nameRU = nameRU;
        this.dictionary = dictionary;
        this.color = color;
    }

}
