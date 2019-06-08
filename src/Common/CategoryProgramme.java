package Common;

public class CategoryProgramme {
    private int id;
    private String name_en;
    private String name_ru;
    private String dictionary;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
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

    public CategoryProgramme(int id, String name_en, String name_ru, String dictionary, String color) {
        this.id = id;
        this.name_en = name_en;
        this.name_ru = name_ru;
        this.dictionary = dictionary;
        this.color = color;
    }

}
