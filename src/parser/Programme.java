package parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.CommonTypes;
import common.UtilStrings;

public class Programme {
    public static final String SEP_LIST = ",";

    private int channelIdx;
    private Date start;
    private Date stop;
    private String correctionTime;
    private String title;
    private String desc;
    private String urlFullDesc;
    private String categoryLangEN;
    private String categoryLangRU;
    private String genres;
    private String directors;
    private String actors;
    private String date;
    private String country;
    private String image;
    private String starrating;

    public Programme(int channelIdx, Date start, Date stop, String title) {
        this.channelIdx = channelIdx;
        this.start = start;
        this.stop = stop;
        this.correctionTime = null;
        this.title = title;
        this.desc = null;
        this.urlFullDesc = null;
        this.categoryLangEN = null;
        this.categoryLangRU = null;
        this.genres = null;
        this.directors = null;
        this.actors = null;
        this.date = null;
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

    public int getChannelIdx() {
        return channelIdx;
    }

    public void setChannelIdx(int channelIdx) {
        this.channelIdx = channelIdx;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarrating() {
        return starrating;
    }

    public void setStarrating(String starrating) {
        this.starrating = starrating;
    }

    public void copyFullDesc(Programme prg) {
        if (prg != null) {
            this.urlFullDesc = prg.urlFullDesc;
            this.desc = prg.desc;
            this.categoryLangEN = prg.categoryLangEN;
            this.categoryLangRU = prg.categoryLangRU;
            this.genres = prg.genres;
            this.directors = prg.directors;
            this.actors = prg.actors;
            this.country = prg.country;
            this.image = prg.image;
            this.date = prg.date;
            this.starrating = prg.starrating;
        }
    }

    public void print() {
        System.out.println(getChannelIdx() + Channel.SEP_FOLDER + getStart() + Channel.SEP_FOLDER + getStop() + Channel.SEP_FOLDER + getTitle() + Channel.SEP_FOLDER + getDesc());
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
        eprogramme.setAttribute("channel", Integer.toString(getChannelIdx()));
        Element etittle = document.createElement("title");
        etittle.setAttribute("lang", "ru");
        etittle.appendChild(document.createTextNode(getTitle()));
        eprogramme.appendChild(etittle);
        if (getDesc() != null) {
            Element edesc = document.createElement("desc");
            edesc.setAttribute("lang", "ru");
            edesc.appendChild(document.createTextNode(getDesc()));
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
        if (getDate() != null) {
            Element edate = document.createElement("date");
            edate.appendChild(document.createTextNode(getDate()));
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
