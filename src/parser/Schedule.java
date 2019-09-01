package parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Date;

import common.ScheduleCategory;
import common.CommonTypes;
import common.UtilStrings;

public class Schedule {
    public static final String SEP_LIST = ",";

    private int index;
    private Date start;
    private Date stop;
    private String correctionTime;
    private String title;
    private String description;
    private int category;
    private String genres;
    private String directors;
    private String actors;
    private String year;
    private String country;
    private String image;
    private String rating;
    private String type;
    private int catalog;

    public Schedule(int index, Date start, Date stop, String title) {
        this.index = index;
        this.start = start;
        this.stop = stop;
        this.correctionTime = null;
        this.title = title;
        this.description = null;
        this.category = 0;
        this.genres = null;
        this.directors = null;
        this.actors = null;
        this.year = null;
        this.country = null;
        this.image =null;
        this.rating = null;
        this.type = null;
        this.catalog = 0;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public void copyFullDesc(Schedule schedule) {
        if (schedule != null) {
            this.description = schedule.getDescription();
            this.category = schedule.getCategory();
            this.genres = schedule.getGenres();
            this.directors = schedule.getDirectors();
            this.actors = schedule.getActors();
            this.country = schedule.getCountry();
            this.image = schedule.getImage();
            this.year = schedule.getYear();
            this.rating = schedule.getRating();
            this.type = schedule.getType();
            this.catalog = schedule.getCatalog();
        }
    }

    public void print() {
        System.out.println(getIndex() + Channel.SEP_FOLDER + getStart() + Channel.SEP_FOLDER + getStop() + Channel.SEP_FOLDER + getTitle() + Channel.SEP_FOLDER + getDescription());
    }

    public void setCorrectionTime(int correction) {
        correctionTime = CommonTypes.intToTime(correction, "", true, false);
    }

    private String getDateToFormat(Date date) {
        String res = CommonTypes.getDateFormat(date, UtilStrings.DATE_FORMATTIME2);
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
        if (getCategory() != 0) {
        	ScheduleCategory category_res = null;
        	for (ScheduleCategory category : CommonTypes.catList.getData()) {
        		if (category.getId() != 0 && category.getId() == getCategory()) {
        			category_res = category;
        			break;
        		}
        	}
        	if (category_res != null) {
		        if (category_res.getNameEN() != null) {
		            Element ecategory1 = document.createElement("category");
		            ecategory1.setAttribute("lang", "en");
		            ecategory1.appendChild(document.createTextNode(category_res.getNameEN()));
		            eprogramme.appendChild(ecategory1);
		        }
		        if (category_res.getNameRU() != null) {
		            Element ecategory2 = document.createElement("category");
		            ecategory2.setAttribute("lang", "ru");
		            ecategory2.appendChild(document.createTextNode(category_res.getNameRU()));
		            eprogramme.appendChild(ecategory2);
		        }        		
        	}
        }
        if (getGenres() != null) {
            String[] strlist = getGenres().split(SEP_LIST);
            for (String astr:strlist) {
                Element ecategory3 = document.createElement("category");
                ecategory3.setAttribute("lang", "ru");
                ecategory3.appendChild(document.createTextNode(astr.trim()));
                eprogramme.appendChild(ecategory3);
            }
        }
        if (getRating() != null) {
            Element erating = document.createElement("star-rating");
            Element evalue = document.createElement("value");
            evalue.appendChild(document.createTextNode(getRating()));
            erating.appendChild(evalue);
            eprogramme.appendChild(erating);
        }
        element.appendChild(eprogramme);
    }
}
