package parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.CommonTypes;
import common.UtilStrings;

public class Programme {
    public static final String SEP_LIST = ",";

    private int index;
    private Date start;
    private Date stop;
    private String correctionTime;
    private String title;
    private String description;
    private String urlFullDesc;
    private String categoryLangEN;
    private String categoryLangRU;
    private String genres;
    private String directors;
    private String actors;
    private String year;
    private String country;
    private String image;
    private String starrating;

    public Programme(int index, Date start, Date stop, String title) {
        this.index = index;
        this.start = start;
        this.stop = stop;
        this.correctionTime = null;
        this.title = title;
        this.description = null;
        this.urlFullDesc = null;
        this.categoryLangEN = null;
        this.categoryLangRU = null;
        this.genres = null;
        this.directors = null;
        this.actors = null;
        this.year = null;
        this.country = null;
        this.image =null;
        this.starrating = null;
    }

    public String getCountry() {
        return country;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlFullDesc() {
        return urlFullDesc;
    }

    public void setUrlFullDesc(String urlFullDesc) {
        this.urlFullDesc = urlFullDesc;
    }

    public String getCategoryLangEN() {
        return categoryLangEN;
    }

    public void setCategoryLangEN(String categoryLang) {
        this.categoryLangEN = categoryLang;
    }

    public String getCategoryLangRU() {
        return categoryLangRU;
    }

    public void setCategoryLangRU(String categoryLang) {
        this.categoryLangRU = categoryLang;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStarrating() {
        return starrating;
    }

    public void setStarrating(String starrating) {
        this.starrating = starrating;
    }

    public void copyFullDesc(Programme programme) {
        if (programme != null) {
            this.urlFullDesc = programme.urlFullDesc;
            this.description = programme.description;
            this.categoryLangEN = programme.categoryLangEN;
            this.categoryLangRU = programme.categoryLangRU;
            this.genres = programme.genres;
            this.directors = programme.directors;
            this.actors = programme.actors;
            this.country = programme.country;
            this.image = programme.image;
            this.year = programme.year;
            this.starrating = programme.starrating;
        }
    }

    public void print() {
        System.out.println(getIndex() + Channel.SEP_FOLDER + getStart() + Channel.SEP_FOLDER + getStop() + Channel.SEP_FOLDER + getTitle() + Channel.SEP_FOLDER + getDescription());
    }

    public void setCorrectionTime(int correction) {
        correctionTime = CommonTypes.intToTime(correction, "", true, false);
    }

    private String getDateToFormat(Date date) {
        SimpleDateFormat ftdt = new SimpleDateFormat(UtilStrings.DATE_FORMATTIME2);
        String res = ftdt.format(date);
        if (correctionTime != null) {
            res = res + " " + correctionTime;
        }
        return res;
    }

    public void getXML(Document document, Element element){
        Element eprogramme = document.createElement("programme");
        eprogramme.setAttribute("start", getDateToFormat(this.getStart()));
        eprogramme.setAttribute("stop", getDateToFormat(this.getStop()));
        eprogramme.setAttribute("channel", Integer.toString(getIndex()));
        Element etittle = document.createElement("title");
        etittle.setAttribute("lang", "ru");
        etittle.appendChild(document.createTextNode(getTitle()));
        eprogramme.appendChild(etittle);
        if (getDescription() != null) {
            Element edesc = document.createElement("desc");
            edesc.setAttribute("lang", "ru");
            edesc.appendChild(document.createTextNode(getDescription()));
            eprogramme.appendChild(edesc);
        }
        if (getDirectors() != null ||  getActors() != null) {
            Element ecredits = document.createElement("credits");
            if (getDirectors() != null) {
                String[] strlist = getDirectors().split(SEP_LIST);
                for (String astr:strlist) {
                    Element edirector = document.createElement("director");
                    edirector.appendChild(document.createTextNode(astr));
                    ecredits.appendChild(edirector);
                }
            }
            if (getActors() != null) {
                String[] strlist = getActors().split(SEP_LIST);
                for (String astr:strlist) {
                    Element eactor = document.createElement("actor");
                    eactor.appendChild(document.createTextNode(astr));
                    ecredits.appendChild(eactor);
                }
            }
            eprogramme.appendChild(ecredits);
        }
        if (getYear() != null) {
            Element edate = document.createElement("date");
            edate.appendChild(document.createTextNode(getYear()));
            eprogramme.appendChild(edate);
        }
        if (getCategoryLangEN() != null) {
            Element ecategory1 = document.createElement("category");
            ecategory1.setAttribute("lang", "en");
            ecategory1.appendChild(document.createTextNode(getCategoryLangEN()));
            eprogramme.appendChild(ecategory1);
        }
        if (getCategoryLangRU() != null) {
            Element ecategory2 = document.createElement("category");
            ecategory2.setAttribute("lang", "ru");
            ecategory2.appendChild(document.createTextNode(getCategoryLangRU()));
            eprogramme.appendChild(ecategory2);
        }
        if (getGenres() != null) {
            String[] strlist = getGenres().split(SEP_LIST);
            for (String astr:strlist) {
                Element ecategory3 = document.createElement("category");
                ecategory3.setAttribute("lang", "ru");
                ecategory3.appendChild(document.createTextNode(astr));
                eprogramme.appendChild(ecategory3);
            }
        }
        if (getStarrating() != null) {
            Element erating = document.createElement("star-rating");
            Element evalue = document.createElement("value");
            evalue.appendChild(document.createTextNode(getStarrating()));
            erating.appendChild(evalue);
            eprogramme.appendChild(erating);
        }
        element.appendChild(eprogramme);
    }
}
