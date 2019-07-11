package common;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonTypes {
    public static final String ICON_FOLDER = "icons";    
    public static final int COUNT_DAY = 3;
    public static final Boolean FULL_DESC = false;
    public static final String OUT_XML = "vsetv.xml";
    public static final String LANG = "ru";
    public static final Boolean INDEX_SORT = true;
    
    public enum DBType {STRING, INTEGER, BOOL, DATETIME, TIME, OBJECT}

    public static CategoryProgrammeList catList;

    public static String getIconsPatch() {
        return getCurrentPatch() + File.separator + ICON_FOLDER + File.separator;
    }

    public static String getCurrentPatch() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    public static void loadConfigs() {
        catList = new CategoryProgrammeList();
        catList.loadFromDB();
    }

    public static void saveConfigs() {
        catList.saveToDB();
    }

    public static String addZero(int aValue) {
        if (aValue < 10) {
            return "0" + Integer.toString(aValue);
        } else {
            return Integer.toString(aValue);
        }
    }

    public static String intToTime(int aValue, String sep, Boolean isZN, Boolean isSec) {
        String zn = "";
        int del = 60;
        if (isSec) {
            del = del * 60;
        }
        int crh = aValue / del;
        int crm;
        if (isSec) {
            crm = (aValue - crh * del) / 60;
        } else crm = aValue - crh * del;
        if (isZN) {
            if (aValue >= 0) {
                zn = "+";
            } else zn = "-";
        }
        return zn + addZero(crh) + sep + addZero(crm);
    }

    public static String getMounth(Calendar cal) {
        String month = "";
        switch (cal.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                month = "янв";
                break;
            case Calendar.FEBRUARY:
                month = "фев";
                break;
            case Calendar.MARCH:
                month = "мар";
                break;
            case Calendar.APRIL:
                month = "апр";
                break;
            case Calendar.MAY:
                month = "май";
                break;
            case Calendar.JUNE:
                month = "июн";
                break;
            case Calendar.JULY:
                month = "июл";
                break;
            case Calendar.AUGUST:
                month = "авг";
                break;
            case Calendar.SEPTEMBER:
                month = "сен";
                break;
            case Calendar.OCTOBER:
                month = "окт";
                break;
            case Calendar.NOVEMBER:
                month = "ноя";
                break;
            case Calendar.DECEMBER:
                month = "дек";
                break;
            default:
            	month = "";
            	break;
        }
        return month;
    }

    public static String getWeek(Calendar cal) {
        String week = "";
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                week = "Пн";
                break;
            case Calendar.TUESDAY:
                week = "Вт";
                break;
            case Calendar.WEDNESDAY:
                week = "Ср";
                break;
            case Calendar.THURSDAY:
                week = "Чт";
                break;
            case Calendar.FRIDAY:
                week = "Пт";
                break;
            case Calendar.SATURDAY:
                week = "Сб";
                break;
            case Calendar.SUNDAY:
                week = "Вс";
                break;
            default:
            	week = "";
            	break;
        }
        return week;
    }

    public static String dtFormat(String aValue, Boolean isOnlyTime) {
        String res = "";
        SimpleDateFormat ftdt = new SimpleDateFormat(UtilStrings.DATE_FORMATTIME3);
        Calendar dt = Calendar.getInstance();
        try {
            dt.setTime(ftdt.parse(aValue));
            int day = dt.get(Calendar.DAY_OF_MONTH);
            String month = getMounth(dt);
            String week = getWeek(dt);
            int hour = dt.get(Calendar.HOUR_OF_DAY);
            int minute = dt.get(Calendar.MINUTE);
            if (isOnlyTime) {
                return addZero(hour) + ":" + addZero(minute);
            } else return addZero(day) + " " + month + " (" + week + ") " + addZero(hour) + ":" + addZero(minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int isTimeLine(String sDate, String eDate) {
        int res = -1;
        SimpleDateFormat ftdt = new SimpleDateFormat(UtilStrings.DATE_FORMATTIME3);
        Date cdt = new Date();
        Date sdt;
        Date edt;
        try {
            sdt = ftdt.parse(sDate);
            edt = ftdt.parse(eDate);
            if (cdt.after(edt)) {
                return 0;
            } else if (cdt.before(edt) && cdt.after(sdt)) {
                return 1;
            } else if (edt.after(cdt)) {
                return 2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

}
