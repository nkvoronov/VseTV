package common;

public class UtilStrings {
    public static final String HOST                = "http://www.vsetv.com/";
    public static final String ICONS_PRE           = HOST + "pic/channel_logos/";
    public static final String STR_SCHEDULECHANNEL = HOST + "schedule_channel_%d_day_%s.html";
    public static final String STR_SCHEDULEDESCRIPTION = HOST + "%s_%d.html"; 
    public static final String STR_ICON_NAME       = "%s_%d.gif";
    public static final String STR_IMAGE_NAME      = "%s_%d.jpg";
    public static final String USER_AGENT          = "Mozilla/5.0 (Windows NT 6.1; rv:5.0) Gecko/20100101 Firefox/5.0";
    public static final String DATE_FORMAT         = "yyyy-MM-dd";    
    public static final String DATE_FORMATTIME     = DATE_FORMAT + " HH:mm:ss";
    public static final String DATE_FORMATTIME2    = "yyyyMMddHHmmss";
    public static final String DATE_FORMATTIME3    = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMATDOT      = "dd.MM.yyyy";        
    public static final String STR_GETCHANEL       = " Получение канала %s за %s";
    public static final String STR_OTIME           = "00:00";
    public static final String STR_ELMDOCSELECT    = "div[class~=(?:pasttime|onair|time)]";
    public static final String STR_ELMDOCTITLE     = "div[class~=(?:pastprname2|prname2)]";
    public static final String STR_ELMDOCDESC      = "div[class~=(?:pastdesc|prdesc)]";
    public static final String STR_ELMSHOWNAME      = "td[class=showname]";
    public static final String STR_ELMSHOWMAIN      = "td[class=showmain]";    

    private UtilStrings() {
    	// do not instantiate
	}
}
