package Parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

import static Common.Common.intToTime;
import static Common.Strings.*;

public class Programme {
    public static final String sepLists = ",";

    private int ChannelIdx;
    private Date Start;
    private Date Stop;
    private String CorrectionTime;
    private String Title;
    private String Desc;
    private String UrlFullDesc;
    private String CategoryLangEN;
    private String CategoryLangRU;
    private String Genres;
    private String Directors;
    private String Actors;
    private String Date;
    private String Country;
    private String Image;
    private String Starrating;

    public Programme(int ChannelIdx, Date Start, Date Stop, String Title) {
        this.ChannelIdx = ChannelIdx;
        this.Start = Start;
        this.Stop = Stop;
        this.CorrectionTime = null;
        this.Title = Title;
        this.Desc = null;
        this.UrlFullDesc = null;
        this.CategoryLangEN = null;
        this.CategoryLangRU = null;
        this.Genres = null;
        this.Directors = null;
        this.Actors = null;
        this.Date = null;
        this.Country = null;
        this.Image =null;
        this.Starrating = null;
    }

    public String getCountry() {
        return Country;
    }

    public String getGenres() {
        return Genres;
    }

    public void setGenres(String Genres) {
        this.Genres = Genres;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public int getChannelIdx() {
        return ChannelIdx;
    }

    public void setChannelIdx(int ChannelIdx) {
        this.ChannelIdx = ChannelIdx;
    }

    public Date getStart() {
        return Start;
    }

    public void setStart(Date Start) {
        this.Start = Start;
    }

    public Date getStop() {
        return Stop;
    }

    public void setStop(Date Stop) {
        this.Stop = Stop;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

    public String getUrlFullDesc() {
        return UrlFullDesc;
    }

    public void setUrlFullDesc(String UrlFullDesc) {
        this.UrlFullDesc = UrlFullDesc;
    }

    public String getCategoryLangEN() {
        return CategoryLangEN;
    }

    public void setCategoryLangEN(String CategoryLang1) {
        this.CategoryLangEN = CategoryLang1;
    }

    public String getCategoryLangRU() {
        return CategoryLangRU;
    }

    public void setCategoryLangRU(String CategoryLang2) {
        this.CategoryLangRU = CategoryLang2;
    }

    public String getDirectors() {
        return Directors;
    }

    public void setDirectors(String Directors) {
        this.Directors = Directors;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String Actors) {
        this.Actors = Actors;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getStarrating() {
        return Starrating;
    }

    public void setStarrating(String Starrating) {
        this.Starrating = Starrating;
    }

    public void copyFullDesc(Programme prg) {
        if (prg != null) {
            this.UrlFullDesc = prg.UrlFullDesc;
            this.Desc = prg.Desc;
            this.CategoryLangEN = prg.CategoryLangEN;
            this.CategoryLangRU = prg.CategoryLangRU;
            this.Genres = prg.Genres;
            this.Directors = prg.Directors;
            this.Actors = prg.Actors;
            this.Country = prg.Country;
            this.Image = prg.Image;
            this.Date = prg.Date;
            this.Starrating = prg.Starrating;
        }
    }

    public void print() {
        System.out.println(getChannelIdx() + Channel.sepFolder + getStart() + Channel.sepFolder + getStop() + Channel.sepFolder + getTitle() + Channel.sepFolder + getDesc());
    }

    public void setCorrectionTime(int Correction) {
        CorrectionTime = intToTime(Correction, "", true, false);
    }

    private String GetDateToFormat(Date date) {
        SimpleDateFormat ftdt = new SimpleDateFormat(DateFormatTime2);
        String res = ftdt.format(date);
        if (CorrectionTime != null) {
            res = res + " " + CorrectionTime;
        }
        return res;
    }

    public void getXML(Document document, Element element){
        Element eprogramme = document.createElement("programme");
        eprogramme.setAttribute("start", GetDateToFormat(this.getStart()));
        eprogramme.setAttribute("stop", GetDateToFormat(this.getStop()));
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
                String[] strlist = getDirectors().split(sepLists);
                for (String astr:strlist) {
                    Element edirector = document.createElement("director");
                    edirector.appendChild(document.createTextNode(astr));
                    ecredits.appendChild(edirector);
                }
            }
            if (getActors() != null) {
                String[] strlist = getActors().split(sepLists);
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
            String[] strlist = getGenres().split(sepLists);
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
