package common;

public class UtilStrings {
	public static final String FIELD_INDEX         = "index";
	public static final String FIELD_SICON         = "";
	public static final String FIELD_USER          = "";
	public static final String FIELD_PICON         = "";
	public static final String FIELD_NAME          = "Название";
	public static final String FIELD_ONAME         = "";
	public static final String FIELD_CORRECTION    = "Коррекция";
	public static final String FIELD_ISUPD         = "";	
	public static final String FIELD_SDATE         = "Дата";
	public static final String FIELD_EDATE         = "";
	public static final String FIELD_DURATION      = "Длительность";
	public static final String FIELD_TITLE         = "Заголовок";
	public static final String FIELD_ISDESC        = "";
	public static final String FIELD_CANEN         = "cat_en";
	public static final String FIELD_CATRU         = "cat_ru";
	
    public static final String HOST                = "http://www.vsetv.com/";
    public static final String ICONS_PRE           = HOST + "pic/channel_logos/";
    public static final String USER_AGENT          = "Mozilla/5.0 (Windows NT 6.1; rv:5.0) Gecko/20100101 Firefox/5.0";
    public static final String DATE_FORMAT         = "yyyy-MM-dd";    
    public static final String DATE_FORMATTIME     = DATE_FORMAT + " HH:mm:ss";
    public static final String DATE_FORMATTIME2    = "yyyyMMddHHmmss";
    public static final String DATE_FORMATTIME3    = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMATDOT      = "dd.MM.yyyy";
        
    public static final String STR_GETCHANEL       = " Получение канала %s за %s";
    public static final String STR_OTIME           = "00:00";
    public static final String STR_SCHEDULECHANNEL = "schedule_channel_%d_day_%s.html";
    public static final String STR_ELMDOCSELECT    = "div[class~=(?:pasttime|onair|time)]";
    public static final String STR_ELMDOCTITLE     = "div[class~=(?:pastprname2|prname2)]";
    public static final String STR_ELMDOCDESC      = "div[class~=(?:pastdesc|prdesc)]";
    
    public static final String StrCategoryLangRU  = "Без категории";
    public static final String StrCategoryLangEN  = "Other";
    public static final String StrLoadChanels     = " Загрузка каналов ...";
    public static final String StrProcessShelude  = " Обработка расписания ...";
    public static final String StrSaveToBD        = " Запись программы в базу данных (%d из %d)";

    private UtilStrings() {
    	// do not instantiate
	}
}
